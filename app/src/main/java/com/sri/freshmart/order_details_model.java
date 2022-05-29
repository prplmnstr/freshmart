package com.sri.freshmart;

public class order_details_model {

    String icon, name,price,unit;

    public order_details_model(String icon, String name, String price, String unit) {
        this.icon = icon;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
