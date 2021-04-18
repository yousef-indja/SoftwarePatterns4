package com.example.softwarepatterns4;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Comparator;

public class Stock implements Serializable {
    private String title, manufacturer, category;
    private double price;
    //private Bitmap image;
    private int id, stock, quantity;
    private boolean reviewed;

    public Stock() {
    }

    /*public Stock(int id, String title, String manufacturer, String category, double price, Bitmap image) {
        this.id = id;
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.image = image;
    }*/

    public Stock(int id, String title, String manufacturer, String category, double price, int stock) {
        this.id = id;
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Stock(int id, String title, String manufacturer, String category, double price, int stock, int quantity) {
        this.id = id;
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
    }

    public Stock(int id, String title, String manufacturer, String category, double price, int stock, int quantity, boolean reviewed) {
        this.id = id;
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
        this.reviewed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /*public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "title='" + title + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", id=" + id +
                ", stock=" + stock +
                ", reviewed=" + reviewed +
                '}';
    }

    public String toStringForRV(){
        return "Manufacturer:    " + this.manufacturer + "\n Category:    " + this.category + "\nStock:    " + this.stock;
    }

    public String toStringForCartRV(){
        return "Manufacturer:    " + this.manufacturer + "\n Category:    " + this.category + "\nStock:    " + this.stock + "\nQuantity:    " + this.quantity;
    }



}

class SortByCategory implements Comparator<Stock>{

    @Override
    public int compare(Stock a, Stock b) {
        return a.getCategory().compareTo(b.getCategory());
    }
}

class SortByTitle implements Comparator<Stock>{

    @Override
    public int compare(Stock a, Stock b) {
        return a.getTitle().compareTo(b.getTitle());
    }
}

class SortByManufacturer implements Comparator<Stock>{

    @Override
    public int compare(Stock a, Stock b) {
        return a.getManufacturer().compareTo(b.getManufacturer());
    }
}

class SortByPrice implements Comparator<Stock>{

    @Override
    public int compare(Stock a, Stock b) {
        if(a.getPrice()<b.getPrice()){
            return -1;
        }
        if(a.getPrice()>b.getPrice()){
            return 1;
        }
        return 0;
    }
}


