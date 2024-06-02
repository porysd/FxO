package com.example.fxo;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignUP extends AppCompatActivity {
    DatabaseHelper Users_DB;
    TextView sUp, logIn;

    EditText firstName, lastName, birthDate, contactNo, userName, password;
    Button signUp;
    CheckBox show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        sUp = findViewById(R.id.sign_up);
        String rawAppName = getIntent().getStringExtra("SIGNUP");
        sUp.setText(rawAppName);

        userName = findViewById(R.id.usernames);
        password = findViewById(R.id.passwords);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        birthDate = findViewById(R.id.birthdates);
        contactNo = findViewById(R.id.phones);
        Users_DB = new DatabaseHelper(this);

        show = findViewById(R.id.checkBox);
        signUp = findViewById(R.id.btnSUP);

        signUp.setOnClickListener(View -> {
            String user = userName.getText().toString();
            String pass = password.getText().toString();
            String fn = firstName.getText().toString();
            String ln = lastName.getText().toString();
            String bd = birthDate.getText().toString();
            String cn = contactNo.getText().toString();

            if (user.isEmpty() || pass.isEmpty() || fn.isEmpty() || ln.isEmpty() || bd.isEmpty() || cn.isEmpty()) {
                Toast.makeText(this, "Put input in everything!!", Toast.LENGTH_SHORT).show();
            } else {
                Boolean checkuser = Users_DB.usernameAlreadyExisting(user);
                if (!checkuser) {
                    Boolean insert = Users_DB.insertUserData(user, pass, fn, ln, bd, cn);
                    if (insert) {
                        int userID = Users_DB.getUserID(user);
                        Users_DB.insertFolderData("Prelim", userID);
                        Users_DB.insertFolderData("Midterms", userID);
                        Users_DB.insertFolderData("Prefinals", userID);
                        Users_DB.insertFolderData("Finals", userID);

                        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LogIN.class);
                        String bckLogIn = "QUIZZLER";
                        intent.putExtra("LOGIN", bckLogIn);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        logIn = findViewById(R.id.log_in);

        logIn.setOnClickListener(View -> {
            Intent intent = new Intent (this, LogIN.class);

            String bckLogIn = "ECLIPSE";

            intent.putExtra("LOGIN", bckLogIn);
            startActivity(intent);
        });
    }
}