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
    DatabaseHelper userDB;
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
        userDB = new DatabaseHelper(this);

        show = findViewById(R.id.checkBox);
        signUp = findViewById(R.id.btnSUP);

        signUp.setOnClickListener(View ->{

            String user = userName.getText().toString();
            String pass = password.getText().toString();
            String fn = firstName.getText().toString();
            String ln = lastName.getText().toString();
            String bd = birthDate.getText().toString();
            String cn = contactNo.getText().toString();

            if(user.equals("") || pass.equals("") || fn.equals("") || ln.equals("") || bd.equals("") || cn.equals("")){
                Toast.makeText(this, "Put input in everything!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Boolean checkuser = userDB.checkusername(user);
                if(!checkuser){
                    Boolean insert = userDB.insertData(user, pass, fn, ln, bd, cn);
                    if(insert){
                        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show();

                        // random pot shit
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        String bckLogIn = "ECLIPSE";
                        intent.putExtra("LOGIN", bckLogIn);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "User already exist!", Toast.LENGTH_SHORT).show();
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
    public void refresh(){
        firstName.setText("");
        lastName.setText("");
        birthDate.setText("");
        contactNo.setText("");
        userName.setText("");
        password.setText("");
    }
}