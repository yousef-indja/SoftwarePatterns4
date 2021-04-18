package com.example.softwarepatterns4;

public class Review {

    String text, id, name;
    float stars;

    public Review() {
    }

    private Review(final Builder builder){
        text = builder.text;
        id = builder.id;
        name = builder.name;
        stars=builder.stars;
    }

    /*public Review(String text, String id, String name, float stars) {
        this.text = text;
        this.id = id;
        this.name = name;
        this.stars = stars;
    }*/

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

    static class Builder{
        private String text, id, name;
        private float stars;

        public Builder setText(final String text){
            this.text = text;
            return this;
        }
        public Builder setId(final String id){
            this.id = id;
            return this;
        }
        public Builder setName(final String name){
            this.name = name;
            return this;
        }
        public Builder setStars(final float stars){
            this.stars = stars;
            return this;
        }
        public Review create(){return new Review(this);
        }
    }
}
