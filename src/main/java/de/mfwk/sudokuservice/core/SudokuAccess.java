package de.mfwk.sudokuservice.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SudokuAccess {
    private static Marshaller mar = null;
    private static Unmarshaller unmar = null;

    /**
     * Creates the marshaller and the unmarshaller.
     */
    private static void create() {
        try {
            JAXBContext context = JAXBContext.newInstance(Sudoku.class);
            mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            unmar = context.createUnmarshaller();
        } catch (JAXBException e) {
            System.err.println("Could not complete JAXB initialisation.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reads a sudoku from a file.
     *
     * @param path The path of the file.
     * @return The sudoku.
     * @throws JAXBException Happens when the interpretation failed.
     * @throws IOException   Happens when file access failed.
     */
    public static Sudoku readSudoku(Path path) throws JAXBException, IOException {
        if (unmar == null)
            create();
        return (Sudoku) unmar.unmarshal(Files.newInputStream(path, StandardOpenOption.READ));
    }

    /**
     * Stores a sudoku in a file.
     *
     * @param path   The file to store the sudoku in.
     * @param sudoku The sudoku to store.
     * @throws JAXBException Happens when the interpretation failed.
     * @throws IOException   Happens when file access failed.
     */
    public static void writeSudoku(Path path, Sudoku sudoku) throws JAXBException, IOException {
        if (mar == null)
            create();
        if (!Files.exists(path))
            Files.createFile(path);
        mar.marshal(sudoku, Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
    }
}
