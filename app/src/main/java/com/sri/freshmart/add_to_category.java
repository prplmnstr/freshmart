package com.sri.freshmart;

public class add_to_category {

    String icon,id,mrp,name,price,unit;

    public add_to_category(String icon, String id, String mrp, String name, String price, String unit) {
        this.icon = icon;
        this.id = id;
        this.mrp = mrp;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public add_to_category() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
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
}
