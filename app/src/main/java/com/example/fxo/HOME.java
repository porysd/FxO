package com.example.fxo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HOME extends Fragment implements RecyclerViewInterface {
    RecyclerView recyclerView;
    FolderAdapter flashcardfolderAdapter;
    List<String> myFolder;
    List<Integer> myFolderID;
    DatabaseHelper Users_DB;
    TextView textView2;
    int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h_o_m_e, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.recycler);
        textView2 = view.findViewById(R.id.textView2);
        userID = getActivity().getIntent().getIntExtra("USERID", 0);
        textView2.setText("Users Account: " + userID);

        // Initialize database helper and lists
        Users_DB = new DatabaseHelper(getActivity());
        myFolder = new ArrayList<>();
        myFolderID = new ArrayList<>();

        // Set up RecyclerView
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        flashcardfolderAdapter = new FolderAdapter(getActivity(), myFolder, this);
        recyclerView.setAdapter(flashcardfolderAdapter);

        // Fetch data from database
        getData();

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
        i.putExtra("FOLDERID", myFolderID.get(position));
        i.putExtra("FOLDERNAME", myFolder.get(position));
        i.putExtra("USERID", userID);
        startActivity(i);
    }
}
