package com.booking.model;

import java.util.Objects;

public class OfferData {
    private String title;
    private String price;

    private String rating;

    public OfferData(String title, String price, String rating) {
        this.title = title;
        this.price = price;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }


    public String getRating() {
        return rating;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OfferData offerData = (OfferData) o;
        return Objects.equals(title, offerData.title) && Objects.equals(price, offerData.price) && Objects.equals(rating, offerData.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, rating);
    }

    @Override
    public String toString() {
        return "OfferData{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
