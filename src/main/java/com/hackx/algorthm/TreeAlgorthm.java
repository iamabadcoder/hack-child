package com.hackx.algorthm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class TreeAlgorthm {

    public static void main(String[] args) {
        TreeNode head = generateTree();
//        branchFirstSearch(head);
//        deepFirstSearch(head);

        int a = 10000;
        long b = 10000;
        System.out.println(a == b);
    }

    public static TreeNode generateTree() {
        TreeNode head = new TreeNode(1);
        head.left = new TreeNode(2);
        head.right = new TreeNode(3);
        head.left.left = new TreeNode(4);
        head.left.right = new TreeNode(5);
        head.right.right = new TreeNode(6);
        head.left.left.left = new TreeNode(7);
        return head;
    }

    public static void deepFirstSearch(TreeNode head) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(head);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            System.out.println(node.val);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
    }

    public static void branchFirstSearch(TreeNode head) {
        Deque<TreeNode> deque = new ArrayDeque<>();
        deque.addLast(head);
        while (!deque.isEmpty()) {
            TreeNode node = deque.pop();
            System.out.println(node.val);
            if (node.left != null) {
                deque.addLast(node.left);
            }
            if (node.right != null) {
                deque.addLast(node.right);
            }
        }
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            val = x;
        }
    }
}

