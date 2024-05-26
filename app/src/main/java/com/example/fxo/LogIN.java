package com.example.fxo;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogIN extends AppCompatActivity {

    TextView appName, signUp;

    Button logIN;
    EditText username, password;
    CheckBox rememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        appName = findViewById(R.id.textView);
        String rawAppName = getIntent().getStringExtra("LOGIN");
        appName.setText(rawAppName);

        logIN = findViewById(R.id.login_button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        signUp = findViewById(R.id.sign_up_link);

        signUp.setOnClickListener(View -> {
            Intent intent = new Intent (this, SignUP.class);

            String signup = "ECLIPSE";

            intent.putExtra("SIGNUP", signup);
            startActivity(intent);

        });

        rememberMe = findViewById(R.id.remember_me);

        logIN.setOnClickListener(View -> {
            if (username.getText().toString().equals("user123") && password.getText().toString().equals("pass123")) {
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
    });
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }
}