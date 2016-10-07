package com.example.user1.tomsnotes;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tom on 07/10/2016.
 */
public class NodeServiceServer implements NoteActions {

    private List<Note> notes;
    private static Runnable runnable;

    private static NodeServiceServer ourInstance = new NodeServiceServer();

    public static NodeServiceServer getInstance(Runnable runnable) {
        NodeServiceServer.runnable = runnable;
        return ourInstance;
    }

    private NodeServiceServer() {
        notes = new ArrayList<>();
        ParseQuery<Note> query = ParseQuery.getQuery("Note");
        query.findInBackground(new FindCallback<Note>() {
            @Override
            public void done(List<Note> objects, ParseException e) {
                notes = objects;
                runnable.run();
            }
        });
    }

    @Override
    public void saveNote(Note note) {
        notes.add(note);
        note.saveInBackground();
    }

    @Override
    public void deleteNote(int location) {
        notes.get(location).deleteInBackground();
        notes.remove(location);
    }

    @Override
    public void editNote(int location, final Note newNote) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
        query.getInBackground(notes.get(location).getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.put("title",newNote.getTitle());
                object.put("text",newNote.getText());
                object.saveInBackground();
            }
        });
        notes.get(location).copyNote(newNote);
    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }
}
