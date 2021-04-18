package com.example.softwarepatterns4;

import java.io.Serializable;

public class Customer implements Serializable {
    private String name, shippingAddress, paymentMethod, phoneNumber;
    //private int ;

    public Customer() {
    }

    public Customer(String name, String shippingAddress, String paymentMethod, String phoneNumber) {
        this.name = name;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
    }

    /*public Customer(String name, String shippingAddress, int phoneNumber) {
        this.name = name;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nPhone Number: " + this.phoneNumber + "\nShipping Address: " + this.shippingAddress + "\nPayment Method: " + this.paymentMethod;
    }
}
