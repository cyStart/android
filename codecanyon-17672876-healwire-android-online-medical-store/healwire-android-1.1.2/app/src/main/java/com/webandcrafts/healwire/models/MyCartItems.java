package com.webandcrafts.healwire.models;

import java.io.Serializable;

/**
 * Created by vineeth on 8/4/2016.
 */

public class MyCartItems implements Serializable {
    private String medName,qty;
    private int rowid;
    private String flag;
    private int is_pres_required;
    private int id;


    public MyCartItems() {

    }



    public MyCartItems(int id,String medName,String qty,int is_pres_required) {

        this.medName = medName;
        this.id = id;
        this.qty = qty;
        this.is_pres_required = is_pres_required;
    }

    public MyCartItems(String medName,String qty) {

        this.medName = medName;
        this.qty = qty;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyCartItems(String medName) {

        this.medName = medName;

    }


    public int getRowID() {
        return rowid;
    }
    public String getMedName() {
        return medName;
    }
    public String getQty() {
        return qty;
    }
    public String getFlag(){

        return flag;
    }

    public int getIs_pres_required() {
        return is_pres_required;
    }

    public void setIs_pres_required(int is_pres_required) {
        this.is_pres_required = is_pres_required;
    }

    public void setRowId(int rowid) {
        this.rowid = rowid;
    }
    public void setMedName(String medName) {
        this.medName = medName;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
    public void setFlag(String flag){
        this.flag = flag;

    }



}
