package com.xiaowu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class demo1_test {
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");

        list.remove(2);
        System.out.println(list);


    }
}
