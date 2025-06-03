package com.booking.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingCase {
    private int id;
    private String destination;
    private Date checkIn;
    private Date checkOut;
    private int guests;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCheckInAsString() {
        return checkIn != null ? formatter.format(checkIn) : null;
    }

    public String getCheckOutAsString() {
        return checkOut != null ? formatter.format(getCheckOut()) : null;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    @Override
    public String toString() {
        return "BookingCase{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", checkIn=" + getCheckInAsString() +
                ", checkOut=" + getCheckOutAsString() +
                ", guests=" + guests +
                '}';
    }
}
