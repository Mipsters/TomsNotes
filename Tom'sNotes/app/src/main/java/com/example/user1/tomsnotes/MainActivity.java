package com.example.user1.tomsnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v ->
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        GridView gv = (GridView) findViewById(R.id.gridView);

        NodeServiceLocal nsl = null;

        try {
            nsl = new NodeServiceLocal(this);
        }catch (Exception e){ }

        final ArrayList<Note> notes = nsl.getNotes();
        gv.setAdapter(new GridViewAdapter(notes));
        /*
        ArrayList<Note> notes = new ArrayList<>();

        notes.add(new Note("title 1","text 1"));
        notes.add(new Note("title 2","text 2"));
        notes.add(new Note("title 3","text 3"));
        notes.add(new Note("title 4","text 4"));
        notes.add(new Note("title 5","text 5"));
        */

        gv.setAdapter(new GridViewAdapter(notes));

        gv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, NoteEdit.class);

            intent.putExtra("title",notes.get(position).getTitle());
            intent.putExtra("text",notes.get(position).getText());
            intent.putExtra("loc",position);

            startActivity(intent);
        });
    }

    class GridViewAdapter extends BaseAdapter{

        ArrayList<Note> data;

        public GridViewAdapter(ArrayList arrayList){
            data = arrayList;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.gridview_child,null);

            TextView title = (TextView)convertView.findViewById(R.id.textView);
            TextView text = (TextView)convertView.findViewById(R.id.textView2);

            Note note = data.get(position);

            title.setText(note.getTitle());
            text.setText(note.getText());

            return convertView;
        }
    }
}
