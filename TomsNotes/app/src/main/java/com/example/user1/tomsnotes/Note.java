package com.example.user1.tomsnotes;

import java.util.Random;

/**
 * Created by USER1 on 15/09/2016.
 */
public class Note {
    private String title;
    private String text;
    private int id;

    public Note(String title, String text){
        this.title = title;
        this.text = text;
        setId();
    }

    public int getId() {
        return id;
    }

    private void setId() {
        this.id = (new Random()).nextInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
