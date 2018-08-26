package com.casa.myapplication.Model;

/**
 * Created by Gastby on 29/05/2018.
 */

public class Petrol {

    private String date, hour, km, liters, truckId, truckNum;

    public Petrol() {
        this.date = date;
        this.hour = hour;
        this.km = km;
        this.liters = liters;
        this.truckId = truckId;
        this.truckNum = truckNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getLiters() {
        return liters;
    }

    public void setLiters(String liters) {
        this.liters = liters;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(String truckNum) {
        this.truckNum = truckNum;
    }
}
