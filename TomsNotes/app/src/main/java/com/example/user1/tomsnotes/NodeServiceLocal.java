package com.example.user1.tomsnotes;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by USER1 on 15/09/2016.
 */
public class NodeServiceLocal implements NoteActions {

    private enum FileManagement {READ_FROM_FILE,WRITE_TO_FILE,REWRITE_FILE}
    private enum FileChars {
        EMPTY_FILE {
            @Override
            public String toString(){
                return Character.toString((char)5);
            }
        },
        OUTER_SEPARATOR{
            @Override
            public String toString(){
                return Character.toString((char)6);
            }
        },
        INNER_SEPARATOR{
            @Override
            public String toString(){
                return Character.toString((char)7);
            }
        }
    }

    private static final String NOTE_NAME = "notes.txt";
    private List<Note> notes;
    private Context context;
    private Runnable runnable;

    public NodeServiceLocal(Context context, Runnable runnable) {
        this.context = context;
        this.runnable = runnable;
        notes = new ArrayList<>();

        new FileManagementTask().execute(FileManagement.READ_FROM_FILE);
    }

    @Override
    public void saveNote(Note note) {
        if(note.getText().equals(""))
            note.setText(FileChars.EMPTY_FILE.toString());

        notes.add(note);

        new FileManagementTask(note).execute(FileManagement.WRITE_TO_FILE);
    }

    @Override
    public void deleteNote(int location) {
        notes.remove(location);
        new FileManagementTask().execute(FileManagement.REWRITE_FILE);
    }

    @Override
    public void editNote(int location, Note newNote) {
        notes.set(location,newNote);
        new FileManagementTask().execute(FileManagement.REWRITE_FILE);
    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    private class FileManagementTask extends AsyncTask<FileManagement, Void, Void>{
        private Note note;

        private FileManagementTask(){}

        private FileManagementTask(Note note){
            this.note = note;
        }

        @Override
        protected Void doInBackground(FileManagement... params) {
            switch (params[0]) {
                case READ_FROM_FILE:
                    scanFromFile();
                    break;
                case WRITE_TO_FILE:
                    writeToFile(note);
                    break;
                case REWRITE_FILE:
                    recreateFile();
                    break;
            }
            return null;
        }
    }
    
    private void scanFromFile(){
        File file = new File(context.getFilesDir(), NOTE_NAME);

        try {
            file.createNewFile();

            Scanner read = new Scanner(context.openFileInput(NOTE_NAME));
            read.useDelimiter(FileChars.OUTER_SEPARATOR.toString());

            while(read.hasNext()) {
                String[] data = read.next().split(FileChars.INNER_SEPARATOR.toString());
                if (data.length == 2)
                    notes.add(new Note(data[0],data[1]));
            }
        } catch (Exception e) { }

        runnable.run();
    }

    private void writeToFile(Note note){
        try {
            PrintWriter out = new PrintWriter(
                    context.openFileOutput(NOTE_NAME, Context.MODE_APPEND));

            out.print(note.getTitle() + FileChars.INNER_SEPARATOR +
                    note.getText() + FileChars.OUTER_SEPARATOR);

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

            for (Note note : notes)
                out.print(note.getTitle() + FileChars.INNER_SEPARATOR +
                        note.getText() + FileChars.OUTER_SEPARATOR);

            out.close();
        } catch (IOException e) { }
    }
}