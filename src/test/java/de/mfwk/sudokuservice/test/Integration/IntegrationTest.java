package de.mfwk.sudokuservice.test.Integration;

import de.mfwk.sudokuservice.client.SudokuClient;
import de.mfwk.sudokuservice.core.Sudoku;
import de.mfwk.sudokuservice.server.SudokuServer;
import de.mfwk.sudokuservice.test.TestData;
import de.mfwk.sudokuservice.test.TestUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by micha on 28.04.2017.
 */
public class IntegrationTest {
    private static SudokuServer server;
    private static SudokuClient client;

    @BeforeClass
    public static void setup() throws MalformedURLException {
        server = new SudokuServer();
        server.start(1337);
        client = new SudokuClient("localhost", 1337);
    }

    @AfterClass
    public static void teardown() {
        server.shutdown();
        server = null;
        client = null;
    }

    @Test
    public void testPing() {
        assertTrue("Ping failed", client.pingrequest());
    }

    @Test
    public void testValidation() {
        assertEquals("Validation of valid failed", 0, client.validaterequest(new Sudoku(TestData.sudokus.get("hardsolved"))));
    }

    @Test
    public void testSolve() {
        assertTrue("Solving failed", TestUtil.isSolved(client.solverequest(new Sudoku(TestData.sudokus.get("easy")))));
    }

    @Test
    public void testSolveBT() {
        assertTrue("Solving with BT failed", TestUtil.isSolved(client.solverguessingrequest(new Sudoku(TestData.sudokus.get("amb")))));
    }
}
