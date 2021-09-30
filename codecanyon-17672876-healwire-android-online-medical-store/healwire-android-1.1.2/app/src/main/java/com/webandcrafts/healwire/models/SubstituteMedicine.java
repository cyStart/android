package com.webandcrafts.healwire.models;

/**
 * Created by vineeth on 8/23/2016.
 */
public class SubstituteMedicine {

    private String item_name;
    private String item_price;


    public SubstituteMedicine(String item_name, String item_price) {
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public SubstituteMedicine() {

    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }
}
