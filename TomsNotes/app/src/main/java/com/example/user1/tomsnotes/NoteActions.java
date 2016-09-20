package com.example.user1.tomsnotes;

import android.content.Context;

import java.io.IOException;
import java.util.List;

/**
 * Created by USER1 on 15/09/2016.
 */
public interface NoteActions {

    void saveNote(Note note) throws IOException;

    void deleteNote(int location);

    void editNote(int locaton, Note newNote);

    List<Note> getNotes();
}