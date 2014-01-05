package SudokuCore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
@XmlRootElement
public class Sudoku {
    protected class Field {
        protected int value;

        /**
         * Constructor creating an empty field with no value.
         */
        public Field() {
            this.value = 0;
        }

        /**
         * Constructor creating a new field with a given value.
         *
         * @param value The value of the new field. If it is 0, the field has no value.
         * @throws IllegalArgumentException Happens when the value is invalid.
         */
        public Field(int value) throws IllegalArgumentException {
            if (value < 0 || value > 9)
                throw new IllegalArgumentException("A field has a value between 0 and 9, inclusive, was: " + value);
            else
                this.value = value;
        }

        /**
         * Getter for the value of the field.
         *
         * @return The value of the field, 0 if there is no value.
         */
        public int getValue() {
            return this.value;
        }

        /**
         * Sets the value of the field.
         *
         * @param value The new value of the field.
         * @throws IllegalArgumentException Happens when the value is invalid.
         */
        public void setValue(int value) throws IllegalArgumentException {
            if (value == 0)
                throw new IllegalArgumentException("Value was 0, a field cannot be set to have no value.");
            this.value = value;
        }

        /**
         * Determines whether the field has no value.
         *
         * @return Whether the field is empty.
         */
        public boolean isEmpty() {
            return this.value == 0;
        }

        /**
         * A string representation of the field, a space if the field is empty.
         *
         * @return A string representation of the field.
         */
        @Override
        public String toString() {
            return this.isEmpty() ? " " : Integer.toString(this.value);
        }
    }

    protected Field[][] grid;

    /**
     * Constructor creating an empty sudoku.
     */
    public Sudoku() {
        this.grid = new Field[9][9];
    }

    /**
     * Constructor creating a new sudoku with a given grid.
     *
     * @param grid The grid of the values for the new sudoku.
     * @throws IllegalArgumentException Happens when the grid has an invalid size or contains invalid values.
     * @throws NullPointerException     Happens when the grid or a line are null.
     */
    public Sudoku(int[][] grid) {
        this();
        this.setSudoku(grid);
    }

    /**
     * Getter of the whole sudoku for JAXB.
     *
     * @return The complete sudoku without options.
     */
    @XmlElement(name = "line")
    public int[][] getSudoku() {
        int[][] result = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                result[i][j] = this.grid[i][j].getValue();
        return result;
    }

    /**
     * Setter for the whole sudoku for JAXB. Options are not set automatically.
     *
     * @param grid The new sudoku.
     */
    public void setSudoku(int[][] grid) {
        if (grid == null)
            throw new NullPointerException("The grid was null.");
        if (grid.length != 9)
            throw new IllegalArgumentException("Grid has invalid size.");
        for (int[] line : grid)
            if (line == null)
                throw new NullPointerException("A line was null.");
            else if (line.length != 9)
                throw new IllegalArgumentException("Grid has invalid size.");

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                this.grid[i][j] = new Field(grid[i][j]);
    }

    /**
     * Converts a vector of fields to a vector of integers.
     *
     * @param vector The vector to convert.
     * @return The converted vector.
     */
    public static int[] fieldtoint(Field[] vector) {
        int[] result = new int[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = vector[i].getValue();
        return result;
    }

    /**
     * A string representation of the sudoku distributed over multiple lines.
     *
     * @return A string representing the sudoku.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String bar = " - - - - - - - - - - - -\n";
        sb.append(bar);
        for (int i = 0; i < 9; i++) {
            sb.append("| " + this.grid[i][0] + " " + this.grid[i][1] + " " + this.grid[i][2] + " | ");
            sb.append(this.grid[i][3] + " " + this.grid[i][4] + " " + this.grid[i][5] + " | ");
            sb.append(this.grid[i][6] + " " + this.grid[i][7] + " " + this.grid[i][8] + " |\n");
            if ((i + 1) % 3 == 0)
                sb.append(bar);
        }
        return sb.toString();
    }
}