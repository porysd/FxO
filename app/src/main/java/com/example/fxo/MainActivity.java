package com.example.fxo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button getStarted;
    TextView tvTT, tvST, tvDC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getStarted = (Button) findViewById(R.id.buttonGetStarted);

        tvTT = findViewById(R.id.textViewTitle);
        Typeface title = ResourcesCompat.getFont(this, R.font.poppins_bold);
        tvTT.setTypeface(title);
        getStarted.setTypeface(title);

        tvST = findViewById(R.id.textViewSubtitle);
        Typeface subTitle = ResourcesCompat.getFont(this, R.font.josefin_sans_bold);
        tvST.setTypeface(subTitle);

        tvDC = findViewById(R.id.textViewDescription);
        Typeface description = ResourcesCompat.getFont(this, R.font.varela_round);
        tvDC.setTypeface(description);

        getStarted.setOnClickListener(View -> {
            Intent intent = new Intent(this, LogIN.class);

            String login = "ECLIPSE";

            intent.putExtra("LOGIN", login);
            startActivity(intent);
        });
    }
}