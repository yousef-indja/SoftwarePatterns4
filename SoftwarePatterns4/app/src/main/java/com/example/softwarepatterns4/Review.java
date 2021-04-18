package com.example.softwarepatterns4;

public class Review {

    String text, id, name;
    float stars;

    public Review() {
    }

    public Review(String text, String id, String name, float stars) {
        this.text = text;
        this.id = id;
        this.name = name;
        this.stars = stars;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String toString(){
        return "\""+this.text + ". \""+ this.getStars()+ "/5 Stars. \n-"+this.getName();
    }
}
