package SudokuServer;

import SudokuCore.Sudoku;
import SudokuCore.SudokuService;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
@WebService(endpointInterface = "SudokuCore.SudokuService")
public class SudokuServer implements SudokuService {

    /**
     * Solves a sudoku.
     *
     * @param sudoku The sudoku to solve.
     * @return The solved sudoku.
     */
    @Override
    public Sudoku solveSudoku(Sudoku sudoku) {
        SudokuSolver solver = new SudokuSolver(sudoku);
        solver.solve();
        return solver;
    }

    /**
     * Determines the status of the sudoku.
     *
     * @param sudoku The sudoku to check.
     * @return -1 if invalid, 0 if complete, the number of open fields if incomplete but valid.
     */
    @Override
    public int validateSudoku(Sudoku sudoku) {
        return new SudokuSolver(sudoku).getStatus();
    }

    /**
     * An attempt to test whether the server is accessible.
     *
     * @return Always true if the server answers.
     */
    @Override
    public boolean ping() {
        return true;
    }

    /**
     * Main method to start the server from the command line. Run wsgen before starting.
     *
     * @param args The first optional argument is the port number to use, all other parameters are ignored.
     */
    public static void main(String[] args) {
        int port = 1337;
        if (args.length > 0)                           //Try to get port number from program arguments
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Error: first parameter was no legal port number, will use standard port 1337");
            }
        else
            System.out.println("No port number specified, will use standard port 1337.");

        URL url = null;
        URL local = null;
        try {                                         //Generate URLs for the service
            url = new URL("http", InetAddress.getLocalHost().getCanonicalHostName(), port, "/sudoku");
            local = new URL("http", "localhost", port, "/sudoku");
            System.out.println();                     //Output of implicated configuration
            System.out.println("Server configuration is: ");
            System.out.println("Host name: " + InetAddress.getLocalHost().getCanonicalHostName());
            System.out.println("Port: " + port);
            System.out.println("IP Address: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Server complete URL is: " + url);
        } catch (MalformedURLException e) {
            System.err.println("Error: could not create server configuration.");
            System.exit(1);
        } catch (UnknownHostException e) {
            System.err.println("Error: could not determine network configuration data.");
            System.exit(1);
        }

        SudokuServer server = new SudokuServer();                  //Start the service for the public host and localhost
        final Endpoint endpointnetw = Endpoint.publish(url.toString(), server);
        final Endpoint endpointloho = Endpoint.publish(local.toString(), server);
        System.out.println("Sudoku web service started.");         //If errors occur around this, classes are not found
        System.out.println("Use Ctrl + C to stop the server.");    //Then make your that you have run the wsgen tool

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            /**
             * This method will stop the server when the program is terminating in a regular way.
             */
            @Override
            public void run() {
                System.out.println("Server will shut down now.");
                endpointnetw.stop();
                endpointloho.stop();
            }
        }));
    }
}
