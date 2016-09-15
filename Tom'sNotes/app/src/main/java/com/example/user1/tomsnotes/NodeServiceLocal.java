package com.example.user1.tomsnotes;

import android.content.Context;

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

    List<Note> notes;

    public NodeServiceLocal(Context context) throws IOException {
        notes = new ArrayList<>();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                context.openFileInput("notes.txt")));

        String line, strdata = "";

        while((line = in.readLine()) != null)
            strdata += line;

        String[] allData = strdata.split(Character.toString((char) 6));

        for(String oneData: allData) {
            String[] data = oneData.split(Character.toString((char) 7));
            notes.add(new Note(data[0], data[1]));
        }

        in.close();
    }

    @Override
    public void saveNote(Context context,Note note) throws IOException {
        notes.add(note);

        PrintWriter out = new PrintWriter(
                context.openFileOutput("notes.txt",Context.MODE_PRIVATE));

        out.print(note.getTitle() + Character.toString((char) 7) +
                  note.getText() + Character.toString((char) 6));

        out.close();
    }

    @Override
    public void deleteNote(Note note) {

    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }
}
