package org.isv.samples.algorithms;

public class RangeSumQuery2D {
    static class NumMatrix {
        private final int [][] prefix;
        public NumMatrix(int[][] matrix) {
            this.prefix = new int[matrix.length][];
            for(var row = 0 ; row < matrix.length; row++) {
                this.prefix[row] = new int[matrix[row].length];
                for(var col = 0 ; col < matrix[row].length; col++) {
                    this.prefix[row][col] = col ==0 ? matrix[row][col] :  matrix[row][col] + this.prefix[row][col-1];
                }
            }
        }
        public  int sumRegion(int row1, int col1, int row2, int col2) {
            var result = 0;

            for(var row = row1; row <=row2; row++) {
                result+= col1 == 0? this.prefix[row][col2] : this.prefix[row][col2] - this.prefix[row][col1-1];
            }
            return result;
        }
    }

    public static void test(){
        var matrix = new NumMatrix(new int[][]{{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1}, {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}});
        System.out.println(matrix.sumRegion(2, 1, 4, 3));
        System.out.println(matrix.sumRegion(1, 1, 2, 2));
        System.out.println(matrix.sumRegion(1, 2, 2, 4));


    }

}
