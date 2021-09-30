package com.simbazet.mbizvo.fragments;

public class SurburbsData {
    private String surburb;
    private int total;

    public SurburbsData(String surburb, int total) {
        this.surburb = surburb;
        this.total = total;
    }

    public String getSurburb() {
        return surburb;
    }

    public int getTotal() {
        return total;
    }
}
