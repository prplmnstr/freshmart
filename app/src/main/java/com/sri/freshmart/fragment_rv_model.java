package com.sri.freshmart;

import java.util.ArrayList;

public class fragment_rv_model {
    String price,mrp,icon,name_,unit,item_id;
    private ArrayList<String> tags;

    public fragment_rv_model(String price, String mrp, String icon, String name_, String unit, String item_id) {
        this.price = price;
        this.mrp = mrp;
        this.icon = icon;
        this.name_ = name_;
        this.unit = unit;
        this.item_id = item_id;

    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }


}
