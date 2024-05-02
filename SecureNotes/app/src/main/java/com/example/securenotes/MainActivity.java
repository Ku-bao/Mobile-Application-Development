package com.example.securenotes;

import static java.util.Locale.filter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private List<Note> noteList;
    private ListView lv;
    private NoteAdapter noteadapter;
    private final Context context = this;
//    private ActivityResultLauncher<Intent> secondActivityResultLauncher;
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
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        refreshListView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteWriteActivity.class);
                startActivity(intent);
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteadapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteadapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == lv.getId()) {
            Note curNote = (Note) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, NoteWriteActivity.class);
            intent.putExtra("id", curNote.getId());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDeleteDialog(position);
        return true;
    }

    private void showDeleteDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null);
        builder.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        Button btnPositive = dialogView.findViewById(R.id.btnPositive);
        Button btnNegative = dialogView.findViewById(R.id.btnNegative);

        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();
        // 设置按钮的点击事件
        btnPositive.setOnClickListener(v -> {
            Note noteToDelete = noteList.get(position);
            databaseOP.open();
            databaseOP.removeNote(noteToDelete);
            databaseOP.close();
            refreshListView();  // 刷新列表显示
            deleteDialog.dismiss();
            Toast.makeText(MainActivity.this, "note has been deleted", Toast.LENGTH_SHORT).show();
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

    }
}