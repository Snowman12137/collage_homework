package com.xiaowu.studing.test1;
/* 
  @auther : snowman
  @date : 2022年11月21日 20:08
  */

public class GirFriendTest {
    public static void main(String[] args) {
        GirFriend gf1 = new GirFriend();
        gf1.name = "切尔西";
        gf1.age = 18;
        gf1.gender = "萌妹子";

        System.out.println(gf1.name);
        System.out.println(gf1.age);
        System.out.println(gf1.gender);

        gf1.eat();
        gf1.sleep();
    }

}