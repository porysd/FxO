package com.example.fxo;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    List<Integer> myFlashcardID;
    DatabaseHelper Users_DB;
    int fcFolderID;
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
        myFlashcardID = new ArrayList<>();

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
                    myFlashcardID.add(cursor.getInt(0));
                    myQuestions.add(cursor.getString(1));
                    myAnswers.add(cursor.getString(2));
                }
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {
        User.getInstance().setFlashcardID(myFlashcardID.get(position));
        User.getInstance().setQuestion(myQuestions.get(position));
        User.getInstance().setAnswer(myAnswers.get(position));
        showEditDialog(position);
    }

    private void showEditDialog(int position){

        LinearLayout editLinearLayout = findViewById(R.id.editLinearLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_flashcard, editLinearLayout);
        Button back = view.findViewById(R.id.back_btn);
        Button save = view.findViewById(R.id.save_btn);
        Button delete = view.findViewById(R.id.delete_btn);
        EditText question = view.findViewById(R.id.questionedit);
        EditText answer = view.findViewById(R.id.answeredit);


        String flashcardID = String.valueOf(myFlashcardID.get(position));
        Toast.makeText(this, "position: " + position + "fcID: " + flashcardID, Toast.LENGTH_SHORT).show();
        question.setText(User.getInstance().getQuestion());
        answer.setText(User.getInstance().getAnswer());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        back.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        save.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(EditFlashcard.this);
            String q = question.getText().toString();
            String a = answer.getText().toString();
            db.updateFlashcard(flashcardID, q, a);
            alertDialog.dismiss();
            finish();
            startActivity(getIntent());
        });

        delete.setOnClickListener(v -> {
            confirmDelete(Integer.parseInt(flashcardID), position);
            alertDialog.dismiss();
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void confirmDelete(int flashcardID, int position) {
        Toast.makeText(this, "delete btn clicked ", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            boolean deleted = Users_DB.deleteFlashcardData(String.valueOf(flashcardID));
            if (deleted) {
                myFlashcardID.remove(position);
                myAnswers.remove(position);
                myQuestions.remove(position);
                Toast.makeText(this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                Users_DB.resetAutoIncrement();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "Failed to delete record", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}