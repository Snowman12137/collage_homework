package com.xiaowu;


public class demo1 {
    private String uername;
    private String password;
    private String email;
    private String gender;
    private String age;


    public demo1() {
    }

    public demo1(String uername, String password, String email, String gender, String age) {
        this.uername = uername;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.age = age;
    }

    /**
     * 获取
     *
     * @return uername
     */
    public String getUername() {
        return uername;
    }

    /**
     * 设置
     *
     * @param uername
     */
    public void setUername(String uername) {
        this.uername = uername;
    }

    /**
     * 获取
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     *
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 获取
     *
     * @return age
     */
    public String getAge() {
        return age;
    }

    /**
     * 设置
     *
     * @param age
     */
    public void setAge(String age) {
        this.age = age;
    }


}
