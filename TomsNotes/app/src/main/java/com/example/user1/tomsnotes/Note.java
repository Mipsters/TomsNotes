package com.example.user1.tomsnotes;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by USER1 on 15/09/2016.
 */
@ParseClassName("Note")
public class Note extends ParseObject implements Serializable {

    public Note(){
        super("Note");
    }

    public Note(String title, String text){
        super();
        setTitle(title);
        setText(text);
    }

    public String getTitle() {
        return get("title").toString();
    }

    public void setTitle(String title) {
        put("title",title);
    }

    public String getText() {
        return get("text").toString();
    }

    public void setText(String text) {
        put("text",text);
    }

    public void copy(Note note){
        put("title",note.getTitle());
        put("text",note.getText());
    }
}