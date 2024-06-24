package com.example.fxo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SeeAllFlashcardsActivity extends AppCompatActivity {

    RecyclerView recyclerViewSeeAll;
    SeeAllFlashcardsAdapter adapter;
    List<String> flashcardTitles;
    List<Integer> flashcardIDs;
    DatabaseHelper Users_DB;

    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_flashcards);

        recyclerViewSeeAll = findViewById(R.id.recyclerViewSeeAll);
        Users_DB = new DatabaseHelper(this);
        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(view -> {
            finish();
        });

        flashcardTitles = new ArrayList<>();
        flashcardIDs = new ArrayList<>();

        adapter = new SeeAllFlashcardsAdapter(this, flashcardTitles, flashcardIDs);
        recyclerViewSeeAll.setAdapter(adapter);
        recyclerViewSeeAll.setLayoutManager(new GridLayoutManager(this,2 ));

        // Fetch and load all flashcards from database
        loadAllFlashcards();
    }

    private void loadAllFlashcards() {
        Cursor cursor = Users_DB.getAllFlashcards();

        if (cursor != null && cursor.moveToFirst()) {
            flashcardTitles.clear();
            flashcardIDs.clear();

            do {
                int flashcardID = cursor.getInt(cursor.getColumnIndex("flashcardfolderID"));
                String flashcardTitle = cursor.getString(cursor.getColumnIndex("title"));

                flashcardIDs.add(flashcardID);
                flashcardTitles.add(flashcardTitle);
            } while (cursor.moveToNext());

            adapter.notifyDataSetChanged();

            cursor.close();
        } else {
            Toast.makeText(this, "No flashcard folders found", Toast.LENGTH_SHORT).show();
        }
    }

}
