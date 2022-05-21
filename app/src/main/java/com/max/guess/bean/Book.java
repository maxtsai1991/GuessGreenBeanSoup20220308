package com.max.guess.bean;

public class Book {
    private Integer imageId;
    private String title;

    public Book() {
    }

    public Book(Integer imageId, String title) {
        this.imageId = imageId;
        this.title = title;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
