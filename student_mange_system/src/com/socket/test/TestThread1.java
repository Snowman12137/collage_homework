package com.socket.test;
/* 
  @auther : snowman
  @date : 2022年11月25日 16:03
  */

//方式二 runnable
public class TestThread1 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("线程代码"+i);
        }
    }

    public static void main(String[] args) {

        TestThread1 testThread1 = new TestThread1();
        new Thread(testThread1).start();

        for (int i = 0; i < 20; i++) {
            System.out.println("主线程"+i);
        }
    }
}

//方式一：Thead
/*
public class TestThread1 extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("线程代码"+i);
        }
    }

    public static void main(String[] args) {
        TestThread1 testThread1 = new TestThread1();

        testThread1.start();

        for (int i = 0; i < 20; i++) {
            System.out.println("主线程"+i);
        }
    }
}*/