package com.example.user1.tomsnotes;

import android.content.Context;

import java.io.IOException;
import java.util.List;

/**
 * Created by USER1 on 15/09/2016.
 */
public interface NoteActions {

    void saveNote(Context context, Note note) throws IOException;

    void deleteNote(Note note);

    List<Note> getNotes();
}