package com.sri.freshmart;

public class catogary_model {

    private String cat_icon_link;
    private String cat_text_name;

    public catogary_model(String cat_icon_link, String cat_text_name) {
        this.cat_icon_link = cat_icon_link;
        this.cat_text_name = cat_text_name;
    }

    public String getCat_icon_link() {
        return cat_icon_link;
    }

    public void setCat_icon_link(String cat_icon_link) {
        this.cat_icon_link = cat_icon_link;
    }

    public String getCat_text_name() {
        return cat_text_name;
    }

    public void setCat_text_name(String cat_text_name) {
        this.cat_text_name = cat_text_name;
    }
}
