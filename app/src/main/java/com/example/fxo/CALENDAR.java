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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CALENDAR extends Fragment {

    RecyclerView recyclerView;
    UpcomingEventAdapter adapter;
    DatabaseHelper Users_DB;
    CalendarView calendarView;

    List<String> eventFolder;
    List<Integer> eventID;
    int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_a_l_e_n_d_a_r, container, false);

        userID = User.getInstance().getUserID();
        Users_DB = new DatabaseHelper(getActivity());
        eventFolder = new ArrayList<>();
        eventID = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView_upcoming_events);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        adapter = new UpcomingEventAdapter(getActivity(), eventFolder, eventID);
        recyclerView.setAdapter(adapter);

        // Load initial data
        getData();

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                intent.putExtra("selectedDate", (month + 1) + "/" + dayOfMonth + "/" + year);
                startActivity(intent);
            }
        });

        return view;
    }

    // Method to fetch data from database and update RecyclerView
    private void getData() {
        eventFolder.clear();
        eventID.clear();

        Cursor cursor = Users_DB.getUpcomingEvents();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No events found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(4) == userID) {
                    eventID.add(cursor.getInt(0));
                    String title = cursor.getString(1);
                    String date = cursor.getString(2);
                    String time = cursor.getString(3);

                    eventFolder.add("Event: " + title + "\n" + "Date: " + date + "\n" + "Time: " + time);



                }
            }
            adapter.notifyDataSetChanged(); // Notify adapter of data change
        }
        cursor.close();
    }

    // Call this method whenever a new event is added to refresh the RecyclerView
    public void refreshEvents() {
        getData();
    }
}
