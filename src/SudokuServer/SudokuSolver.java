package SudokuServer;

import SudokuCore.Sudoku;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class SudokuSolver extends Sudoku {
    private static final List<Integer> all = Arrays.<Integer>asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    public static final int INVALID = -1;

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
            this.options = value == 0 ? new CopyOnWriteArrayList<>(all) : Collections.emptyList();
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
            this.options = Collections.<Integer>emptyList();
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
            return this.setonoption() ? this.value : 0;
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
                this.value = this.options.get(0);
                this.options = Collections.<Integer>emptyList();
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
     * @return The requested column of the sudoku.
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
     * @return The requested line of the sudoku.
     */
    public OptionField[] getLine(int index) {
        testindexvalidity(index);
        return (OptionField[]) this.grid[index - 1];
    }

    /**
     * Getter for a house of the sudoku.
     *
     * @param index Which house to take.
     * @return The requested house of the sudoku.
     */
    public OptionField[] getHouse(int index) {
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
     * Getter for a house of the sudoku.
     *
     * @param x The x-coordinate of the house.
     * @param y The y-coordinate of the house.
     * @return The requested house of the sudoku.
     */
    public OptionField[] getHouse(int x, int y) {
        testindexvalidity(x);
        testindexvalidity(y);
        x = (x + 2) / 3;
        y = (y + 2) / 3;
        return this.getHouse((x - 1) * 3 + y);
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
     * Solves the sudoku with backtracking.
     *
     * @return 0 if solved, -1 if there is no solution
     */
    public int solve() {
        if (this.getStatus() < 0)
            return -1;
        Stack<SudokuSolver> stack = new Stack<>();
        stack.push(this);
        while (!stack.isEmpty()) {
            SudokuSolver current = stack.pop();
            int status = current.solvelogically();
            if (status == 0) {
                this.grid = current.grid;
                return 0;
            } else if (status > 0) {
                int min = 10, minx = -1, miny = -1;
                for (int i = 0; i < 9; i++)
                    for (int j = 0; j < 9; j++)
                        if (((OptionField) current.grid[i][j]).getOptions().size() < min && !((OptionField) current.grid[i][j]).getOptions().isEmpty()) {
                            min = ((OptionField) current.grid[i][j]).getOptions().size();
                            minx = i;
                            miny = j;
                        }
                if (minx != -1)
                    for (int i : ((OptionField) current.grid[minx][miny]).getOptions()) {
                        SudokuSolver newsolver = new SudokuSolver(current);
                        newsolver.grid[minx][miny].setValue(i);
                        stack.push(newsolver);
                    }
            }
        }
        return -1;
    }

    /**
     * Solve a sudoku.
     *
     * @return -1 if wrong, 0 if completely solved, the number of open fields if incompletely solved.
     */
    public int solvelogically() {
        int prevopen = 81;
        while (this.getStatus() < prevopen && this.getStatus() != INVALID) {
            prevopen = this.getStatus();
            this.solvesimple();
            if (this.getStatus() > 0)
                this.pointingpair();
        }
        return prevopen;
    }

    /**
     * Solve a sudoku with non-advanced techniques.
     *
     * @return -1 if wrong, 0 if completely solved, the number of open fields if incompletely solved.
     */
    public int solvesimple() {
        int prevopen = 81;
        while (this.getStatus() < prevopen && this.getStatus() != INVALID) {
            prevopen = this.getStatus();
            for (int i = 1; i <= 9; i++) {
                OptionField[] house = this.getHouse(i);
                OptionField[] column = this.getColumn(i);
                OptionField[] line = this.getLine(i);
                this.setalloptions();
                solvevector(house);
                this.setalloptions();
                solvevector(column);
                this.setalloptions();
                solvevector(line);
                this.setalloptions();
            }
        }
        return prevopen;
    }

    /**
     * Sets all options in the sudoku.
     */
    private void setalloptions() {
        int state = this.getStatus();
        for (int i = 1; i <= 9; i++) {
            setoptions(this.getLine(i));
            setoptions(this.getColumn(i));
            setoptions(this.getHouse(i));
        }
        if (state > this.getStatus())  //We gained significant new information
            this.setalloptions(); //Set again
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
        List<Integer> newvalues = new ArrayList<>(9);
        for (OptionField field : vector)
            for (int value : counter) {
                int result = field.removeoption(value);
                if (result != 0)
                    newvalues.add(result);
            }
        if (newvalues.size() > 0)
            setoptions(vector, newvalues);
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
     * Resolves all pointing pairs for a line or a column.
     *
     * @param i          The identifier of the line or column.
     * @param linecolumn true if the function is used for a line, false if the function is used for a column.
     */
    private void pointingpair(int i, boolean linecolumn) {
        OptionField[] vector;
        if (linecolumn)
            vector = this.getLine(i + 1);
        else
            vector = this.getColumn(i + 1);
        for (int j = 0; j < 8; j++) {
            OptionField field = vector[j];
            for (int option1 : field.getOptions())
                for (int k = j + 1; k < 9; k++) {
                    OptionField partner = vector[k];
                    if (j / 3 == k / 3 && field != partner && partner.hasOption(option1))
                        for (int option2 : field.getOptions())
                            if (option1 != option2 && partner.hasOption(option2)) {
                                boolean onlypair = true;
                                for (OptionField element : vector)
                                    onlypair &= (!element.hasOption(option1) && !element.hasOption(option2)) || element == field || element == partner;
                                if (onlypair) {
                                    for (OptionField element : linecolumn ? this.getHouse(i + 1, j + 1) : this.getHouse(j + 1, i + 1))
                                        if (element != field && element != partner) {
                                            element.removeoption(option1);
                                            element.removeoption(option2);
                                        }
                                    field.getOptions().stream().filter(element -> element != option1 && element != option2).forEach(field::removeoption);
                                    partner.getOptions().stream().filter(element -> element != option1 && element != option2).forEach(partner::removeoption);
                                    this.solvesimple();
                                }
                            }
                }
        }
    }

    /**
     * Pointing pair resolution for all lines and colums.
     */
    public void pointingpair() {
        for (int i = 0; i < 9; i++) {
            this.pointingpair(i, true);
            this.pointingpair(i, false);
        }
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
            int house = isValid(this.getHouse(i));
            int column = isValid(this.getColumn(i));
            int line = isValid(this.getLine(i));
            if (house == INVALID || column == INVALID || line == INVALID)
                return INVALID;
            else
                result += house + column + line;
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
                        return INVALID;
        return result;
    }

    /**
     * This method creates a string representing the sudoku and showing the options for open fields.
     * If a field is filled, then its value surrounded by ·-symbols.
     *
     * @return A string representing the sudoku and showing the options for open fields.
     */
    public String toStringWithOptions() {
        StringBuilder sb = new StringBuilder();
        String strongbar = " = = = = = = = = = = = = = = = = = = = = = = = = = = = \n";
        String weakbar = "‖ - - - - - - - - - - - - - - - - - - - - - - - - - - ‖\n";
        sb.append(strongbar);
        for (int i = 0; i < 9; i++) {
            sb.append("‖ ");
            for (int l = 0; l < 3; l++) {
                for (int j = 0; j < 9; j++) {
                    for (int c = 1; c <= 3; c++)
                        sb.append(fieldtochar((OptionField) this.grid[i][j], c + l * 3));
                    sb.append((j + 1) % 3 != 0 ? " | " : " ‖ ");
                }
                sb.append(l == 2 ? '\n' : "\n‖ ");
            }
            sb.append((i + 1) % 3 == 0 ? strongbar : weakbar);
        }
        return sb.toString();
    }

    /**
     * This method finds the char that is appropriate for a field in toStringWithOptions.
     *
     * @param field  The field to convert.
     * @param option The value against which the field is checked.
     * @return The appropriate char for the field.
     */
    private static char fieldtochar(OptionField field, int option) {
        if (field.hasOption(option))
            return (char) ('0' + option);
        else if (field.getValue() != 0)
            if (option != 5)
                return '·';
            else
                return (char) ('0' + field.getValue());
        else
            return ' ';
    }
}
