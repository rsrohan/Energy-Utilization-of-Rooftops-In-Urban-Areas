package com.example.minorproject2;

public class Plant {
    String imageUrl, plantName ,soilReq, minTemp, maxTemp, energy, weather, months;

    public Plant() {
    }

    public Plant(String imageUrl, String plantName, String soilReq, String minTemp, String maxTemp, String energy, String weather, String months) {
        this.imageUrl = imageUrl;
        this.plantName = plantName;
        this.soilReq = soilReq;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.energy = energy;
        this.weather = weather;
        this.months = months;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSoilReq() {
        return soilReq;
    }

    public void setSoilReq(String soilReq) {
        this.soilReq = soilReq;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }
}
