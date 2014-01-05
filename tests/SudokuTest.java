import SudokuCore.Sudoku;
import SudokuServer.SudokuSolver;
import org.junit.Test;

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


    @Test
    public void testgetLine() {
        SudokuSolver sudoku = new SudokuSolver(new Sudoku(easysudoku));
        for (int i = 0; i < 9; i++)
            org.junit.Assert.assertArrayEquals("Zeile " + (i + 1) + "nicht korrekt", easysudoku[i], Sudoku.fieldtoint(sudoku.getLine(i + 1)));
    }

    @Test
    public void testgetColumn() {
        int[][] transponded = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                transponded[i][j] = easysudoku[j][i];
        SudokuSolver sudoku = new SudokuSolver(new Sudoku(easysudoku));
        for (int i = 0; i < 9; i++)
            org.junit.Assert.assertArrayEquals("Spalte " + (i + 1) + " nicht korrekt", transponded[i], Sudoku.fieldtoint(sudoku.getColumn(i + 1)));
    }

    @Test
    public void testgetSquare() {
        int[][] squares = new int[9][9];
        for (int i = 1; i < 9; i++)
            ;                              //TODO formal test
        Sudoku sudoku = new Sudoku(easysudoku);

//        for (int i = 1; i <= 9; i++)
//            System.out.println(java.util.Arrays.toString(SudokuService.SudokuCore.SudokuCore.fieldtoint(sudoku.getSquare(i))));
    }

    @Test
    public void testsimple() {
        org.junit.Assert.assertEquals("Simplesudoku nicht korrekt gelöst", 0, new SudokuSolver(new Sudoku(simplesudoku)).solve());
    }

    @Test
    public void testeasy() {
        org.junit.Assert.assertEquals("Easysudoku nicht korrekt gelöst", 0, new SudokuSolver(new Sudoku(easysudoku)).solve());
    }

    @Test
    public void testmedium() {
        org.junit.Assert.assertEquals("Mediumsudoku nicht korrekt gelöst", 0, new SudokuSolver(new Sudoku(mediumsudoku)).solve());
    }

    @Test
    public void testhard() {
        org.junit.Assert.assertEquals("Hardsudoku nicht korrekt gelöst", 0, new SudokuSolver(new Sudoku(hardsudoku)).solve());
    }
}
