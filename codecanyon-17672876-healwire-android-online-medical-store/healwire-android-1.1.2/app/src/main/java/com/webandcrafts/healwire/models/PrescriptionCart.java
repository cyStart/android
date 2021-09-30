package com.webandcrafts.healwire.models;

/**
 * Created by vineeth on 8/16/2016.
 */
public class PrescriptionCart {

    int item_id,unit_price,discount_percent,discount_cart,quantity,total_price;
    String item_code,item_name,currency,curr_position;


    public String getCurr_position() {
        return curr_position;
    }

    public void setCurr_position(String curr_position) {
        this.curr_position = curr_position;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PrescriptionCart() {
    }

    public PrescriptionCart(String item_name, int quantity, int unit_price, int discount_percent, int discount_cart, int total_price) {
        this.item_name = item_name;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount_percent = discount_percent;
        this.discount_cart = discount_cart;
        this.total_price = total_price;
    }


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public int getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

    public int getDiscount_cart() {
        return discount_cart;
    }

    public void setDiscount_cart(int discount_cart) {
        this.discount_cart = discount_cart;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
