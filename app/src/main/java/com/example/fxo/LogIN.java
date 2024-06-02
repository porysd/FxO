package com.example.fxo;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LogIN extends AppCompatActivity {

    DatabaseHelper Users_DB;
    TextView appName, signUp;
    Button logIn;
    EditText username, password;
    CheckBox showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        // Initialize UI components
        appName = findViewById(R.id.textView);
        logIn = findViewById(R.id.login_button);
        username = findViewById(R.id.usernames);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.sign_up_link);
        showPassword = findViewById(R.id.show_password);

        // Initialize DatabaseHelper
        Users_DB = new DatabaseHelper(this);

        // Handle sign up link click
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUP.class);
            String signup = "QUIZZLER";
            intent.putExtra("SIGNUP", signup);
            startActivity(intent);
        });

        // Handle login button click
        logIn.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                Integer userID = Users_DB.getUserIDIfMatch(user, pass);
                if (userID != null) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Nav.class);
                    i.putExtra("USERID", userID); // Pass the userId to the next activity
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Show password in checkbox
        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }
}