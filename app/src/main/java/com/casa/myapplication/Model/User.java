package com.casa.myapplication.Model;



public class User {

    private String employeeNameSettings, platformIdSettings, truckIdSettings, truckNumSettings;

    public User(String employeeNameSettings, String platformIdSettings,
                String truckIdSettings, String truckNumSettings) {

        this.employeeNameSettings = employeeNameSettings;
        this.platformIdSettings = platformIdSettings;
        this.truckIdSettings = truckIdSettings;
        this.truckNumSettings = truckNumSettings;
    }

    public User (){
        
    }

    public String getEmployeeNameSettings() {
        return employeeNameSettings;
    }

    public void setEmployeeNameSettings(String employeeNameSettings) {
        this.employeeNameSettings = employeeNameSettings;
    }

    public String getPlatformIdSettings() {
        return platformIdSettings;
    }

    public void setPlatformIdSettings(String platformIdSettings) {
        this.platformIdSettings = platformIdSettings;
    }

    public String getTruckIdSettings() {
        return truckIdSettings;
    }

    public void setTruckIdSettings(String truckIdSettings) {
        this.truckIdSettings = truckIdSettings;
    }

    public String getTruckNumSettings() {
        return truckNumSettings;
    }

    public void setTruckNumSettings(String truckNumSettings) {
        this.truckNumSettings = truckNumSettings;
    }
}
