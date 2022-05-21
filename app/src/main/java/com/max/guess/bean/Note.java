package com.max.guess.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Note implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private Timestamp lastUpdateTime;

    public Note() {
    }

    public Note(Integer id, String title, String content, Timestamp lastUpdateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
