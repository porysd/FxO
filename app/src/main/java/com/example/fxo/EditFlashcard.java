package com.example.fxo;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EditFlashcard extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    EditFlashcardAdapter editFlashcardAdapter;
    List<String> myQuestions;
    List<String> myAnswers;
    DatabaseHelper Users_DB;
    int fcFolderID, fcID;
    String fcFolderName;
    TextView fcFolderNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);
        recyclerView = findViewById(R.id.recycler);
        fcFolderNameText = findViewById(R.id.fcfoldernametext);

        Users_DB = new DatabaseHelper(this);

        myQuestions = new ArrayList<>();
        myAnswers = new ArrayList<>();

        fcFolderID = User.getInstance().getFlashcardFolderID();
        fcFolderName = User.getInstance().getFlashcardFolderTitle();
        fcFolderNameText.setText("fcfolderId: " + fcFolderID + " fcfolderName: " + fcFolderName);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        editFlashcardAdapter = new EditFlashcardAdapter(this, myQuestions, myAnswers, this);
        recyclerView.setAdapter(editFlashcardAdapter);

        getData();
    }
    private void getData() {
        Cursor cursor = Users_DB.getFlashcards();
        if (cursor.getCount() == 0) {
            fcFolderNameText.setText("No Flashcards");
            Toast.makeText(this, "No db exist", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(4) == fcFolderID) {
                    myQuestions.add(cursor.getString(1));
                    myAnswers.add(cursor.getString(2));
                }
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {

    }
}