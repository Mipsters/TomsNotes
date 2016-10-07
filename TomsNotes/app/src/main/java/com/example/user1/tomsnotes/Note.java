package com.example.user1.tomsnotes;


import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by USER1 on 15/09/2016.
 */

@ParseClassName("Note")
public class Note extends ParseObject {

    public Note() {
        super();
    }
    
    public Note(String title, String text){
        super();
        setTitle(title);
        setText(text);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title",title);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text",text);
    }

    public void copyNote(Note note){
        setTitle(note.getTitle());
        setText(note.getText());
    }
}
