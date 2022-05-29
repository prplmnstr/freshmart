package com.sri.freshmart;

public class customer_register {

    String name,mobile,home_id,adress,img1,img2;

    public customer_register() {
    }

    public customer_register(String name, String mobile, String home_id, String adress, String img1, String img2) {
        this.name = name;
        this.mobile = mobile;
        this.home_id = home_id;
        this.adress = adress;
        this.img1 = img1;
        this.img2 = img2;
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

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }
}
