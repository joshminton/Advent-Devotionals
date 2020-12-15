package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import io.noties.markwon.Markwon;

import android.os.Bundle;
import android.widget.TextView;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        DevotionalEntry devotionalEntry =

        TextView txtEntry = findViewById(R.id.txtEntry);
        // obtain an instance of Markwon
        final Markwon markwon = Markwon.create(context);

        // set markdown
        markwon.setMarkdown(txtEntry, );


    }
}