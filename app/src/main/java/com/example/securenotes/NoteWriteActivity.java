package com.example.securenotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NoteWriteActivity extends AppCompatActivity {
    private Toast titleToast;
    private EditText titleEditText;
    private TextView dataTextView;
    private EditText contentEditText;
    private DatabaseOP databaseOP;
    private String currentDateTime;
    private Note lastNote;
    private long noteId = -1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);
        initToolbar();
        initDatabase();
        initView();
        loadNote();
    }

    private void initToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initDatabase() {
        databaseOP = new DatabaseOP(this);
    }

    private void initView() {
        titleEditText = findViewById(R.id.TitleEditText);
        int maxLengthTitle = 20;
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > maxLengthTitle) {
                    if (titleToast != null) {
                        titleToast.cancel();
                    }
                    titleToast = Toast.makeText(NoteWriteActivity.this, "You have entered too many characters", Toast.LENGTH_SHORT);
                    titleToast.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > maxLengthTitle) {
                    s.delete(maxLengthTitle, s.length());
                }
            }
        });

        currentDateTime = new SimpleDateFormat("MMM dd, yyyy, h:mm a", Locale.US).format(new Date());
        dataTextView = findViewById(R.id.textViewBelow);
        dataTextView.setText(currentDateTime);

        contentEditText = findViewById(R.id.ContentEditText);

        ImageButton confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            saveNoteToDatabase();
            finish();
        });
    }

    private void loadNote() {
        noteId = getIntent().getLongExtra("id", -1);
        if (noteId != -1) {
            databaseOP.open();
            lastNote = databaseOP.getNote(noteId);
            if (lastNote != null) {
                titleEditText.setText(lastNote.getTitle());
                contentEditText.setText(lastNote.getContent());
            }
            databaseOP.close();
        }
    }

    private void saveNoteToDatabase() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        databaseOP.open();
        if (noteId != -1) {
            lastNote.setId(noteId);
            lastNote.setTitle(title);
            lastNote.setTime(currentDateTime);
            lastNote.setContent(content);
            databaseOP.updateNote(lastNote);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Note note = new Note(title, content, currentDateTime, 1);
            databaseOP.addNote(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }
        databaseOP.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentEditText.requestFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(contentEditText, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
