package com.example.user1.tomsnotes;

import android.os.AsyncTask;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chen on 06/10/2016.
 */

public class NoteServiceServer implements NoteActions {
    private enum ServerManagement {READ_FROM_SERVER,EDIT_SERVER}

    private static NoteServiceServer instance = null;

    public static NoteServiceServer getInstance(){
        if(instance == null)
            instance = new NoteServiceServer();
        return instance;
    }

    private NoteServiceServer(){
        notes = new ArrayList<>();
        new ServerManagementTask().execute(ServerManagement.READ_FROM_SERVER);
    }

    private ArrayList<Note> notes;

    @Override
    public void saveNote(Note note) throws IOException {
        notes.add(note);
        note.saveInBackground();
    }

    @Override
    public void deleteNote(int location) {
        notes.get(location).deleteInBackground();
        notes.remove(location);
    }

    @Override
    public void editNote(int location, Note newNote) {
        notes.get(location).copy(newNote);
        new ServerManagementTask(location, newNote).execute(ServerManagement.EDIT_SERVER);
    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    private class ServerManagementTask extends AsyncTask<ServerManagement, Void, Void> {
        private Note note;
        private int location;

        private ServerManagementTask(){}

        private ServerManagementTask(int location,Note note){
            this.note = note;
            this.location = location;
        }

        @Override
        protected Void doInBackground(ServerManagement... params) {
            switch (params[0]) {
                case READ_FROM_SERVER:
                    scanFromServer();
                    break;
                case EDIT_SERVER:
                    editServer(location, note);
                    break;
            }
            return null;
        }
    }

    private void scanFromServer(){
        ParseQuery<Note> query = new ParseQuery<>("Note");
        try {
            notes = (ArrayList<Note>)query.find();
        }
        catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    private void editServer(int location, final Note newNote){
        ParseQuery<Note> query = new ParseQuery<>("Note");
        query.getInBackground(notes.get(location).getObjectId(), new GetCallback<Note>() {
            @Override
            public void done(Note object, ParseException e) {
                object.copy(newNote);
            }
        });
    }
}
