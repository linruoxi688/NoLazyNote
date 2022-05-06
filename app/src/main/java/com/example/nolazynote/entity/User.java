package com.example.nolazynote.entity;

public class User {
    private String nickName;
    private String autograph;
    private String sex;
    private Integer total;

    public User(){}

    public String getSex(){return sex;}
    public String getNickName(){
        return nickName;
    }
    public String getAutograph(){return autograph;}
    public Integer getTotal(){
        return total;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public void setAutograph(String autograph){this.autograph = autograph;}
    public void setSex(String sex){this.sex = sex;}
    public void setTotal(Integer total){
        this.total = total;
    }
}
