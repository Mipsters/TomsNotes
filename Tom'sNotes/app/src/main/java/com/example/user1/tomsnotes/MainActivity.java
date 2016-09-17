package com.example.user1.tomsnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static NodeServiceLocal nsl;
    private Boolean delete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        GridView gv = (GridView) findViewById(R.id.gridView);

        fab.setOnClickListener(v ->
            startActivity(new Intent(this, NoteEdit.class)));

        nsl = null;

        try {
            nsl = new NodeServiceLocal(this);
            List<Note> notes = nsl.getNotes();

            gv.setAdapter(new GridViewAdapter(notes));

            gv.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(this, NoteEdit.class);

                intent.putExtra("title",notes.get(position).getTitle());
                intent.putExtra("text",notes.get(position).getText());
                intent.putExtra("loc",position);

                startActivity(intent);
            });

            gv.setOnItemLongClickListener((parent, view, position, id) -> {
                if(!delete) {
                    delete = true;

                    Snackbar.make(view, "deleting note...", Snackbar.LENGTH_LONG)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    if (delete) {
                                        nsl.deleteNote(position);
                                        gv.setAdapter(new GridViewAdapter(notes));
                                        delete = false;
                                    }
                                }
                            }).setAction("cancel", (v) -> delete = false)
                            .show();
                }
                return true;
            });
        }catch (Exception e){
            Snackbar.make(coordinatorLayout, "Cannot load the notes", Snackbar.LENGTH_LONG).show();
        }


    }

    class GridViewAdapter extends BaseAdapter{

        List<Note> data;

        public GridViewAdapter(List list){
            data = list;
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
