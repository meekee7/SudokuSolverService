package SudokuServer;

import SudokuCore.Sudoku;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SudokuSolver extends Sudoku {
    private static final List<Integer> empty = new ArrayList<>();
    private static final List<Integer> all = new ArrayList<Integer>() {{
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);
        add(7);
        add(8);
        add(9);
    }};

    private class OptionField extends Field {
        private List<Integer> options;

        /**
         * Constructor creating an empty field with all options.
         */
        public OptionField() {
            super();
            this.options = new ArrayList<>(all);
        }

        /**
         * Constructor creating a new field. When there is no value the field has all options.
         *
         * @param value The value of the new field, 0 if the field is empty.
         */
        public OptionField(int value) {
            super(value);
            if (value == 0)
                this.options = new ArrayList<>(all);
            else
                this.options = empty;
        }

        /**
         * Extended setter for the value removing the options.
         *
         * @param value The new value of the field.
         * @throws IllegalArgumentException Happens when the value is invalid.
         */
        @Override
        public void setValue(int value) throws IllegalArgumentException {
            super.setValue(value);
            this.options = empty;
        }

        /**
         * Getter for the options.
         *
         * @return The options of the field.
         */
        public List<Integer> getOptions() {
            return this.options;
        }

        /**
         * Removes an option of the field.
         *
         * @param value The option for removal.
         * @return If the removal leads to the determination of the value of the field, the value is returned, else 0.
         */
        public int removeoption(Integer value) {
            this.options.remove(value);
            if (this.setonoption())
                return this.value;
            else
                return 0;
        }

        /**
         * Determinates whether the field has a certain option.
         *
         * @param value The option to check.
         * @return Whether the field has the option.
         */
        public boolean hasOption(Integer value) {
            return this.options.contains(value);
        }

        /**
         * Checks whether the field has only one option. If this is the case, then the value is set to the option.
         *
         * @return True if there was indeed only one option, false if not.
         */
        public boolean setonoption() {
            if (this.options.size() == 1) {
                this.value = options.get(0);
                this.options = empty;
                return true;
            } else
                return false;
        }

    }

    /**
     * Constructor creating a new sudoku solver.
     *
     * @param sudoku The sudoku to solve.
     */
    public SudokuSolver(Sudoku sudoku) {
        int[][] grid = sudoku.getSudoku();
        this.grid = new OptionField[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                this.grid[i][j] = new OptionField(grid[i][j]);
    }

    /**
     * Getter for a column of the sudoku.
     *
     * @param index Which column to take.
     * @return The column of the sudoku.
     */
    public OptionField[] getColumn(int index) {
        testindexvalidity(index);
        OptionField[] result = new OptionField[9];
        for (int i = 0; i < 9; i++)
            result[i] = (OptionField) this.grid[i][index - 1];
        return result;
    }

    /**
     * Getter for a line of the sudoku.
     *
     * @param index Which line to take.
     * @return The line of the sudoku.
     */
    public OptionField[] getLine(int index) {
        testindexvalidity(index);
        return (OptionField[]) this.grid[index - 1];
    }

    /**
     * Getter for a square of the sudoku.
     *
     * @param index Which square to take.
     * @return The square of the sudoku.
     */
    public OptionField[] getSquare(int index) {
        testindexvalidity(index);
        OptionField[] result = new OptionField[9];
        int i = 0;
        int a = (((index - 1) / 3) + 1) * 3 - 2;                      //Both functions were found by poking around
        int b = (index % 3 == 0 ? 3 : index % 3) * 3 - 2;
        for (int j = -1; j <= 1; j++)
            for (int k = -1; k <= 1; k++)
                result[i++] = (OptionField) this.grid[a + j][b + k];
        return result;
    }

    /**
     * Tests an index for validity.
     *
     * @param index The index to check.
     * @throws IllegalArgumentException Happens when the index is invalid.
     */
    private static void testindexvalidity(int index) throws IllegalArgumentException {
        if (index < 1 || index > 9)
            throw new IllegalArgumentException("Index has to be between 1 and 9, inclusive, was: " + index);
    }

    /**
     * Solve a sudoku.
     *
     * @return -1 if wrong, 0 if completely solved, the number of open fields if incompletely solved.
     */
    public int solve() {
        int prevopen = 81;
        while (this.getStatus() < prevopen) {
            prevopen = this.getStatus();
            for (int i = 1; i <= 9; i++) {
                OptionField[] square = this.getSquare(i);
                OptionField[] column = this.getColumn(i);
                OptionField[] line = this.getLine(i);
                this.setalloptions();
                solvevector(square);
                this.setalloptions();
                solvevector(column);
                this.setalloptions();
                solvevector(line);
            }
        }
        return prevopen;
    }

    /**
     * Sets all options in the sudoku.
     */
    private void setalloptions() {
        for (int i = 1; i <= 9; i++) {
            setoptions(this.getLine(i));
            setoptions(this.getColumn(i));
            setoptions(this.getSquare(i));
        }
    }

    /**
     * Sets all options in a vector.
     *
     * @param vector The vector whose options are to be set.
     */
    private static void setoptions(OptionField[] vector) {
        setoptions(vector, trim(vector));
    }

    /**
     * Sets all options in a vector.
     *
     * @param vector  The vector whose options are to be set.
     * @param counter The values to remove from the vector.
     */
    private static void setoptions(OptionField[] vector, List<Integer> counter) {
        List<Integer> nvals = new ArrayList<>(9);
        for (OptionField field : vector)
            for (int value : counter) {
                int result = field.removeoption(value);
                if (result != 0)
                    nvals.add(result);
            }
        if (nvals.size() > 0)
            setoptions(vector, nvals);
    }

    /**
     * Removes all unfilled fields from a vector.
     *
     * @param vector The vector to trim.
     * @return A trimmed vector.
     */
    private static List<Integer> trim(Field[] vector) {
        List<Integer> result = new ArrayList<>(9);
        for (Field field : vector)
            if (!field.isEmpty())
                result.add(field.getValue());
        return result;
    }

    /**
     * Solves a vector.
     *
     * @param vector The vector to solve.
     */
    private static void solvevector(OptionField[] vector) {
        for (OptionField field : vector)
            if (!field.setonoption())
                for (Integer option : field.getOptions()) {
                    int count = 0;
                    for (int i = 0; i < 9 && count <= 0; i++)
                        if (vector[i] != field && vector[i].hasOption(option))
                            count++;
                    if (count == 0) {
                        field.setValue(option);
                        setoptions(vector);
                    }
                }
            else
                setoptions(vector);
    }

    /**
     * Counts the number of all options in the sudoku.
     *
     * @return The amount of options in the sudoku.
     */
    public int countoptions() {
        int options = 0;
        OptionField[][] grid = (OptionField[][]) this.grid;
        for (OptionField[] vector : grid)
            for (OptionField field : vector)
                options += field.getOptions().size();
        return options;
    }

    /**
     * Determines the status of the sudoku.
     *
     * @return -1 if invalid, 0 if complete, the number of open fields if incomplete but valid.
     */
    public int getStatus() {
        int result = 0;
        for (int i = 1; i <= 9; i++) {
            int square = isValid(this.getSquare(i));
            int column = isValid(this.getColumn(i));
            int line = isValid(this.getLine(i));
            if (square == -1 || column == -1 || line == -1)
                return -1;
            else
                result += square + column + line;
        }
        return result / 3;    //Each empty field is counted multiple times by each vector test.
    }

    /**
     * Determine the validity of a vector.
     *
     * @param vector The vector to test.
     * @return -1 if the vector is invalid, 0 if it is valid and complete, the number of open fields if it is valid and incomplete.
     */
    private static int isValid(Field[] vector) {
        int result = 0;                          //No sum==45 and product==362880 test
        for (Field field : vector)               //It does not cover incomplete vectors
            if (field.isEmpty())                 //And the combination 1-2-4-4-4-5-7-9-9 completes it as well.
                result++;
            else
                for (Field counter : vector)
                    if (field != counter && field.getValue() == counter.getValue())
                        return -1;
        return result;
    }
}
