package com.hackx.algorthm;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<List<Integer>> res = combine(5, 3);
        for (List<Integer> list : res) {
//            System.out.println(list.toString());
        }
    }

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> combs = new ArrayList<List<Integer>>();
        helper(combs, new ArrayList<Integer>(), 1, n, k);
        return combs;
    }

    public static void helper(List<List<Integer>> combs, List<Integer> comb, int start, int n, int k) {
        if (k == 0) {
            System.out.println(comb);
            combs.add(new ArrayList<Integer>(comb));
            return;
        }
        for (int i = start; i <= n; i++) {
            comb.add(i);
            helper(combs, comb, i + 1, n, k - 1);
            comb.remove(comb.size() - 1);
        }
    }


//    public static List<String> generateParenthesis(int n) {
//        List<String> result = new LinkedList<String>();
//        if (n > 0) generateParenthesisCore("", n, n, result);
//        return result;
//    }
//
//    public static void generateParenthesisCore(String prefix, int left, int right, List<String> result) {
//        if (left == 0 && right == 0) {
//            result.add(prefix);
//        }
//        // Has left Parenthesis
//        if (left > 0) {
//            generateParenthesisCore(prefix + '(', left - 1, right, result);
//        }
//        // has more right Parenthesis
//        if (left < right) {
//            generateParenthesisCore(prefix + ')', left, right - 1, result);
//        }
//    }
}
