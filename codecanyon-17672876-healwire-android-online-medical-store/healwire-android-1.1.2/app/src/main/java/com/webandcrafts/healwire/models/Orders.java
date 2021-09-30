package com.webandcrafts.healwire.models;

/**
 * Created by vineeth on 8/18/2016.
 */
public class Orders {

    private String date, invoice,status, imageUrl;


    public Orders() {
    }

    public Orders(String date, String invoice, String status, String imageUrl) {
        this.date = date;
        this.invoice = invoice;
        this.status = status;
        this.imageUrl = imageUrl;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
