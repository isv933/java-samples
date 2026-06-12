package org.isv.samples.algorithms;
//Binary Search h in Rotated Sorted Array
//Задача:
//Найти индекс элемента в отсортированном, но сдвинутом массиве. Если не найдет - вернуть -1
//
//Пример:
//[4,5,6,7,0,1,2], target = 0
//ответ: 4

public class RotatedBinarySearch {
    public static int findTarget(int[] input, int target) {
        if (input == null || input.length == 0) {
            return -1;
        }
        var pivot = input.length > 1 ? findPivot(input, 0, input.length - 1) : -1;

        if (pivot == -1) {
            return binarySearch(input, 0, input.length - 1, target);
        }

        if (target >= input[pivot] && target < input[0]) {
            return binarySearch(input, pivot, input.length - 1, target);
        }

        return binarySearch(input, 0, pivot - 1, target);
    }

    private static int findPivot(int[] input, int start, int end) {
        if (start < 0 || start > end || start == end) {
            return -1;
        }

        var mid = (start + end) / 2;

        if (input[mid] > input[mid + 1]) {
            return mid + 1;
        }
        if (input[0] > input[mid]) {
            return findPivot(input, start, mid);
        }

        return findPivot(input, mid + 1, end);
    }

    private static int binarySearch(int[] input, int start, int end, int target) {
        if (start > end) {
            return -1;
        }
        var mid = (start + end) / 2;

        if (target == input[mid]) {
            return mid;
        }

        if (target > input[mid]) {
            return binarySearch(input, mid + 1, end, target);

        } else {
            return binarySearch(input, start, mid - 1, target);
        }
    }

    public static void test() {
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{4, 5, 6, 7, 0, 1, 2}, 0), 4);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{4, 5, 6, 7, 0, 1, 2}, 3), -1);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{1, 2, 3, 4, 5}, 6), -1);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{1}, 1), 0);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{1, 3}, 3), 1);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{5, 1, 3}, 3), 2);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{5, 1, 3}, 5), 0);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{3, 5, 1}, 1), 2);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{3, 5, 1}, 3), 0);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{3, 5, 1}, 5), 1);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{3, 4, 5, 1}, 5), 2);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{2, 3, 4, 5, 1}, 5), 3);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{5, 1, 2}, 5), 0);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{5, 1, 2, 3}, 5), 0);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{5, 1, 2, 3, 4}, 5), 0);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{9, 10, 11, 1, 2, 3, 4}, 10), 1);
        System.out.printf("RotatedBinarySearch: %d == %d\n", findTarget(new int[]{9, 10, 11, 1, 2, 3, 4}, 2), 4);
    }

}
