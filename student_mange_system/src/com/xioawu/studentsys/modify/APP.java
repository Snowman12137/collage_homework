package com.xioawu.studentsys.modify;

import com.xioawu.studentsys.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class APP {
    public static void main(String[] args) {
        ArrayList<User> list = new ArrayList<User>();
        chuShiHua(list);
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

    private static void chuShiHua(ArrayList<User> list) {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        User user5 = new User();
        user1.setUsername("zhangsan");user1.setPassword("123456");user1.setPersonID("123456789012345678");user1.setPhtoneNumber("12345678901");
        user2.setUsername("lisi");user1.setPassword("123456");user1.setPersonID("123456789012345678");user1.setPhtoneNumber("12345678901");
        user3.setUsername("wangwu");user1.setPassword("123456");user1.setPersonID("123456789012345678");user1.setPhtoneNumber("12345678901");
        user4.setUsername("laoliu");user1.setPassword("123456");user1.setPersonID("123456789012345678");user1.setPhtoneNumber("12345678901");
        user5.setUsername("xiaoqi");user1.setPassword("123456");user1.setPersonID("123456789012345678");user1.setPhtoneNumber("12345678901");
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);
        list.add(user5);
    }

    private static void forgetPassword(ArrayList<User> list) {
        System.out.println("忘记密码");
    }

    private static void regeister(ArrayList<User> list) {
        System.out.println("注册");
        //把用户信息添加到集合中
        //把用户名，密码，身份证，手机号放到用户对象中
        //把用户对象添加到集合中
        //1.录入用户名
        Scanner sc = new Scanner(System.in);
        String username;
        while (true) {
            System.out.println("请输入用户名");
            username = sc.next();
            //用户名唯一
            //用户名长度必须在3~15位之间
            //只能是字母加数字组合，但不能是纯数字
            boolean flag1 = checkUsername(username);
            if(!flag1){
                System.out.println("不满足条件");
                continue;
            }

            //校验用户名唯一
            //username到集合中判断是否有存在
            boolean flag2 = contains(list,username);
            if(flag2){
                //用户名已存在，那么当前用户名无法注册
                System.out.println("用户名"+username+"已存在，请重新输入");
            }else {
                //不存在，表示当前用户名可用
                System.out.println("注册成功");
                break;
            }


        }

        //2.键盘录入密码
        String password;
        while (true) {
            System.out.println("请输入要注册的密码");
            password = sc.next();
            System.out.println("请再次输入要注册的密码");
            String againPassword = sc.next();
            if (!password.equals(againPassword)) {
                System.out.println("两次密码输入不一致，请重新输入");
                continue;
            }else {
                break;
            }
        }

        //3.键盘录入身份证号
        //长度18位，前17位必须都是数字
        //最后一位可以是数字，也可以是大写X或者小写x
        String personID;
        while (true) {
            System.out.println("请输入身份证号码");
            personID = sc.next();
            boolean flag = checkPersonID(personID);
            if(flag){
                System.out.println("身份证号码满足要求");
                break;
            }else {
                System.out.println("身份证格式有误，请重新输入");
                continue;
            }
        }

        //4.键盘录入手机号码
        //长度为11位
        //不能以0开头
        //必须都是数字
        String phoneNumber;
        while (true) {
            System.out.println("请输入手机号");
            phoneNumber = sc.next();
            boolean flag = checkPhoneNumber(phoneNumber);
            if (flag){
                System.out.println("手机格式正确");
                break;
            }else {
                System.out.println("手机格式有误，请重新输入");
                continue;
            }
        }

        //用户名，密码，身份证号，手机号放到用户对象中
        User u = new User(username,password,personID,phoneNumber);
        list.add(u);
        System.out.println("注册成功");
        printList(list);
    }

    private static void printList(ArrayList<User> list) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            System.out.println(user.getUsername() + "," +user.getPassword()+","+user.getPersonID()+","+user.getPhtoneNumber());
        }
    }

    private static boolean checkPhoneNumber(String phoneNumber) {
        //长度为11位
        if (phoneNumber.length() != 11){
            return false;
        }
        //开头不能是0
        if(phoneNumber.startsWith("0")){
            return false;
        }
        //必须都是数字
        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            if (c < '0' && c> '9') {
                return false;
            }
        }
        return true;
    }

    private static boolean checkPersonID(String personID) {
        //长度18位
        if(personID.length() !=18 ){
            return false;
        }
        //不能是0开头
        boolean b = personID.startsWith("0");
        if (b){
            //如果以0开头，返回FALSE
            return false;
        }
        //前17位必须是数字
        for (int i = 0; i < personID.length() - 1 ; i++) {
            char c = personID.charAt(i);
            //如果有一个字符不在0-9之间，那么直接返回FALSE
            if (c<'0' && c > '9'){
                return false;
            }
        }
        //最后一位可以是数字，也可以是大写X或者小写x
        char endChar = personID.charAt(personID.length()-1);
        if ((endChar >= '0' && endChar <= '9') || (endChar == 'X') || (endChar == 'x')) {
        return true;
        }else {
            return false;
        }

    }

    private static boolean contains(ArrayList<User> list, String username) {
    //遍历用户
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            String rightUsername = user.getUsername();
            if (rightUsername.equals(username)){
                return true;
            }
        }
        //遍历完成没有匹配到证明没有一样的
        return false;

    }

    private static boolean checkUsername(String username) {
        //用户名长度必须在3~15位之间
        int len = username.length();
        if(len <3 || len>15){
            System.out.println("应该是3~15位");
            return false;
        }
        int a = 0;
        //只能是字母加数字的组合
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if(!((c >= 'a' && c<= 'z')||(c >= 'A' && c<= 'Z')||(c >= '0' && c<= '9'))){
                System.out.println("应该是字母加数字");
                return false;
            }

            if ((c >= 'a' && c<= 'z')||(c >= 'A' && c<= 'Z')){
                a++;
            }
        }
        return a > 0;
    }

    private static void login(ArrayList<User> list) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.println("请输入用户名：");
            String username = sc.next();
            //判断用户名是否存在
            boolean flag = contains(list,username);
            if(!flag){
                System.out.println("用户名"+username+"未注册，请先注册");
                return;
            }

            System.out.println("请输入密码：");
            String password = sc.next();

            while (true) {
                String rightCode =getCode();
                System.out.println("当前正确的验证码为："+rightCode);
                System.out.println("请输入与验证码");
                String code = sc.next();
                if(code.equalsIgnoreCase(rightCode)){
                    System.out.println("验证码正确");
                    break;
                }else {
                    System.out.println("验证码错误");
                    continue;
                }
            }

            //验证用户名和密码
            //我们可以把一些零散的数据封装成一个对象
            User userInfo = new User(username,password,null,null);
            boolean result = checkUserInfo(list,userInfo);
            if(result){
                System.out.println("登陆成功");
                break;
            }else {
                System.out.println("登录失败，请重新输入用户名和密码");
                if(i==2){
                    System.out.println("当前账号"+username+"被锁定，请联系客服XXX-XXXXXX");
                    //被锁定以后直接结束方法即可
                    //被锁定后执行代码
                    /*
                    *
                    *
                    *
                    *
                    *
                    * */
                    return;
                }else{
                    System.out.println("用户名或密码错误，还剩下"+ ( 2- i )+"次机会");
                }
            }
        }


    }

    private static boolean checkUserInfo(ArrayList<User> list, User userInfo) {
        //遍历集合
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if(user.getUsername().equals(userInfo.getUsername())&&user.getPassword().equals(userInfo.getPassword())){
                return true;
            }
        }
        return false;
    }

    //生成验证码
    private static String getCode(){
        //1.创建一个集合添加所有的大写和小写字母和数字
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char)('a' + i));
            list.add((char)('A' + i));
        }
        for (int i = 0; i < 10; i++) {
            list.add((char)('0' + i));
        }
        System.out.println(list);
        StringBuilder sb = new StringBuilder();
        //2.随机抽取4个随机字符
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            //获取随机索引
            int index = r.nextInt(list.size());
            //利用是随机索引获取字符
            //5以后Character char
            char c = list.get(index);
            sb.append(c);
        }
        System.out.println(sb);
        return "";
    }

}
