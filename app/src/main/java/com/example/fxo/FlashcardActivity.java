package com.example.fxo;

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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FlashcardActivity extends AppCompatActivity {
    // UI components
    TextView noTableText;
    TextView frontCard;
    TextView backCard;
    TextView fcName, nofc;
    Button addFlashcardBtn, correctBtn, wrongBtn, flip, backToQuestion, easyBtn, retakebtn;

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
        wrongBtn = findViewById(R.id.wrong_btn);
        correctBtn = findViewById(R.id.correct_btn);
        backBtn = findViewById(R.id.back_btn);
        flip = findViewById(R.id.flip_btn);
        frontCard = findViewById(R.id.front_card);
        backCard = findViewById(R.id.back_card);
        backToQuestion = findViewById(R.id.back_to_question);
        easyBtn = findViewById(R.id.easy_btn);
        fcName = findViewById(R.id.fcName);
        retakebtn = findViewById(R.id.retake_btn);
        nofc = findViewById(R.id.nofc);

        // Initialize data lists
        myQuestions = new ArrayList<>();
        myAnswers = new ArrayList<>();

        // Initialize database helper and get flashcard folder ID
        Users_DB = new DatabaseHelper(this);
        flashcardfolderID = User.getInstance().getFlashcardFolderID();
        flashcardTitle = User.getInstance().getFlashcardFolderTitle();
        fcName.setText(flashcardTitle);

        getData();
        Toast.makeText(this, "q:" + myQuestions, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "a: "+ myAnswers, Toast.LENGTH_SHORT).show();

        correctBtn.setVisibility(View.GONE);
        wrongBtn.setVisibility(View.GONE);
        easyBtn.setVisibility(View.GONE);
        backToQuestion.setVisibility(View.GONE);

        retakebtn.setVisibility(View.GONE);
        nofc.setVisibility(View.GONE);

        frontCard.setVisibility(View.VISIBLE);
        backCard.setVisibility(View.GONE);

        index = 0;
        // Set initial flashcard data if available
        if (!myQuestions.isEmpty() && !myAnswers.isEmpty()) {
            frontCard.setText(myQuestions.get(index));
            backCard.setText(myAnswers.get(index));
        } else {
            wrongBtn.setEnabled(false);
            easyBtn.setEnabled(false);
            correctBtn.setEnabled(false);
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show();
        }

        // show answer
        flip.setOnClickListener(view -> {
            flip.setVisibility(View.GONE);
            wrongBtn.setVisibility(View.VISIBLE);
            correctBtn.setVisibility(View.VISIBLE);
            easyBtn.setVisibility(View.VISIBLE);
            backToQuestion.setVisibility(View.VISIBLE);
            frontCard.setVisibility(View.GONE);
            backCard.setVisibility(View.VISIBLE);
        });
        // moves the current flashcard to index 9
        correctBtn.setOnClickListener(v -> {
            flip.setVisibility(View.VISIBLE);
            wrongBtn.setVisibility(View.GONE);
            correctBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);
            String itemQ = myQuestions.remove(0);
            String itemA = myAnswers.remove(0);
            int itemCorrectMoveLocation = Math.min(9, myQuestions.size());
            myQuestions.add(itemCorrectMoveLocation, itemQ);
            myAnswers.add(itemCorrectMoveLocation, itemA);
            frontCard.setText(myQuestions.get(0));
            backCard.setText(myAnswers.get(0));
            // taga announce lng to check
            Toast.makeText(this, "q:" + myQuestions, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "a: "+ myAnswers, Toast.LENGTH_SHORT).show();
            wrongBtn.setEnabled(true);
            frontCard.setVisibility(View.VISIBLE);
            backCard.setVisibility(View.GONE);
        });
        // view the question again
        backToQuestion.setOnClickListener(view -> {
            flip.setVisibility(View.VISIBLE);
            wrongBtn.setVisibility(View.GONE);
            correctBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);

            frontCard.setVisibility(View.VISIBLE);
            backCard.setVisibility(View.GONE);
        });
        // taga remove ng current flashcard from the array
        easyBtn.setOnClickListener(view -> {
            wrongBtn.setVisibility(View.GONE);
            correctBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);
            flip.setVisibility(View.VISIBLE);
            myQuestions.remove(0);
            myAnswers.remove(0);
            if(myQuestions.isEmpty()){
                retakebtn.setVisibility(View.VISIBLE);
                nofc.setVisibility(View.VISIBLE);
                frontCard.setVisibility(View.GONE);
                backCard.setVisibility(View.GONE);
                flip.setVisibility(View.GONE);
            }
            else{
                frontCard.setText(myQuestions.get(0));
                backCard.setText(myAnswers.get(0));
                // taga announce lng to check
                Toast.makeText(this, "q:" + myQuestions, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "a: "+ myAnswers, Toast.LENGTH_SHORT).show();
                frontCard.setVisibility(View.VISIBLE);
                backCard.setVisibility(View.GONE);
            }
        });
        retakebtn.setOnClickListener(view -> {
            Intent i = getIntent();
            finish();
            startActivity(i);
        });
        // moves the current flashcard to index 4
        wrongBtn.setOnClickListener(v -> {
            flip.setVisibility(View.VISIBLE);
            wrongBtn.setVisibility(View.GONE);
            correctBtn.setVisibility(View.GONE);
            easyBtn.setVisibility(View.GONE);
            backToQuestion.setVisibility(View.GONE);

            String itemQ = myQuestions.remove(0);
            String itemA = myAnswers.remove(0);
            int itemCorrectMoveLocation = Math.min(4, myQuestions.size());
            myQuestions.add(itemCorrectMoveLocation, itemQ);
            myAnswers.add(itemCorrectMoveLocation, itemA);
            frontCard.setText(myQuestions.get(0));
            backCard.setText(myAnswers.get(0));
            // taga announce lng to check
            Toast.makeText(this, "q:" + myQuestions, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "a: "+ myAnswers, Toast.LENGTH_SHORT).show();
            frontCard.setVisibility(View.VISIBLE);
            backCard.setVisibility(View.GONE);
        });

        backBtn.setOnClickListener(v -> {
            finish();
        });
        addFlashcardBtn.setOnClickListener(v -> {
            Intent i = new Intent(FlashcardActivity.this, AddFlashcardActivity.class);
            startActivity(i);
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
            // turned the array of question and answers into a pair para mashuffle sila ng naka align parin sa isat isa
            List<Pair> flashcardPairs = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (cursor.getInt(4) == flashcardfolderID) {
                    flashcardPairs.add(new Pair(cursor.getString(1), cursor.getString(2)));
                }
            }
            // Shuffle the pairs
            Collections.shuffle(flashcardPairs, new Random(System.nanoTime()));

            // Hiniwala ung pair para individual array nalang ulit ung myQuestion and myAnswers
            for (Pair pair : flashcardPairs) {
                myQuestions.add(pair.question);
                myAnswers.add(pair.answer);
            }
        }
        cursor.close();
    }

    private static class Pair {
        String question;
        String answer;

        Pair(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }
}