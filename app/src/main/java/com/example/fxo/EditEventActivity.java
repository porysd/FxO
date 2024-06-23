package com.example.fxo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    EditText editEventName;

    DatabaseHelper Users_DB;
    TextView editEventDate, editEventTime;
    Button saveButton, backButton;

    Calendar eventCalendar;

    int eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Initialize DatabaseHelper instance
        Users_DB = new DatabaseHelper(this);

        // Initialize views
        editEventName = findViewById(R.id.editEventNames);
        editEventDate = findViewById(R.id.editEventDates);
        editEventTime = findViewById(R.id.editEventTime);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        eventCalendar = Calendar.getInstance();

        // Retrieve event ID from intent extras
        eventId = getIntent().getIntExtra("eventId", -1);
        if (eventId == -1) {
            // Handle invalid event ID scenario
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchEventDetails();

        setupDateTimePickers();

        // Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvent();
            }
        });

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement back functionality (optional)
                finish(); // Close activity
            }
        });
    }

    // Method to fetch event details and populate UI fields
    private void fetchEventDetails() {

        String eventName = "Sample Event";
        String eventDate = "01/01/2025";
        String eventTime = "12:00 PM";

        // Populate UI fields
        editEventName.setText(eventName);
        editEventDate.setText(eventDate);
        editEventTime.setText(eventTime);
    }

    // Method to setup date and time pickers
    private void setupDateTimePickers() {
        // Date picker dialog
        editEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eventCalendar.set(Calendar.YEAR, year);
                                eventCalendar.set(Calendar.MONTH, monthOfYear);
                                eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateDateLabel();
                            }
                        },
                        eventCalendar.get(Calendar.YEAR),
                        eventCalendar.get(Calendar.MONTH),
                        eventCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        // Time picker dialog
        editEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                eventCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                eventCalendar.set(Calendar.MINUTE, minute);
                                updateTimeLabel();
                            }
                        },
                        eventCalendar.get(Calendar.HOUR_OF_DAY),
                        eventCalendar.get(Calendar.MINUTE),
                        false); // 24 hour format
                timePickerDialog.show();
            }
        });
    }

    // Method to update date label
    private void updateDateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEventDate.setText(sdf.format(eventCalendar.getTime()));
    }

    // Method to update time label
    private void updateTimeLabel() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEventTime.setText(sdf.format(eventCalendar.getTime()));
    }

    // Method to update event details
    private void updateEvent() {
        String eventName = editEventName.getText().toString().trim();
        String eventDate = editEventDate.getText().toString().trim();
        String eventTime = editEventTime.getText().toString().trim();

        // Validate inputs (optional)
        if (eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update event in database
        boolean updated = Users_DB.updateEvent(eventId, eventName, eventDate, eventTime);
        if (updated) {
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            notifyCalendarFragment();
            finish();
        } else {
            Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to notify CALENDAR fragment to refresh events
    private void notifyCalendarFragment() {
        // Get reference to CALENDAR fragment
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("CALENDAR_TAG");

        if (fragment != null && fragment instanceof CALENDAR) {
            ((CALENDAR) fragment).refreshEvents();
        }
    }

}
