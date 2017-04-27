import SudokuCore.Sudoku;
import SudokuServer.SudokuSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SudokuTest {

    private static int[][] simplesudoku = {
            {8, 9, 0, 2, 1, 0, 0, 7, 0},
            {6, 0, 0, 0, 0, 7, 0, 0, 0},
            {0, 2, 0, 8, 0, 6, 0, 0, 0},
            {0, 7, 0, 0, 5, 1, 0, 9, 8},
            {0, 6, 0, 0, 0, 0, 0, 3, 0},
            {0, 0, 4, 0, 0, 0, 0, 0, 0},
            {0, 5, 0, 6, 0, 0, 3, 0, 0},
            {0, 0, 0, 0, 0, 9, 8, 0, 5},
            {3, 0, 0, 0, 0, 0, 7, 0, 0},
    };

    private static int[][] easysudoku = {
            {0, 0, 0, 0, 0, 0, 3, 0, 0},
            {0, 0, 0, 0, 7, 1, 5, 0, 0},
            {0, 0, 2, 4, 0, 0, 0, 1, 8},
            {0, 0, 0, 0, 0, 9, 0, 4, 0},
            {0, 9, 0, 6, 1, 8, 2, 3, 0},
            {6, 1, 0, 7, 0, 0, 0, 0, 0},
            {4, 3, 0, 8, 0, 7, 6, 0, 0},
            {0, 0, 8, 1, 4, 0, 0, 0, 0},
            {0, 0, 9, 0, 0, 0, 0, 0, 0},
    };

    private static int[][] mediumsudoku = {
            {0, 0, 0, 0, 0, 0, 2, 0, 0},
            {0, 5, 8, 0, 0, 6, 0, 0, 0},
            {0, 0, 0, 3, 0, 0, 0, 8, 5},
            {0, 1, 0, 4, 7, 0, 6, 0, 0},
            {0, 0, 6, 0, 0, 0, 5, 0, 7},
            {0, 0, 7, 0, 3, 9, 0, 4, 0},
            {7, 6, 0, 0, 0, 8, 0, 0, 0},
            {0, 0, 0, 9, 0, 0, 8, 1, 0},
            {0, 0, 9, 0, 0, 0, 0, 0, 0},
    };

    private static int[][] hardsudoku = {
            {0, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 3, 0, 0, 9, 0},
            {0, 4, 0, 0, 0, 6, 5, 0, 0},
            {0, 5, 9, 0, 0, 0, 8, 0, 0},
            {3, 0, 0, 0, 0, 0, 0, 1, 0},
            {8, 0, 0, 2, 0, 0, 0, 0, 7},
            {2, 3, 0, 0, 0, 8, 0, 0, 0},
            {4, 0, 5, 0, 0, 0, 1, 0, 0},
            {0, 1, 7, 3, 2, 0, 0, 0, 0},
    };

    private static int[][] debug1sudoku = {
            {8, 0, 0, 1, 3, 0, 0, 0, 4},
            {0, 2, 0, 0, 0, 5, 0, 7, 0},
            {0, 0, 7, 0, 0, 0, 6, 0, 0},
            {0, 0, 1, 5, 0, 0, 0, 9, 2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {4, 6, 0, 0, 0, 2, 3, 0, 0},
            {0, 0, 3, 0, 0, 0, 2, 0, 0},
            {0, 4, 0, 3, 0, 0, 0, 1, 0},
            {6, 0, 0, 0, 1, 8, 0, 0, 9},
    };

    private static int[][] debug2sudoku = {
            {4, 2, 6, 0, 0, 0, 0, 0, 8},
            {8, 1, 7, 5, 0, 0, 0, 9, 0},
            {9, 3, 5, 0, 2, 0, 4, 0, 0},
            {0, 0, 1, 0, 0, 3, 0, 0, 0},
            {2, 8, 0, 0, 0, 0, 0, 7, 3},
            {0, 0, 0, 8, 0, 0, 5, 0, 0},
            {7, 4, 2, 0, 1, 0, 6, 0, 0},
            {5, 6, 8, 0, 0, 7, 0, 3, 0},
            {1, 9, 3, 0, 0, 0, 0, 0, 5},
    };

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
