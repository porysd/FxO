package com.example.fxo;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FlashcardFolderActivity extends AppCompatActivity implements RecyclerViewInterface {
    Button addFlashcardButton, backBtn;
    RecyclerView recyclerView;
    FlashcardFolderAdapter flashcardFolderAdapter;
    TextView folderTextView, noTableText;
    List<String> myTitle;
    List<Integer> myFlashcardFolderID;
    int folderID, userID;
    String folderName;
    DatabaseHelper Users_DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_folder);

        // Initialize UI elements
        folderTextView = findViewById(R.id.folderTextView);
        noTableText = findViewById(R.id.notable_text);
        recyclerView = findViewById(R.id.recycler);
        addFlashcardButton = findViewById(R.id.addflashcard_btn);
        backBtn = findViewById(R.id.back_btn);

        // Initialize Database Helper
        Users_DB = new DatabaseHelper(this);

        // Initialize data lists
        myTitle = new ArrayList<>();
        myFlashcardFolderID = new ArrayList<>();

        // Set up RecyclerView
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        flashcardFolderAdapter = new FlashcardFolderAdapter(this, myTitle, folderID, userID, folderName, this);
        recyclerView.setAdapter(flashcardFolderAdapter);

        // Get folder ID and name from intent
        folderID = getIntent().getIntExtra("FOLDERID", 0);
        folderName = getIntent().getStringExtra("FOLDERNAME");
        userID = getIntent().getIntExtra("USERID", 0);
        folderTextView.setText("Folder ID: " + folderID + " Folder Name: " + folderName);

        // Button click listener for adding flashcards
        addFlashcardButton.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardFolderActivity.this, AddFlashcardActivity.class);
            i.putExtra("FOLDERID", folderID);
            i.putExtra("FOLDERNAME", folderName);
            i.putExtra("USERID", userID);
            startActivity(i);
        });

        backBtn.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardFolderActivity.this, Nav.class);
            i.putExtra("USERID", userID);
            startActivity(i);
        });

        // Load flashcard data
        getData();
    }

    // Method to fetch flashcard data from the database
    private void getData() {
        Cursor cursor = Users_DB.getFlashcardFolders();
        if (cursor.getCount() == 0) {
            noTableText.setText("No Flashcard Folders");
            Toast.makeText(FlashcardFolderActivity.this, "No db exist", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(4) == folderID) {
                    myFlashcardFolderID.add(cursor.getInt(0));
                    myTitle.add(cursor.getString(1));
                }
            }
        }
        cursor.close();
    }

    // RecyclerView item click listener
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(FlashcardFolderActivity.this, FlashcardActivity.class);
        i.putExtra("USERID", userID);
        i.putExtra("FLASHCARDFOLDERID", myFlashcardFolderID.get(position));
        i.putExtra("FOLDERID", folderID);
        i.putExtra("FOLDERNAME", folderName);
        startActivity(i);
    }
}
