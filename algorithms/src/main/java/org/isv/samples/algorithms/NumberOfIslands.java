//Number of Islands (BFS/DFS)
//
//        Задача:
//        Дана бинарная матрица (сетка) размером m x n, представляющая карту, где '1' означает сушу, а '0' — воду.
//        Необходимо подсчитать количество островов.
//        Остров — это группа '1' (суши), окруженная водой, и формируется путем горизонтального или вертикального соединения соседних участков суши.
//        Можно считать, что все четыре края сетки окружены водой.
//
//        Пример:
//
//        Вход: grid = [
//        ["1","1","1","1","0"],
//        ["1","1","0","1","0"],
//        ["1","1","0","0","0"],
//        ["0","0","0","0","0"]
//        ]
//        Выход: 1


package org.isv.samples.algorithms;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;

public class NumberOfIslands {
    public static int getNumberOfIslands(int[][] input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        var visited = new HashSet<Integer>();
        var stack = new ArrayDeque<Integer>();
        var numIslands = 0;
        var numCols = input.length > 0 ? input[0].length : 0;

        for (var i = 0; i < input.length; i++) {
            for (var j = 0; j < input[i].length; j++) {
                if (input[i].length != numCols) {
                    throw new IllegalArgumentException(String.format("Invalid number of cols in row %d", i));
                }
                var currentIndex = i * numCols + j;

                if (input[i][j] == 1 && !visited.contains(currentIndex)) {
                    numIslands++;
                    stack.add(currentIndex);
                    visited.add(currentIndex);

                    while (!stack.isEmpty()) {
                        var islandIndex = stack.pollFirst();
                        addNeighbors(input, numCols, islandIndex, stack, visited);
                    }
                }
            }
        }
        return numIslands;
    }

    private static void addNeighbors(int[][] input, int numCols, int islandIndex, Deque<Integer> stack, HashSet<Integer> visited) {
        var offsets = List.of(new int[]{0, 1}, new int[]{0, -1}, new int[]{1, 0}, new int[]{-1, 0});
        for (var offset : offsets) {
            var i = islandIndex / numCols + offset[0];
            var j = islandIndex % numCols + offset[1];
            var neighBoardIndex = i * numCols + j;
            if (isValidInput(input, i, j) && input[i][j] == 1 && !visited.contains(neighBoardIndex)) {
                stack.add(neighBoardIndex);
                visited.add(neighBoardIndex);
            }
        }
    }

    private static boolean isValidInput(int[][] input, int i, int j) {
        return (i >= 0 && j >= 0 && i < input.length && j < input[i].length);
    }

    public static void test() {
        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{

        }), 0);


        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{
                {1, 0, 1},
                {1, 1, 1},
        }), 1);

        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{
                {1, 1},
                {1, 1},
        }), 1);

        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{
                {1, 1, 0, 0, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 1}
        }), 3);

        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{
                {1, 1, 1, 1, 0},
                {1, 1, 0, 1, 0},
                {1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0}
        }), 1);


        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        }), 0);

        System.out.printf("NumberOfIslands: %d == %d\n", getNumberOfIslands(new int[][]{
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        }), 5);

    }


}
