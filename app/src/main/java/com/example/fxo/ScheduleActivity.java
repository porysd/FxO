package com.example.fxo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleActivity extends AppCompatActivity {
    EditText eventName;
    TextView eventDate, eventReminder;
     Button cancelButton, doneButton;
    DatePickerDialog datePickerDialog;
    ImageButton backBtn;
    DatabaseHelper db;
    int hour, min;
    int userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        db = new DatabaseHelper(this);

        eventName = findViewById(R.id.event_name);
        eventDate = findViewById(R.id.event_dates);
        eventReminder = findViewById(R.id.event_reminders);
        cancelButton = findViewById(R.id.cancel_button);
        doneButton = findViewById(R.id.done_button);
        backBtn = findViewById(R.id.goback);

        backBtn.setOnClickListener(view -> {
            finish();
        });

        initDatePicker();

        String selectedDate = getIntent().getStringExtra("selectedDate");

        if (selectedDate != null) {
            eventDate.setText(selectedDate);
        } else {
            eventDate.setText(getTodaysDate());
        }

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        eventReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReminderDialog();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = eventName.getText().toString();
                String date = eventDate.getText().toString();
                String reminder = eventReminder.getText().toString();
                userID = User.getInstance().getUserID();


                if (name.isEmpty() || date.isEmpty() || reminder.isEmpty()) {
                    Toast.makeText(ScheduleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    boolean isInserted = db.insertEventData(name, date, reminder, userID);
                    if (isInserted) {
                        Toast.makeText(ScheduleActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScheduleActivity.this, "Event creation failed", Toast.LENGTH_SHORT).show();
                    }
                }

                finish();
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                eventDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
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
        TimePickerDialog tpd = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;

                String time = hour + ":" + min;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                try {
                    Date date = timeFormat.parse(time);

                    SimpleDateFormat time12hrs = new SimpleDateFormat("hh:mm aa");
                    eventReminder.setText(time12hrs.format(date));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 12, 0, false);
        tpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tpd.updateTime(hour, min);
        tpd.show();
    }
}
