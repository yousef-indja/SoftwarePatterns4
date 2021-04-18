package com.example.softwarepatterns4;

import java.io.Serializable;

public class Customer implements Serializable {
    private String name , shippingAddress, paymentMethod, phoneNumber;
    //private int ;

    public Customer() {
    }

    private Customer(final Builder builder){
        name = builder.name;
        shippingAddress=builder.shippingAddress;
        paymentMethod=builder.paymentMethod;
        phoneNumber=builder.phoneNumber;
    }
    /*public Customer(String name, String shippingAddress, String paymentMethod, String phoneNumber) {
        this.name = name;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
    }*/

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

    static class Builder{
        private String name , shippingAddress, paymentMethod, phoneNumber;

        public Builder setName(final String name){
            this.name = name;
            return this;
        }
        public Builder setShippingAddress(final String shippingAddress){
            this.shippingAddress = shippingAddress;
            return this;
        }
        public Builder setPaymentMethod(final String paymentMethod){
            this.paymentMethod = paymentMethod;
            return this;
        }
        public Builder setPhoneNumber(final String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }


        public Customer create(){
            return new Customer(this);
        }


    }
}


