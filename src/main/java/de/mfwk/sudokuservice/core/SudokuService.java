package de.mfwk.sudokuservice.core;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
@WebService(serviceName = "SudokuService")
public interface SudokuService {
    /**
     * Solves a sudoku, but also uses backtracking.
     *
     * @param sudoku The sudoku to solve.
     * @return The solved sudoku.
     */
    @WebMethod(operationName = "solveGuessing")
    @WebResult(name = "solution")
    public Sudoku solveSudokuGuessing(@WebParam(name = "puzzle") Sudoku sudoku);

    /**
     * Solves a sudoku.
     *
     * @param sudoku The sudoku to solve.
     * @return The solved sudoku.
     */
    @WebMethod(operationName = "solve")
    @WebResult(name = "solution")
    public Sudoku solveSudoku(@WebParam(name = "puzzle") Sudoku sudoku);

    /**
     * Determines the status of the sudoku.
     *
     * @param sudoku The sudoku to check.
     * @return -1 if invalid, 0 if complete, the number of open fields if incomplete but valid.
     */
    @WebMethod(operationName = "validate")
    @WebResult(name = "validation")
    public int validateSudoku(@WebParam(name = "puzzle") Sudoku sudoku);

    /**
     * An attempt to test whether the server is accessible.
     *
     * @return Always true if the server answers.
     */
    @WebMethod(operationName = "ping")
    @WebResult(name = "accessible")
    public boolean ping();
}
