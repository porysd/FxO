package com.example.fxo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LIBRARY extends Fragment implements RecyclerViewInterface {
    RecyclerView recyclerView;
    LibraryFolderAdapter flashcardfolderAdapter;

    FlashcardHomeAdapter flashcardHomeAdapter;

    List<String> myTitle;
    List<String> myFolder;
    List<Integer> myFolderID;
    DatabaseHelper Users_DB;
    TextView textView2;

    ImageView notify;
    int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_l_i_b_r_a_r_y, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.recycler);
        textView2 = view.findViewById(R.id.textView2);

        userID = User.getInstance().getUserID();
        textView2.setText("Users Account: " + userID);



        notify = view.findViewById(R.id.notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to open NotificationActivity
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        // Initialize database helper and lists
        Users_DB = new DatabaseHelper(getActivity());
        myFolder = new ArrayList<>();
        myFolderID = new ArrayList<>();

        // Set up RecyclerView
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        flashcardfolderAdapter = new LibraryFolderAdapter(getActivity(), myFolder, this);
        recyclerView.setAdapter(flashcardfolderAdapter);

        getData();


        // FIX THE ARRAY LIST IN THE RECENT FLASHCARD!
        return view;
    }

    // Method to fetch data from database
    private void getData() {
        Cursor cursor = Users_DB.getFolders();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No db exists", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                if(cursor.getInt(2) == userID){
                    myFolderID.add(cursor.getInt(0));
                    myFolder.add(cursor.getString(1));
                }
            }
        }
        cursor.close();
    }

    // RecyclerView item click listener
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(getActivity(), FlashcardFolderActivity.class);
        User.getInstance().setFolderID(myFolderID.get(position));
        User.getInstance().setFolder(myFolder.get(position));
        startActivity(i);
    }
}