package com.sri.freshmart;

public class add_to_orders {
    String id,name,price,unit,qty ,icon1,mrp;

    public add_to_orders(String id, String name, String price, String unit, String qty, String icon1, String mrp) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.qty = qty;
        this.icon1 = icon1;
        this.mrp = mrp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
