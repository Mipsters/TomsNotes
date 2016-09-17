package com.example.user1.tomsnotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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
                    Toast.makeText(NoteEdit.this,"couldnt edit the note",Toast.LENGTH_SHORT).show();
                }
            else
                try {
                    MainActivity.nsl.saveNote(new Note(title.getText().toString(),
                            text.getText().toString()));
                } catch (Exception e) {
                    Toast.makeText(NoteEdit.this,"couldnt add the note",Toast.LENGTH_SHORT).show();
                }
            finish();
        });
    }
}
