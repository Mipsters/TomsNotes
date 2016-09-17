package com.example.user1.tomsnotes;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class NoteEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        EditText title = (EditText)findViewById(R.id.editText),
                 text = (EditText)findViewById(R.id.editText2);

        Intent intent = getIntent();

        final int loc = intent.getIntExtra("loc",-1);

        if(intent.getStringExtra("title") != null &&
           intent.getStringExtra("text") != null &&
           intent.getIntExtra("loc",-1) != -1){
            title.setText(intent.getStringExtra("title"));
            text.setText(intent.getStringExtra("text"));
        }

        fab.setOnClickListener(v -> {
            if(loc != -1)
                try {
                    MainActivity.nsl.editNote(loc,new Note(title.getText().toString(),
                            text.getText().toString()));
                } catch (Exception e) {
                    Snackbar.make(v,"couldnt edit the note",Snackbar.LENGTH_SHORT);
                }
            else
                try {
                    MainActivity.nsl.saveNote(new Note(title.getText().toString(),
                            text.getText().toString()));
                } catch (Exception e) {
                    Snackbar.make(v,"couldnt add the note",Snackbar.LENGTH_SHORT);
                }
            finish();
        });
    }
}
