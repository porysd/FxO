package com.example.fxo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HOME extends Fragment implements RecyclerViewInterface, FlashcardHomeInterface{
    RecyclerView setView, flashview;
    FolderAdapter flashcardfolderAdapter;
    FlashcardHomeAdapter flashcardHomeAdapter;
    List<String> myFolder;
    List<String> myFlashcardFolderName, myFlashcardFolderTitle;
    List<Integer> myFlashcardFolderID;
    List<Integer> myFolderID;
    DatabaseHelper Users_DB;
    TextView textView2, seeAll;
    ImageView notify;
    int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h_o_m_e, container, false);

        // Initialize UI components
        setView = view.findViewById(R.id.recycler);
        textView2 = view.findViewById(R.id.textView2);
        flashview = view.findViewById(R.id.flashview);
        seeAll = view.findViewById(R.id.seeAll);
        notify = view.findViewById(R.id.notify);

        notify.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        seeAll.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), FlashcardFolderActivity.class);
            startActivity(i);
        });

        userID = User.getInstance().getUserID();
        textView2.setText("Users Account: " + userID);
        Users_DB = new DatabaseHelper(getActivity());
        myFolder = new ArrayList<>();
        myFolderID = new ArrayList<>();
        myFlashcardFolderName = new ArrayList<>();
        myFlashcardFolderID = new ArrayList<>();
        myFlashcardFolderTitle = new ArrayList<>();

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        setView.setLayoutManager(lm);
        flashcardfolderAdapter = new FolderAdapter(getActivity(), myFolder, this);
        setView.setAdapter(flashcardfolderAdapter);

        LinearLayoutManager lm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        flashview.setLayoutManager(lm1);
        flashcardHomeAdapter = new FlashcardHomeAdapter(getActivity(), myFlashcardFolderName, myFlashcardFolderID,this);
        flashview.setAdapter(flashcardHomeAdapter);

        getData();
        fetchRecentFlashcardFolders();  // Fetch recent flashcard folders and update the adapter


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

    private void fetchRecentFlashcardFolders() {
        int limit = 5; // Number of recent folders to fetch
        Cursor cursor = Users_DB.getRecentFlashcardFolders(userID, limit);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No recent flashcard folders found", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (cursor.moveToNext()) {
                int flashcardFolderID = cursor.getInt(0);
                String title = cursor.getString(1);
                String subject = cursor.getString(2);
                String dateCreated = cursor.getString(3);

                myFlashcardFolderID.add(flashcardFolderID);
                myFlashcardFolderTitle.add(title);
                myFlashcardFolderName.add(title + "\n\n\n(" + dateCreated + ")");

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

    @Override
    public void onFlashcardFolderItemClick(int position) {
        Intent i = new Intent(getActivity(), FlashcardActivity.class);
        User.getInstance().setFlashcardFolderID(myFlashcardFolderID.get(position));
        User.getInstance().setFlashcardFolderTitle(myFlashcardFolderTitle.get(position));
        int folderID = Users_DB.getFolderIDByFlashcardFolderID(myFlashcardFolderID.get(position));
        String folderName = Users_DB.getFolderNameByFolderID(folderID);
        User.getInstance().setFolderID(folderID);
        User.getInstance().setFolder(folderName);
        startActivity(i);
    }
}