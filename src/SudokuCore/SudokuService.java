package SudokuCore;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
@WebService
public interface SudokuService {
    /**
     * Solves a sudoku.
     *
     * @param sudoku The sudoku to solve.
     * @return The solved sudoku.
     */
    @WebMethod
    public Sudoku solveSudoku(Sudoku sudoku);

    /**
     * Determines the status of the sudoku.
     *
     * @param sudoku The sudoku to check.
     * @return -1 if invalid, 0 if complete, the number of open fields if incomplete but valid.
     */
    @WebMethod
    public int validateSudoku(Sudoku sudoku);

    /**
     * An attempt to test whether the server is accessible.
     *
     * @return Always true if the server answers.
     */
    @WebMethod
    public boolean ping();
}
