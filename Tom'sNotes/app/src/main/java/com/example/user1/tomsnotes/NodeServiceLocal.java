package com.example.user1.tomsnotes;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by USER1 on 15/09/2016.
 */
public class NodeServiceLocal implements NoteActions {

    private static final String NOTE_NAME = "notes.txt";
    private enum FileManagment {READ_FROM_FILE,WRITE_TO_FILE,REWRITE_FILE}
    private enum FileChars {
        EMPTY_FILE {
            @Override
            public String toString(){
                return Character.toString((char)5);
            }
        },
        OUTER_SEPERATOR{
            @Override
            public String toString(){
                return Character.toString((char)6);
            }
        },
        INNER_SEPERATOR{
            @Override
            public String toString(){
                return Character.toString((char)7);
            }
        }
    }

    private ArrayList<Note> notes;
    private Context context;
    private Note note;

    public NodeServiceLocal(Context context) throws IOException {
        this.context = context;
        notes = new ArrayList<>();
        this.note = null;

        new FileManagmentTask().execute(FileManagment.READ_FROM_FILE);
    }

    @Override
    public void saveNote(Note note) throws IOException {
        if(note.getText().equals(""))
            note.setText(FileChars.EMPTY_FILE.toString());

        notes.add(note);
        this.note = note;

        new FileManagmentTask().execute(FileManagment.WRITE_TO_FILE);
    }

    @Override
    public void deleteNote(int location) {
        notes.remove(location);
        new FileManagmentTask().execute(FileManagment.REWRITE_FILE);
    }

    @Override
    public void editNote(int location, Note newNote) {
        notes.set(location,newNote);
        new FileManagmentTask().execute(FileManagment.REWRITE_FILE);
    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    private class FileManagmentTask extends AsyncTask<FileManagment, Void, Void>{

        @Override
        protected Void doInBackground(FileManagment... params) {
            switch (params[0]) {
                case READ_FROM_FILE:
                    scanFromFile();
                    break;
                case WRITE_TO_FILE:
                    writeToFile();
                    break;
                case REWRITE_FILE:
                    recreateFile();
                    break;
            }
            return null;
        }
    }
    
    void scanFromFile(){
        File file = new File(context.getFilesDir(), NOTE_NAME);

        try {
            file.createNewFile();

            Scanner read = new Scanner(context.openFileInput(NOTE_NAME));
            read.useDelimiter(FileChars.OUTER_SEPERATOR.toString());

            while(read.hasNext()) {
                String[] data = read.next().split(FileChars.INNER_SEPERATOR.toString());
                if (data.length == 2)
                    notes.add(new Note(data[0],data[1]));
            }
        } catch (Exception e) { }
    }

    private void writeToFile(){
        try {
            PrintWriter out = new PrintWriter(
                    context.openFileOutput(NOTE_NAME, Context.MODE_APPEND));

            out.print(note.getTitle() + FileChars.INNER_SEPERATOR +
                    note.getText() + FileChars.OUTER_SEPERATOR);

            note = null;

            out.close();
        }catch (Exception e){}
    }

    private void recreateFile(){
        File file = new File(context.getFilesDir(), NOTE_NAME);

        while(!file.delete());

        try {
            file.createNewFile();

            PrintWriter out = new PrintWriter(
                    context.openFileOutput(NOTE_NAME,Context.MODE_APPEND));

            for (Note note: notes)
                out.print(note.getTitle() + FileChars.INNER_SEPERATOR +
                        note.getText() + FileChars.OUTER_SEPERATOR);

            out.close();
        } catch (IOException e) { }
    }
}