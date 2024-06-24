package com.example.fxo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationEventAdapter adapter;
    DatabaseHelper Users_DB;
    List<String> eventDetails;

    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Users_DB = new DatabaseHelper(this);
        eventDetails = new ArrayList<>();



        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotificationActivity.this, Nav.class);
                startActivity(i);
            }
        });
        recyclerView = findViewById(R.id.recyclerView_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationEventAdapter(this, eventDetails);
        recyclerView.setAdapter(adapter);

        // Load initial data
        getData();
    }

    // Method to fetch data from database and update RecyclerView
    private void getData() {
        eventDetails.clear();

        Cursor cursor = Users_DB.getUpcomingEvents();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);

                eventDetails.add("Event: " + title + "\n" + "Date: " + date + "\n" + "Time: " + time);
            }
            adapter.notifyDataSetChanged(); // Notify adapter of data change
        }
        cursor.close();
    }
}
