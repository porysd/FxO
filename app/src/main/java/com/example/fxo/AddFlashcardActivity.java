package com.example.fxo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddFlashcardActivity extends AppCompatActivity {
    // UI components
    EditText questionText, answerText;
    Button addBtn, finishBtn, bckBtn;
    DatabaseHelper Users_DB;
    int flashcardfolderID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        // Initialize database helper
        Users_DB = new DatabaseHelper(this);

        // Initialize UI components
        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        addBtn = findViewById(R.id.add_btn);
        bckBtn = findViewById(R.id.bck_btn);
        finishBtn = findViewById(R.id.finish_btn);

        // Get flashcard folder ID from intent
        flashcardfolderID = User.getInstance().getFlashcardFolderID();

        // Set onClick listener for the add button
        addBtn.setOnClickListener(v -> {
            String question = questionText.getText().toString();
            String answer = answerText.getText().toString();

            // Check if question or answer is empty
            if(question.isEmpty() || answer.isEmpty()){
                Toast.makeText(this, "Please fill both question and answer", Toast.LENGTH_SHORT).show();
            } else {
                // Insert flashcard data into the database
                boolean insertSuccess = Users_DB.insertFlashcardData(question, answer, flashcardfolderID);
                if(insertSuccess){
                    Toast.makeText(this, "Flashcard question created: " + question, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Adding flashcard failed", Toast.LENGTH_SHORT).show();
                }
            }
            questionText.setText("");
            answerText.setText("");
        });

        bckBtn.setOnClickListener(view -> finish());

        // Set onClick listener for the finish button
        finishBtn.setOnClickListener(view -> {
            // Navigate back to the flashcard folder activity
            Intent i = new Intent(AddFlashcardActivity.this, FlashcardActivity.class);
            startActivity(i);
        });
    }
}
