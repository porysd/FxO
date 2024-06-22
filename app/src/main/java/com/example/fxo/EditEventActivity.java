package com.example.fxo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditEventActivity extends AppCompatActivity {

    private EditText editEventName;
    private TextView editEventDate, editEventTime;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private String eventId;
    private int hour, min;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        dbHelper = new DatabaseHelper(this);

        editEventName = findViewById(R.id.editEventName);
        editEventDate = findViewById(R.id.editEventDate);
        editEventTime = findViewById(R.id.editEventTime);
        saveButton = findViewById(R.id.saveButton);

        eventId = getIntent().getStringExtra("eventId");

        loadEventDetails(eventId);

        initDatePicker();

        editEventDate.setOnClickListener(v -> datePickerDialog.show());

        editEventTime.setOnClickListener(v -> showReminderDialog());

        saveButton.setOnClickListener(v -> saveEventDetails());
    }

    private void loadEventDetails(String eventId) {
        Cursor cursor = dbHelper.getEventById(eventId);
        if (cursor.moveToFirst()) {
            editEventName.setText(cursor.getString(cursor.getColumnIndex("eventName")));
            editEventDate.setText(cursor.getString(cursor.getColumnIndex("eventDate")));
            editEventTime.setText(cursor.getString(cursor.getColumnIndex("eventReminder")));
        }
        cursor.close();
    }

    private void saveEventDetails() {
        String eventName = editEventName.getText().toString();
        String eventDate = editEventDate.getText().toString();
        String eventTime = editEventTime.getText().toString();

        if (eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("eventName", eventName);
        values.put("eventDate", eventDate);
        values.put("eventReminder", eventTime);

        dbHelper.updateEvent(eventId, values);

        Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            editEventDate.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + "/" + day + "/" + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    private void showReminderDialog() {
        TimePickerDialog tpd = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, hourOfDay, minute) -> {
                    hour = hourOfDay;
                    min = minute;

                    String time = hour + ":" + min;
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                    try {
                        Date date = timeFormat.parse(time);
                        SimpleDateFormat time12hrs = new SimpleDateFormat("hh:mm aa");
                        editEventTime.setText(time12hrs.format(date));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }, 12, 0, false);
        tpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tpd.updateTime(hour, min);
        tpd.show();
    }
}
