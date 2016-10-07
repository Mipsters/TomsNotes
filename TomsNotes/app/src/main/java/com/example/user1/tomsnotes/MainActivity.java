package com.example.user1.tomsnotes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static NoteServiceServer nsl;
    private Boolean delete = false;
    private GridView gv;
    private List<Note> notes;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        gv = (GridView) findViewById(R.id.gridView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NoteEdit.class));
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoteEdit.class);

                if(notes != null) {
                    intent.putExtra("title", notes.get(position).getTitle());
                    intent.putExtra("text", notes.get(position).getText());
                    intent.putExtra("loc", position);

                    startActivity(intent);
                }
            }
        });

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!delete) {
                    delete = true;

                    Snackbar.make(view, "deleting note...", Snackbar.LENGTH_LONG)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    if (delete) {
                                        nsl.deleteNote(position);
                                        if(notes != null)
                                            gv.setAdapter(new GridViewAdapter(notes));
                                        delete = false;
                                    }
                                }
                            }).setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete = false;
                        }
                    }).show();
                }
                return true;
            }
        });

        if (getResources().getConfiguration().orientation == 2) {
            gv.setColumnWidth((int) convertDpToPixel(150, this));
        }
        else {
            gv.setColumnWidth((int) convertDpToPixel(90, this));
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                notes = nsl.getNotes();
                gv.setAdapter(new GridViewAdapter(notes));
            }
        };

        if(nsl != null)
            runnable.run();

        nsl = NoteServiceServer.getInstance(runnable);
        //nsl = new NoteServiceLocal(this, runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nsl != null)
            runnable.run();
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    class GridViewAdapter extends BaseAdapter{

        List<Note> data;

        private GridViewAdapter(List<Note> list){
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
