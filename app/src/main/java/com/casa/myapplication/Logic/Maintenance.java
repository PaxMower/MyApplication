package com.casa.myapplication.Logic;

public class Maintenance {

    private String date, truckId, truckNum, km, type, comments;

    public Maintenance(String date, String truckId, String truckNum, String km, String type, String comments) {
        this.date = date;
        this.truckId = truckId;
        this.truckNum = truckNum;
        this.km = km;
        this.type = type;
        this.comments = comments;
    }

    public Maintenance() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
