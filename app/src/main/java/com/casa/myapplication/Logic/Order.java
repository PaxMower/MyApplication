package com.casa.myapplication.Logic;

/**
 * Created by Gastby on 29/06/2017.
 */

public class Order {

    private String arrivalHour, charger, client, container, day, departureHour,
            destination, driver, kmStart, month, platform, startHour, truckID,
            truckNumber, year, endHour, kmEnd, mail, password;


    public Order(String arrivalHour, String charger, String client, String container, String day, String departureHour, String destination, String driver, String kmStart, String month, String platform, String startHour, String truckID, String truckNumber, String year, String endHour, String kmEnd, String mail, String password) {
        this.arrivalHour = arrivalHour;
        this.charger = charger;
        this.client = client;
        this.container = container;
        this.day = day;
        this.departureHour = departureHour;
        this.destination = destination;
        this.driver = driver;
        this.kmStart = kmStart;
        this.month = month;
        this.platform = platform;
        this.startHour = startHour;
        this.truckID = truckID;
        this.truckNumber = truckNumber;
        this.year = year;
        this.endHour = endHour;
        this.kmEnd = kmEnd;
        this.mail = mail;
        this.password = password;
    }


    public String getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(String arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(String departureHour) {
        this.departureHour = departureHour;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getKmStart() {
        return kmStart;
    }

    public void setKmStart(String kmStart) {
        this.kmStart = kmStart;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getTruckID() {
        return truckID;
    }

    public void setTruckID(String truckID) {
        this.truckID = truckID;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getKmEnd() {
        return kmEnd;
    }

    public void setKmEnd(String kmEnd) {
        this.kmEnd = kmEnd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
