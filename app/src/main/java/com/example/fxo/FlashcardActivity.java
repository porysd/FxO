package com.example.fxo;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {
    // UI components
    TextView noTableText, frontCard, backCard;
    Button addFlashcardBtn, nextBtn, prevBtn, backBtn, flip;

    // Database helper
    DatabaseHelper Users_DB;

    // FlashcardActivity data
    List<String> myQuestions;
    List<String> myAnswers;
    int flashcardfolderID, folderID, index, userID;
    String folderName;

    // Flip animation
    private AnimatorSet frontAnim, backBtnAnim;
    private boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Initialize UI components
        noTableText = findViewById(R.id.notable_text);
        addFlashcardBtn = findViewById(R.id.addflashcard_btn);
        prevBtn = findViewById(R.id.prev_btn);
        nextBtn = findViewById(R.id.next_btn);
        backBtn = findViewById(R.id.back_btn);
        flip = findViewById(R.id.flip_btn);
        frontCard = findViewById(R.id.front_card);
        backCard = findViewById(R.id.back_card);

        // Initialize data lists
        myQuestions = new ArrayList<>();
        myAnswers = new ArrayList<>();
        prevBtn.setEnabled(false);

        // Initialize database helper and get flashcard folder ID
        Users_DB = new DatabaseHelper(this);
        flashcardfolderID = getIntent().getIntExtra("FLASHCARDFOLDERID", 0);
        userID = getIntent().getIntExtra("USERID", 0);
        folderID = getIntent().getIntExtra("FOLDERID", 0);
        folderName = getIntent().getStringExtra("FOLDERNAME");


        getData();



        backBtn.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardActivity.this, FlashcardFolderActivity.class);
            i.putExtra("FOLDERID", folderID);
            i.putExtra("FOLDERNAME", folderName);
            i.putExtra("USERID", userID);
            startActivity(i);
        });

        // Set event listeners
        addFlashcardBtn.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardActivity.this, AddFlashcardActivity.class);
            i.putExtra("FLASHCARDFOLDERID", flashcardfolderID);
            i.putExtra("FOLDERID", folderID);
            i.putExtra("FOLDERNAME", folderName);
            i.putExtra("USERID", userID);
            startActivity(i);
        });

        nextBtn.setOnClickListener(v -> {
            if (index < myQuestions.size() - 1) {
                index++;
                frontCard.setText(myQuestions.get(index));
                backCard.setText(myAnswers.get(index));
                prevBtn.setEnabled(true);
                if (index == myQuestions.size() - 1) {
                    nextBtn.setEnabled(false);
                }
            }
        });

        prevBtn.setOnClickListener(v -> {
            if (index > 0) {
                index--;
                frontCard.setText(myQuestions.get(index));
                backCard.setText(myAnswers.get(index));
                nextBtn.setEnabled(true);
                if (index == 0) {
                    prevBtn.setEnabled(false);
                }
            }
        });

        index = 0;
        // Set initial flashcard data if available
        if (!myQuestions.isEmpty() && !myAnswers.isEmpty()) {
            frontCard.setText(myQuestions.get(index));
            backCard.setText(myAnswers.get(index));
        } else {
            noTableText.setText("No questions available for this flashcard folder.");
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
        }

        // Set up flip animation
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        frontCard.setCameraDistance(8000 * scale);
        backCard.setCameraDistance(8000 * scale);

        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        backBtnAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);

        flip.setOnClickListener(view -> {
            if (isFront) {
                frontAnim.setTarget(frontCard);
                backBtnAnim.setTarget(backCard);
                frontAnim.start();
                backBtnAnim.start();
                isFront = false;
            } else {
                frontAnim.setTarget(backCard);
                backBtnAnim.setTarget(frontCard);
                backBtnAnim.start();
                frontAnim.start();
                isFront = true;
            }
        });
    }

    // Load data from database
    private void getData() {
        Cursor cursor = Users_DB.getFlashcards();
        if (cursor.getCount() == 0) {
            noTableText.setText("No Flashcard Folders");
            Toast.makeText(FlashcardActivity.this, "No db exist", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(4) == flashcardfolderID) {
                    myQuestions.add(cursor.getString(1));
                    myAnswers.add(cursor.getString(2));
                }
            }
        }
        cursor.close();
    }
}
