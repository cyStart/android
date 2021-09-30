package com.booking.nirbhay.testapp2;

/**
 * Created by lannet on 5/2/2017.
 */

public class TourPackageModel {
    String bus_id;
    String bus_name;
    String bus_number;
    String totalSeats;
    String route_id;
    String bus_source;
    String bus_dest;
    String bus_pickup;
    String source_time;
    String des_time;

    public TourPackageModel(String bus_idT, String bus_nameT, String bus_numberT, String totalSeatsT,
                            String route_idT, String bus_sourceT, String bus_destT, String bus_pickupT, String source_timeT, String des_timeT)
    {
        bus_id=bus_idT;
        bus_name=bus_nameT;
        bus_number=bus_numberT;
        totalSeats=totalSeatsT;
        route_id=route_idT;
        bus_source=bus_sourceT;
        bus_dest=bus_destT;
        bus_pickup=bus_pickupT;
        source_time=source_timeT;
        des_time=des_timeT;


    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public String getBus_number() {
        return bus_number;
    }

    public void setBus_number(String bus_number) {
        this.bus_number = bus_number;
    }

    public String getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(String totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getBus_source() {
        return bus_source;
    }

    public void setBus_source(String bus_source) {
        this.bus_source = bus_source;
    }

    public String getBus_dest() {
        return bus_dest;
    }

    public void setBus_dest(String bus_dest) {
        this.bus_dest = bus_dest;
    }

    public String getBus_pickup() {
        return bus_pickup;
    }

    public void setBus_pickup(String bus_pickup) {
        this.bus_pickup = bus_pickup;
    }

    public String getSource_time() {
        return source_time;
    }

    public void setSource_time(String source_time) {
        this.source_time = source_time;
    }

    public String getDes_time() {
        return des_time;
    }

    public void setDes_time(String des_time) {
        this.des_time = des_time;
    }
}
