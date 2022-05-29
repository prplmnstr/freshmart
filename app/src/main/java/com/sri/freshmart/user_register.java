package com.sri.freshmart;

public class user_register {
    String name, mobile, homeid, adress;

    public user_register() {

    }

    public user_register(String name, String mobile, String homeid, String adress) {
        this.name = name;
        this.mobile = mobile;
        this.homeid = homeid;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomeid() {
        return homeid;
    }

    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}



