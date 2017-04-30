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
        for (String s : TestData.solvable)
            assertEquals(s + " nicht korrekt gelöst", 0, new SudokuSolver(new Sudoku(TestData.sudokus.get(s))).solvelogically());
    }

    @Test
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

    }

    @Test
    public void testValidateInvalid() {

    }
}
