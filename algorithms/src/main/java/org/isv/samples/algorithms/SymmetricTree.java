//Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).
//

package org.isv.samples.algorithms;

public class SymmetricTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    static class Solution {
        public static boolean isSymmetric(TreeNode root) {
            return root == null || isEqual(root.left, root.right);
        }
        private static  boolean isEqual(TreeNode left, TreeNode right) {
            if (left == null && right==null) {
                return true;
            }

            if (left ==null || right==null) {
                return false;
            }

            if (left.val!=right.val) {
                return false;
            }

            return  isEqual(left.left, right.right) && isEqual(left.right, right.left);
        }
    }

}
