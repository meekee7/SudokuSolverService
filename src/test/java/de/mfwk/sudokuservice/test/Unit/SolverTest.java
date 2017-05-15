package de.mfwk.sudokuservice.test.Unit;

import de.mfwk.sudokuservice.core.Sudoku;
import de.mfwk.sudokuservice.server.SudokuSolver;
import de.mfwk.sudokuservice.test.TestData;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SolverTest {

    @Test
    public void testgetLine() {
        int[][] easysudoku = TestData.sudokus.get("easy");
        SudokuSolver sudoku = new SudokuSolver(new Sudoku(easysudoku));
        for (int i = 0; i < 9; i++)
            assertArrayEquals("Zeile " + (i + 1) + "nicht korrekt", easysudoku[i], Sudoku.fieldtoint(sudoku.getLine(i + 1)));
    }

    @Test
    public void testgetColumn() {
        int[][] easysudoku = TestData.sudokus.get("easy");
        int[][] transponded = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                transponded[i][j] = easysudoku[j][i];
        SudokuSolver sudoku = new SudokuSolver(new Sudoku(easysudoku));
        for (int i = 0; i < 9; i++)
            assertArrayEquals("Spalte " + (i + 1) + " nicht korrekt", transponded[i], Sudoku.fieldtoint(sudoku.getColumn(i + 1)));
    }

    @Test
    public void testSolve() {
        for (String s : TestData.solvable) {
            SudokuSolver solver = new SudokuSolver(new Sudoku(TestData.sudokus.get(s)));
            solver.solvesimple();
            assertEquals(s + " nicht korrekt gelöst: \n" + solver.toStringWithOptions(), 0, solver.getStatus());
        }
    }

    @Test(timeout = 1000L)
    public void testSolveBT() {
        assertEquals("Backtracking-Sudoku nicht korrekt gelöst", 0, new SudokuSolver(new Sudoku(TestData.sudokus.get("amb"))).solve());
    }

    @Test
    public void testValidateIncomplete() {
        for (String s : TestData.solvable)
            assertNotEquals(s + " nicht als unvollständig validiert", 0, new SudokuSolver(new Sudoku(TestData.sudokus.get(s))).getStatus());
    }

    @Test
    public void testValidateComplete() {
        assertEquals("Completed sudoku not validated to 0", 0, new SudokuSolver(new Sudoku(TestData.sudokus.get("hardsolved"))).getStatus());
    }

    @Test
    public void testValidateInvalid() {
        assertEquals("Incomplete invalid not validated to -1", -1, new SudokuSolver(new Sudoku(TestData.sudokus.get("invalid1"))).getStatus());
        assertEquals("Complete invalid not validated to -1", -1, new SudokuSolver(new Sudoku(TestData.sudokus.get("invalid2"))).getStatus());
    }

    /**
     * This test is intentionally broken and unsolvable
     */
//    @Test
    public void testMediumSudokuSolvable() {
        assertEquals("Medium sudoku not solved correctly", 0, new SudokuSolver(new Sudoku(TestData.sudokus.get("medium"))).getStatus());
    }

    /**
     * This test is intentionally broken and unsolvable
     */
//    @Test
    public void testBTSudokuSolvable() {
        assertEquals("Ambiguous BT sudoku not solved correctly", 0, new SudokuSolver(new Sudoku(TestData.sudokus.get("amb"))).getStatus());
    }
}
