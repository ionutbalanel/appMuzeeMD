package com.ltstefanesti.muzeemd.models;

public class MuseumDataClass {
    private String name;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String address;
    private String phoneNumber;
    private String website;
    private String email;
    private String hoursOfOperation;
    private String description;
    private String latitude;
    private String longitude;
    private String key;

    public MuseumDataClass() {
    }

    public MuseumDataClass(String name, String imageOne, String imageTwo, String imageThree, String address, String phoneNumber, String website, String email, String hoursOfOperation, String description, String latitude, String longitude) {
        this.name = name;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.email = email;
        this.hoursOfOperation = hoursOfOperation;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getImageOne() {
        return imageOne;
    }

    public String getImageTwo() {
        return imageTwo;
    }

    public String getImageThree() {
        return imageThree;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}