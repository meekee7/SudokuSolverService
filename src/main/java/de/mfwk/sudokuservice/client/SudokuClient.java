package de.mfwk.sudokuservice.client;

import de.mfwk.sudokuservice.core.Sudoku;
import de.mfwk.sudokuservice.core.SudokuAccess;
import de.mfwk.sudokuservice.core.SudokuService;
import de.mfwk.sudokuservice.server.SudokuServer;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SudokuClient {
    public static final String selfhosted = "selfhosted"; //Use this as the hostname when you want to use an anonymous internal server

    private final SudokuService service;

    /**
     * Constructor creating a new client for the service.
     *
     * @param hostname The host name or ip address of the server.
     * @param port     The port of the service on the server.
     * @throws MalformedURLException Happens when the resulting URL is invalid.
     */
    public SudokuClient(String hostname, int port) throws MalformedURLException {
        if (hostname.equals(SudokuClient.selfhosted))
            this.service = new SudokuServer();
        else
            this.service = Service.create(new URL("http", hostname, port, "/sudoku"), new QName("http://server.sudokuservice.mfwk.de/", "SudokuServerService")).getPort(SudokuService.class);
    }

    /**
     * Proposes a request to resolve a sudoku with backtracking
     *
     * @param sudoku The sudoku to solve.
     * @return The solved sudoku.
     */
    public Sudoku solverguessingrequest(Sudoku sudoku) {
        return this.service.solveSudokuGuessing(sudoku);
    }

    /**
     * Proposes a request to resolve a sudoku.
     *
     * @param sudoku The sudoku to solve.
     * @return The solved sudoku.
     */
    public Sudoku solverequest(Sudoku sudoku) {
        return this.service.solveSudoku(sudoku);
    }

    /**
     * Proposes a request to determine the status of the sudoku.
     *
     * @param sudoku The sudoku to check.
     * @return -1 if invalid, 0 if complete, the number of open fields if incomplete but valid.
     */
    public int validaterequest(Sudoku sudoku) {
        return this.service.validateSudoku(sudoku);
    }

    /**
     * Tries to access the server.
     *
     * @return Whether the server is accessible.
     */
    public boolean pingrequest() {
        try {
            return this.service.ping();
        } catch (Exception e) {           //The ping fails when the server is not accessible
            return false;
        }
    }

    /**
     * Main method to run the client from the command line.
     *
     * @param args The arguments for the client.
     */
    public static void main(String[] args) {
        String hostname = null;
        int port = 0;
        Path sudokufile = null, outputfile = null;
        boolean solvebtoption = false, solveoption = false, statusoption = false, ping = false;
        for (int i = 0; i < args.length; i++)                         //Parse the arguments from the command line
            switch (args[i]) {
                case "-solvebt":
                    solvebtoption = true;
                    break;
                case "-solve":
                    solveoption = true;
                    break;
                case "-valid":
                case "-status":
                    statusoption = true;
                    break;
                case "-ping":
                    ping = true;
                    break;
                case "-host":
                    if (hostname == null)
                        if (i + 1 < args.length)
                            hostname = args[i + 1];
                        else
                            System.out.println("Error: host name not specified.");
                    break;
                case "-port":
                    if (port == 0)
                        if (i + 1 < args.length)
                            try {
                                port = Integer.parseInt(args[i + 1]);
                            } catch (NumberFormatException e) {
                                System.err.println("Error: invalid port number.");
                            }
                        else
                            System.out.println("Error: port number not specified.");
                    break;
                case "-file":
                case "-src":
                    if (sudokufile == null)
                        if (i + 1 < args.length)
                            sudokufile = Paths.get(args[i + 1]);
                        else
                            System.out.println("Error: host name not specified.");
                    break;
                case "-out":
                case "-dest":
                    if (outputfile == null)
                        if (i + 1 < args.length)
                            outputfile = Paths.get(args[i + 1]);
                        else
                            System.out.println("Error: host name not specified.");
                    break;
            }

        if (port == 0) {
            System.out.println("Port unspecified, will use standard port 1337.");
            port = 1337;
        }

        SudokuClient client = null;
        try {
            client = new SudokuClient(hostname, port);
        } catch (MalformedURLException e) {
            System.err.println("Error: host name or port invalid.");
            e.printStackTrace();
            System.exit(1);
        }

        if (ping) {
            if (client.pingrequest())
                System.out.println("Ping successful, host connection could be established.");
            else
                System.out.println("Ping not successful, host connection could not be established.");
            return;
        }

        Sudoku sudoku = null;
        if (sudokufile != null)
            try {
                sudoku = SudokuAccess.readSudoku(sudokufile);
            } catch (JAXBException e) {
                System.err.println("Error: could not interpret data.");
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Error: could not read file.");
                e.printStackTrace();
                System.exit(1);
            }
        else {
            System.err.println("Error: no source file specified.");
            System.exit(1);
        }

        if (statusoption) {
            int result = client.validaterequest(sudoku);
            if (result < 0)
                System.out.println("The sudoku is not valid.");
            else if (result == 0)
                System.out.println("The sudoku is valid and completely solved.");
            else
                System.out.println("The sudoku is valid, but " + result + " fields are open.");
        }

        if (solvebtoption)
            sudoku = client.solverguessingrequest(sudoku);
        else if (solveoption)
            sudoku = client.solverequest(sudoku);

        if (solveoption || solvebtoption) {
            if (outputfile != null)
                try {
                    SudokuAccess.writeSudoku(outputfile, sudoku);
                } catch (JAXBException e) {
                    System.err.println("Error: could not write data.");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Error: could not save file.");
                    e.printStackTrace();
                }
            else
                System.out.println(sudoku);
        }
    }
}
