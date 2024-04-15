package com.example.securenotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        EditText titleeditText = findViewById(R.id.TitleEditText);
        int maxLengthTitle = 20;

        String currentDateTime = new SimpleDateFormat("MMM dd, yyyy, h:mm a", Locale.US).format(new Date());
        TextView dataTextView = findViewById(R.id.textViewBelow);
        dataTextView.setText(currentDateTime);

        titleeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count > 0 && s.length() >= maxLengthTitle) {
//                    Toast.makeText(NoteWriteActivity.this, "You have entered too many characters", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > maxLengthTitle) {
                    s.delete(maxLengthTitle, s.length());
                    if (titleToast != null) {
                        titleToast.cancel();
                    }
                    titleToast = Toast.makeText(NoteWriteActivity.this, "You have entered too many characters", Toast.LENGTH_SHORT);
                    titleToast.show();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        EditText contentEditText = findViewById(R.id.ContentEditText);
        contentEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(contentEditText, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}