package com.example.fxo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUP extends AppCompatActivity {

    TextView sUp, logIn;
    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        sUp = findViewById(R.id.sign_up);
        String rawAppName = getIntent().getStringExtra("SIGNUP");
        sUp.setText(rawAppName);

        signUp = findViewById(R.id.btnSUP);

        signUp.setOnClickListener(View ->{

            Intent intent = new Intent(this, LogIN.class);

            String bckLogIn = "QUIZZLER";

            intent.putExtra("LOGIN", bckLogIn);
            startActivity(intent);

        });
        logIn = findViewById(R.id.log_in);

        logIn.setOnClickListener(View -> {
            Intent intent = new Intent (this, LogIN.class);

            String bckLogIn = "QUIZZLER";

            intent.putExtra("LOGIN", bckLogIn);
            startActivity(intent);

        });

    }
}