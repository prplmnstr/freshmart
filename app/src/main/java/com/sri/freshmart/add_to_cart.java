package com.sri.freshmart;

public class add_to_cart {
    String mrp,price,name,icon1,unit_number,unit,id;

    public add_to_cart() {
       //empty required
    }

    public add_to_cart(String mrp, String price, String name, String icon1, String unit_number, String unit,String id) {
        this.mrp = mrp;
        this.price = price;
        this.name = name;
        this.icon1 = icon1;
        this.unit_number = unit_number;
        this.unit = unit;
        this.id = id;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getUnit_number() {
        return unit_number;
    }

    public void setUnit_number(String unit_number) {
        this.unit_number = unit_number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
