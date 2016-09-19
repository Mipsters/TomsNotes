package com.example.user1.tomsnotes;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
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

    private ArrayList<Note> notes;
    private Context context;
    private Note note;

    public NodeServiceLocal(Context context) throws IOException {
        this.context = context;
        notes = new ArrayList<>();
        this.note = null;

        new FileManagmentTask().execute(0);
    }

    @Override
    public void saveNote(Note note) throws IOException {
        if(note.getText().equals(""))
            note.setText(Character.toString((char)5));

        notes.add(note);
        this.note = note;

        new FileManagmentTask().execute(1);
    }

    @Override
    public void deleteNote(int location) {
        notes.remove(location);
        new FileManagmentTask().execute(2);
    }

    @Override
    public void editNote(int location, Note newNote) {
        notes.set(location,newNote);
        new FileManagmentTask().execute(2);
    }

    @Override
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    private void recreateFile(){
        File file = new File(context.getFilesDir(),"notes.txt");
        while(!file.delete());

        try {
            file.createNewFile();

            PrintWriter out = new PrintWriter(
                    context.openFileOutput("notes.txt",Context.MODE_APPEND));

            for (Note note: notes)
                out.print(note.getTitle() + Character.toString((char) 7) +
                        note.getText() + Character.toString((char) 6));

            out.flush();

        } catch (IOException e) { }
    }

    private class FileManagmentTask extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                switch (params[0]) {
                    case 0:
                        File file = new File(context.getFilesDir(), "notes.txt");

                        if (!file.exists())
                            file.createNewFile();

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        context.openFileInput("notes.txt")));

                        String line, strdata = "";

                        while ((line = in.readLine()) != null)
                            strdata += line + '\n';

                        String[] allData = strdata.split(Character.toString((char) 6));

                        for (String oneData : allData) {
                            String[] data = oneData.split(Character.toString((char) 7));
                            if (data.length == 2)
                                notes.add(new Note(data[0], data[1].equals(Character.toString((char)5)) ? "" : data[1]));
                        }

                        in.close();
                        break;
                    case 1:
                        PrintWriter out = new PrintWriter(
                                context.openFileOutput("notes.txt",Context.MODE_APPEND));

                        out.print(note.getTitle() + Character.toString((char) 7) +
                                note.getText() + Character.toString((char) 6));
                        out.flush();
                        note = null;

                        out.close();
                        break;
                    case 2:
                        recreateFile();
                        break;
                }
            }catch (Exception e){}
            return null;
        }
    }
}