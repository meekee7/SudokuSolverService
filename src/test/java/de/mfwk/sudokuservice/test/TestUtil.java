package de.mfwk.sudokuservice.test;

import de.mfwk.sudokuservice.core.Sudoku;
import de.mfwk.sudokuservice.server.SudokuSolver;

/**
 * Created by micha on 30.04.2017.
 */
public class TestUtil {
    public static boolean isSolved(Sudoku s) {
        return new SudokuSolver(s).getStatus() == 0;
    }
}
