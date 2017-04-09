package com.hackx.algorthm;

public class Main {

    public static int START = 2;
    public static int END = 10;

    public static void main(String[] args) {

        System.out.println(decision());

    }

    public static boolean decision() {
        try {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }
}
