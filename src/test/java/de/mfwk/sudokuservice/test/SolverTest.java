package de.mfwk.sudokuservice.test;

import de.mfwk.sudokuservice.core.Sudoku;
import de.mfwk.sudokuservice.server.SudokuSolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.mfwk.sudokuservice.test.TestData.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SolverTest {

    @BeforeEach
    public void setup() {
        System.out.println("SETUP");
    }

    @AfterEach
    public void teardown() {
        System.out.println("TEARDOWN");
    }

    @Test
    public void testgetLine() {
        SudokuSolver sudoku = new SudokuSolver(new Sudoku(easysudoku));
        for (int i = 0; i < 9; i++)
            assertArrayEquals(easysudoku[i], Sudoku.fieldtoint(sudoku.getLine(i + 1)), "Zeile " + (i + 1) + "nicht korrekt");
    }

    @Test
    public void testgetColumn() {
        int[][] transponded = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                transponded[i][j] = easysudoku[j][i];
        SudokuSolver sudoku = new SudokuSolver(new Sudoku(easysudoku));
        for (int i = 0; i < 9; i++)
            assertArrayEquals(transponded[i], Sudoku.fieldtoint(sudoku.getColumn(i + 1)), "Spalte " + (i + 1) + " nicht korrekt");
    }

    @Test
    public void testsimple() {
        assertEquals(0, new SudokuSolver(new Sudoku(simplesudoku)).solve(), "Simplesudoku nicht korrekt gelöst");
    }

    @Test
    public void testeasy() {
        assertEquals(0, new SudokuSolver(new Sudoku(easysudoku)).solve(), "Easysudoku nicht korrekt gelöst");
    }

    @Test
    public void testmedium() {
        assertEquals(0, new SudokuSolver(new Sudoku(mediumsudoku)).solve(), "Mediumsudoku nicht korrekt gelöst");
    }

    @Test
    public void testhard() {
        assertEquals(0, new SudokuSolver(new Sudoku(hardsudoku)).solve(), "Hardsudoku nicht korrekt gelöst");
    }

    @Test
    public void testdebug1() {
        assertEquals(0, new SudokuSolver(new Sudoku(debug1sudoku)).solve(), "Debug1 nicht korrekt gelöst");
    }

    @Test
    public void testdebug2() {
        assertEquals(0, new SudokuSolver(new Sudoku(debug2sudoku)).solve(), "Debug2 nicht korrekt gelöst");
    }
}
