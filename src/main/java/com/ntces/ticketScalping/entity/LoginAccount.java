package com.ntces.ticketScalping.entity;

/**
 * 登录账号信息实体类
 */
public class LoginAccount {
    //
    private String code;
    //登录账号
    private String regCode;
    //登录密码
    private String passWord;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
