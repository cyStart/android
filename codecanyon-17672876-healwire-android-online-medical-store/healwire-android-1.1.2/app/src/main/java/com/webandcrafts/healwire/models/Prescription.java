package com.webandcrafts.healwire.models;

/**
 * Created by vineeth on 8/12/2016.
 */
public class Prescription {

    private String date;
    private String status;
    private String imageUrl;
    private int statusId;
    private int invoice_Id;
    private String invoice_status;





    public Prescription() {
    }

    public Prescription(String date, String status, String imageUrl,String invoice_status) {
        this.date = date;
        this.status = status;
        this.imageUrl = imageUrl;
        this.invoice_status = invoice_status;
    }

    public int getInvoice_Id() {
        return invoice_Id;
    }

    public void setInvoice_Id(int invoice_Id) {
        this.invoice_Id = invoice_Id;
    }

    public String getStatus() {
        return status;
    }
    public String getInvoiceStatus() {
        return invoice_status;
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

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setInvoiceStatus(String invoice_status) {
        this.invoice_status = invoice_status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
