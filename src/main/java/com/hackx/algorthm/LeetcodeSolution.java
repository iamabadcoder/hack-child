package com.hackx.algorthm;

import java.util.HashMap;
import java.util.Map;

public class LeetcodeSolution {

    public static void main(String[] args) {
        String s = "abcabcbb";
        System.out.println(lengthOfLongestSubstring(s));
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int longestSubstringLen = 0;
        int start = -1;
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int end = 0; end < s.length(); end++) {
            char ch = s.charAt(end);
            if (map.containsKey(ch)) {
                start = Math.max(start, map.get(ch));
            }
            longestSubstringLen = Math.max(longestSubstringLen, end - start);
        }
        return longestSubstringLen;
    }
}
