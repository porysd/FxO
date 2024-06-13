package com.example.fxo;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {
    // UI components
    TextView noTableText;
    TextView frontCard;
    TextView backCard;
    TextView fcName;
    Button addFlashcardBtn, nextBtn, prevBtn, flip, backToQuestion, easyBtn;

    ImageButton backBtn;

    // Database helper
    DatabaseHelper Users_DB;

    // FlashcardActivity data
    List<String> myQuestions;
    List<String> myAnswers;
    int flashcardfolderID, index;
    String flashcardTitle;

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
        backToQuestion = findViewById(R.id.back_to_question);
        easyBtn = findViewById(R.id.easy_btn);
        fcName = findViewById(R.id.fcName);

        // Initialize data lists
        myQuestions = new ArrayList<>();
        myAnswers = new ArrayList<>();
        prevBtn.setEnabled(false);

        // Initialize database helper and get flashcard folder ID
        Users_DB = new DatabaseHelper(this);
        flashcardfolderID = User.getInstance().getFlashcardFolderID();

        flashcardTitle = User.getInstance().getFlashcardFolderTitle();

        fcName.setText(flashcardTitle);

        getData();



        backBtn.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardActivity.this, FlashcardFolderActivity.class);
            startActivity(i);
        });

        // Set event listeners
        addFlashcardBtn.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardActivity.this, AddFlashcardActivity.class);
            startActivity(i);
        });

        nextBtn.setVisibility(View.GONE);
        prevBtn.setVisibility(View.GONE);
        easyBtn.setVisibility(View.GONE);
        backToQuestion.setVisibility(View.GONE);

        backToQuestion.setOnClickListener(view -> {

            flip.setVisibility(View.VISIBLE);

            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);

            if (!isFront) {
                frontAnim.setTarget(backCard);
                backBtnAnim.setTarget(frontCard);
                frontAnim.start();
                backBtnAnim.start();
                isFront = true;
            }
        });

        easyBtn.setOnClickListener(view -> {

        });


        nextBtn.setOnClickListener(v -> {

            flip.setVisibility(View.VISIBLE);

            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);

            if (isFront) {
                index++;
                if (index < myQuestions.size()) {
                    frontCard.setText(myQuestions.get(index));
                    backCard.setText(myAnswers.get(index));
                    prevBtn.setEnabled(true);
                    nextBtn.setEnabled(index < myQuestions.size() - 1);
                }
            } else {
                frontAnim.setTarget(backCard);
                backBtnAnim.setTarget(frontCard);
                frontAnim.start();
                backBtnAnim.start();
                isFront = true;
                nextBtn.performClick();
            }
        });
        prevBtn.setOnClickListener(v -> {

            flip.setVisibility(View.VISIBLE);

            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);

            if (isFront) {
                index--;
                if (index >= 0) {
                    frontCard.setText(myQuestions.get(index));
                    backCard.setText(myAnswers.get(index));
                    nextBtn.setEnabled(true);
                    prevBtn.setEnabled(index > 0);
                }
            } else {
                frontAnim.setTarget(backCard);
                backBtnAnim.setTarget(frontCard);
                frontAnim.start();
                backBtnAnim.start();
                isFront = true;
                prevBtn.performClick();
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

            flip.setVisibility(View.GONE);

            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            easyBtn.setVisibility(View.VISIBLE);
            backToQuestion.setVisibility(View.VISIBLE);

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
