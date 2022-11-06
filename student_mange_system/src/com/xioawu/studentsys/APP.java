package com.xioawu.studentsys;

import java.util.ArrayList;
import java.util.Scanner;

//这是一个测试
public class APP {
    public static void main(String[] args) {
        ArrayList<User> list = new ArrayList<User>();
        System.out.println("欢迎来到学生管理系统");
        Scanner sc = new Scanner(System.in);
        loop:while (true) {
            System.out.println("请输操作：1.登录 2.注册 3.忘记密码 4.退出");
            String choose =sc.next();

            switch (choose){
                case "1" -> login(list);
                case "2" -> regeister(list);
                case "3" -> forgetPassword(list);
                case "4" -> {
                    System.out.println("谢谢使用，再见");
                    break loop;
                }
                default -> System.out.println("没有这个选项");
            }
        }


    }

    private static void forgetPassword(ArrayList<User> list) {
        System.out.println("忘记密码");
    }

    private static void regeister(ArrayList<User> list) {
        System.out.println("注册");
        //
    }

    private static void login(ArrayList<User> list) {
        System.out.println("登录");
    }
}
