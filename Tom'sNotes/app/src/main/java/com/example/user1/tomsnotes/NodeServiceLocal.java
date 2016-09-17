package com.example.user1.tomsnotes;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by USER1 on 15/09/2016.
 */
public class NodeServiceLocal implements NoteActions {

    ArrayList<Note> notes;
    Context context;

    public NodeServiceLocal(Context context) throws IOException {
        this.context = context;
        notes = new ArrayList<>();

        File file = new File(context.getFilesDir(),"notes.txt");

        if(!file.exists())
            file.createNewFile();

        BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            context.openFileInput("notes.txt")));

        String line, strdata = "";

        while((line = in.readLine()) != null)
            strdata += line;

        Log.e("data",strdata);

        String[] allData = strdata.split(Character.toString((char) 6));

        for(String oneData : allData) {
            String[] data = oneData.split(Character.toString((char) 7));
            if(data.length == 2)
                notes.add(new Note(data[0], data[1]));
        }

        in.close();
    }

    @Override
    public void saveNote(Note note) throws IOException {
        notes.add(note);

        PrintWriter out = new PrintWriter(
                context.openFileOutput("notes.txt",Context.MODE_APPEND));

        out.print(note.getTitle() + Character.toString((char) 7) +
                  note.getText() + Character.toString((char) 6));

        out.close();
    }

    @Override
    public void deleteNote(int location) {
        notes.remove(location);
        recreateFile();
    }

    @Override
    public void editNote(int location, Note newNote) {
        notes.set(location,newNote);
        recreateFile();
    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    private void recreateFile(){
        File file = new File(context.getFilesDir(),"notes.txt");
        while(!file.delete());

        try {
            PrintWriter out = new PrintWriter(
                    context.openFileOutput("notes.txt",Context.MODE_PRIVATE));

            file.createNewFile();

            for (Note note: notes)
                out.print(note.getTitle() + Character.toString((char) 7) +
                        note.getText() + Character.toString((char) 6));
        } catch (IOException e) { }

    }
}
