package com.example.fxo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddFlashcardFolderActivity extends AppCompatActivity {

    // UI components
    EditText flashcardTitle, subjectName;
    Spinner spinnerFolders;
    Button cancelBtn, finishBtn;

    // Database helper
    DatabaseHelper Users_DB;

    // List to hold folder folders
    List<String> myFolder;
    List<Integer> myFolderID;

    // Variables to hold selected folder position and ID
    int pos, userID;
    String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard_folder);

        // Initialize database helper and list
        Users_DB = new DatabaseHelper(this);
        myFolder = new ArrayList<>();
        myFolderID = new ArrayList<>();

        // Initialize UI components
        flashcardTitle = findViewById(R.id.flashcard_title_input);
        subjectName = findViewById(R.id.subject_input);
        spinnerFolders = findViewById(R.id.spinner_folders);
        cancelBtn = findViewById(R.id.cancel_btn);
        finishBtn = findViewById(R.id.finish_btn);
        userID = getIntent().getIntExtra("USERID", 0);

        // Populate spinner with items from the database
        inputSpinnerItems();

        // Set up the spinner with folder folders
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, myFolder);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolders.setAdapter(spinnerArrayAdapter);

        // Set click listener for the finish button
        finishBtn.setOnClickListener(view -> {
            String title = flashcardTitle.getText().toString();
            String subject = subjectName.getText().toString();

            // Check if title or subject is empty
            if(title.isEmpty() || subject.isEmpty()){
                Toast.makeText(this, "Please fill both Title and Subject", Toast.LENGTH_SHORT).show();
            } else {
                // Insert flashcard folder data into the database
                boolean insert = Users_DB.insertFlashcardFolderData(title, subject, pos);
                if(insert){
                    Intent i = new Intent(AddFlashcardFolderActivity.this, FlashcardFolderActivity.class);
                    i.putExtra("USERID", userID);
                    i.putExtra("FOLDERID", pos);
                    i.putExtra("FOLDERNAME", spinnerFolders.getSelectedItem().toString());
                    startActivity(i);
                    Toast.makeText(this, "Flashcard Folder Created: " + title, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Adding flashcard folder Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for the cancel button
        cancelBtn.setOnClickListener(view -> finish());

        // Set item selected listener for the spinner
        spinnerFolders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = (myFolderID.get(0)) + (position); // Adjust position to match folder ID
                Toast.makeText(getApplicationContext(), "Position: " + pos, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    // Method to populate spinner items from the database
    private void inputSpinnerItems(){
        Cursor cursor = Users_DB.getFolders();
        if(cursor.getCount() == 0){
            Toast.makeText(AddFlashcardFolderActivity.this, "No db exist", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                if(cursor.getInt(2) == userID){
                    myFolder.add(cursor.getString(1)); // Add folders to the list
                    myFolderID.add(cursor.getInt(0));
                }
            }
        }
        cursor.close();
    }
}
