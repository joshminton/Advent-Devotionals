package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import io.noties.markwon.Markwon;
import io.noties.markwon.image.ImagesPlugin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EntryActivity extends AppCompatActivity {

    private DevotionalEntry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        entry = (DevotionalEntry) getIntent().getSerializableExtra("Entry");

        TextView txtEntry = findViewById(R.id.txtEntry);
        ((TextView) findViewById(R.id.txtEntryTitle)).setText(entry.getTitle());
        ((TextView) findViewById(R.id.txtEntryDevotionalTitle)).setText(entry.getSeriesTitle());
        // obtain an instance of Markwon

        final Markwon markwon = Markwon.builder(getApplicationContext()).usePlugin(ImagesPlugin.create()).build();
        // set markdown
        markwon.setMarkdown(txtEntry, entry.getDescription().replace("http:", "https:"));
        Picasso.get().load(entry.getImageURL()).into((ImageView) findViewById(R.id.entryImage), new Callback() {
            @Override
            public void onSuccess() {
                ((ImageView) findViewById(R.id.entryImage)).setScaleType(ImageView.ScaleType.CENTER_CROP);//Or ScaleType.FIT_CENTER
            }
            @Override
            public void onError(Exception e) {
                //nothing for now
            }
        });
    }
}