import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 */
public class Abrakadabra {
    private class Pair implements Comparable<Pair> {
        int a;
        int b;

        private Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int compareTo(Pair o) {
            return equals(o) ? 0 : a - o.a == 0 ? b - o.b : a - o.a;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return ((pair.a == a && pair.b == b) || (pair.a == b && pair.b == a));
        }

        @Override
        public int hashCode() {
            return (a * b) - (a + b);
        }

        @Override
        public String toString() {
            return "(" + a + " | " + b + ")";
        }
    }

    public int taxicab(int n) {
        Set<Pair> set = new TreeSet<>();
        int i;
        for (i = 1; set.size() < n; i++) {
            set.clear();
            for (int a = 1; a < i; a++)
                for (int b = 1; b + a <= i; b++)
                    innerloop:{
                        int value = a * a * a + b * b * b;
                        if (value == i)
                            set.add(new Pair(a, b));
                        if (value >= i)
                            break innerloop;
                    }
            if (i % 100 == 0)
                System.out.println(i);
        }
        System.out.println((i - 1) + " Set " + set);
        return i - 1;
    }

    private static int[][] testsudoku = {
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

    public static void main(String[] args) throws Exception {
//        System.out.println(new Sudoku(easysudoku));
//        System.out.println(new Sudoku(testsudoku));
//        System.out.println(new Sudoku(easysudoku).toString());
//        long t = System.currentTimeMillis();
//        System.out.println(s.solve());
//        System.out.println(SudokuService.SudokuCore.SudokuCore.solveguessing(s));
//        System.out.println(System.currentTimeMillis() - t);

//        System.out.println(s.toString());
//        System.out.println(s.countoptions());
//        System.out.println(s.countoptions());
//        System.out.println(s.solve());
//        System.out.println(s.countoptions());
//        System.out.println(s.solve());
//        System.out.println(s.toString());
//        Abrakadabra x = new Abrakadabra();
//        System.out.println("Taxicab 1 ist " + x.taxicab(1));
//        System.out.println("Taxicab 2 ist " + x.taxicab(2));
//        System.out.println("Taxicab 3 ist " + x.taxicab(3));
    }
}
