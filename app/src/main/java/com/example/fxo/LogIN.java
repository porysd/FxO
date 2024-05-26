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
    DatabaseHelper userDB;
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

        userDB = new DatabaseHelper(this);

        logIN = findViewById(R.id.login_button);
        username = findViewById(R.id.usernames);
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
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if(user.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Put input in username and password!!", Toast.LENGTH_SHORT).show();
            }
            else{
                boolean checkuserpass = userDB.checkusernamepassword(user,pass);
                if(checkuserpass){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Nav.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
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