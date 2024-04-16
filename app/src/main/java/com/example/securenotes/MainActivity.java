package com.example.securenotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Note> noteList;
    private ListView lv;
    private NoteAdapter noteadapter;
    private final Context context = this;
    private ActivityResultLauncher<Intent> secondActivityResultLauncher;
    private DatabaseOP databaseOP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_data, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);


        lv = findViewById(R.id.lv);
        noteList = new ArrayList<>();
        noteadapter = new NoteAdapter(context, noteList);
        lv.setAdapter(noteadapter);
        refreshListView();


        secondActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            String time = data.getStringExtra("time");
                            String content = data.getStringExtra("content");
                            Note note = new Note(title, content, time, 1);
                            databaseOP = new DatabaseOP(context);
                            databaseOP.open();
                            databaseOP.addNote(note);
                            databaseOP.close();
                            refreshListView();
                        }
                    }
                });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteWriteActivity.class);
                secondActivityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Language) {
            return true;
        }
        if (id == R.id.Theme) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshListView() {
        databaseOP = new DatabaseOP(context);
        databaseOP.open();
        if (noteList.size() > 0) noteList.clear();
        noteList.addAll(databaseOP.getAllNotes());
        databaseOP.close();
        noteadapter.notifyDataSetChanged();
    }
}