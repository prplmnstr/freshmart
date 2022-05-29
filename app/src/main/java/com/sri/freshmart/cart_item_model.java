package com.sri.freshmart;

public class cart_item_model {

    String icon1,name_cart,price_cart,mrp_cart,unit_cart,unit_number_cart,cart_id;

    public cart_item_model(String icon1, String name_cart, String price_cart, String mrp_cart, String unit_cart, String unit_number_cart , String cart_id) {
        this.icon1 = icon1;
        this.name_cart = name_cart;
        this.price_cart = price_cart;
        this.mrp_cart = mrp_cart;
        this.unit_cart = unit_cart;
        this.unit_number_cart = unit_number_cart;
        this.cart_id = cart_id;

    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getName_cart() {
        return name_cart;
    }

    public void setName_cart(String name_cart) {
        this.name_cart = name_cart;
    }

    public String getPrice_cart() {
        return price_cart;
    }

    public void setPrice_cart(String price_cart) {
        this.price_cart = price_cart;
    }

    public String getMrp_cart() {
        return mrp_cart;
    }

    public void setMrp_cart(String mrp_cart) {
        this.mrp_cart = mrp_cart;
    }

    public String getUnit_cart() {
        return unit_cart;
    }

    public void setUnit_cart(String unit_cart) {
        this.unit_cart = unit_cart;
    }

    public String getUnit_number_cart() {
        return unit_number_cart;
    }

    public void setUnit_number_cart(String unit_number_cart) {
        this.unit_number_cart = unit_number_cart;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }


}
