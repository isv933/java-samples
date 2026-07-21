package org.isv.samples.algorithms;

public class SearchIn2DMatrix {
    public static  class Solution {
        public static boolean searchMatrix(int[][] matrix, int target){
            if (matrix.length==0 || matrix[0].length==0) {
                return false;
            }

            var row = findTargetRow(matrix, 0,
                            matrix.length-1,matrix[0].length-1, target);

            return row != -1 && binSearch(matrix[row], 0, matrix[row].length - 1, target);
        }

        private static int findTargetRow(int [][] matrix, int rowMin, int rowMax, int col, int target) {
            if (rowMin>rowMax && rowMin>=0) {
                return -1;
            }

            var mid = (rowMin + rowMax)/2;

            if (matrix[mid][col] == target) {
                return mid;
            }

            if (matrix[mid][col] > target) {
                var lessThen = findTargetRow(matrix, rowMin, mid-1, col, target);
                return lessThen == -1? mid : lessThen;
            }
            return findTargetRow(matrix, mid+1, rowMax, col, target);
        }

        private static boolean binSearch(int[] row, int colMin, int colMax, int target) {
            if (colMin<0 || colMin> colMax) {
                return false;
            }

            var mid = (colMax+colMin)/2;

            if (row[mid] == target) {
                return true;
            }

            if (row[mid] > target){
                return binSearch(row, colMin, mid-1, target);
            }

            return binSearch(row, mid+1, colMax, target);
         }
    }
    public static void test(){
        System.out.println(Solution.searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}},3));
        System.out.println(Solution.searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}},13));
        System.out.println(Solution.searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}},16));
        System.out.println(Solution.searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}},60));
    }

}
