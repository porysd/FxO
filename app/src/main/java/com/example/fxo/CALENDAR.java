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

public class CALENDAR extends Fragment {

    RecyclerView recyclerView;
    UpcomingEventAdapter adapter;
    DatabaseHelper dbHelper;
    CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_a_l_e_n_d_a_r, container, false);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView_upcoming_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Cursor cursor = dbHelper.getUpcomingEvents();
        adapter = new UpcomingEventAdapter(getContext(), cursor);
        recyclerView.setAdapter(adapter);

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
}
