package com.casa.myapplication.Logic;

public class Prices {

    private String cityName, cityDistance;

    public Prices(String cityName, String cityDistance) {
        this.cityName = cityName;
        this.cityDistance = cityDistance;
    }

    public Prices() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityDistance() {
        return cityDistance;
    }

    public void setCityDistance(String cityDistance) {
        this.cityDistance = cityDistance;
    }

}
