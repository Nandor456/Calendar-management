package edu.bbte.idde.mnim2377.data.model;

import java.time.LocalDate;

public class Calendar extends BaseEntity{
    private String address;
    private String location;
    private LocalDate date;
    private Boolean isOnline;

    public Calendar(String address, String location, LocalDate date, Boolean isOnline) {
        this.address = address;
        this.location = location;
        this.date = date;
        this.isOnline = isOnline;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", isOnline=" + isOnline +
                '}';
    }
}
