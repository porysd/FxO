package com.example.fxo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;

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
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateShowCustomDialog();
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
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUP.this, LogIN.class);
                String bckLogIn = "ECLIPSE";
                intent.putExtra("LOGIN", bckLogIn);
                startActivity(intent);
            }
        });
    }

    private boolean isValidContactNo(String contact) {
        String contactPattern = "^\\d{11}$"; // ^ - start , \\d - digit , {11} - 11 required digit , $ - end
        return Pattern.matches(contactPattern, contact); // .matches() - to match an entire string pattern, returns a bool value if it matches or not.
    }   //contactPattern - represents the regex expression to match in 'contact'
    //contact - a string to match to our regex pattern of 'contactPattern'

    private boolean isValidBirthDate(String birthDate) {
        String birthDatePattern = "^\\d{2}/\\d{2}/\\d{4}$"; // same explanation in isValidContactNo
        return Pattern.matches(birthDatePattern, birthDate); // same explanation of regex pattern in isValidContactNo
    }

    private void ValidateShowCustomDialog() { // i made this method because showCustomDialog after i tested without this method it shows the dialog first of welcome shit then the toast pops up example is 'invalid contact number'. so i made this so that the last dialog will be called up after done checking all the inputs, contact no, and birthdate.
        String user = userName.getText().toString().trim(); // .trim() - a method to 'remove whitespace'
        String pass = password.getText().toString().trim();
        String fn = firstName.getText().toString().trim();
        String ln = lastName.getText().toString().trim();
        String bd = birthDate.getText().toString().trim();
        String cn = contactNo.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty() || fn.isEmpty() || ln.isEmpty() || bd.isEmpty() || cn.isEmpty()) {
            Toast.makeText(SignUP.this, "Put input in everything!!", Toast.LENGTH_SHORT).show();
        } else if (!isValidContactNo(cn)) {
            Toast.makeText(SignUP.this, "Invalid contact number.", Toast.LENGTH_SHORT).show();
        } else if (!isValidBirthDate(bd)) {
            Toast.makeText(SignUP.this, "Invalid Birthdate. Format MM/DD/YYYY", Toast.LENGTH_SHORT).show();
        } else {
            showCustomDialog(); // call this method after the validation is done, to make sure that the dialog will pop up at last when registered.
        }
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogforsignup);

        TextView title = dialog.findViewById(R.id.dialog_title);
        TextView message = dialog.findViewById(R.id.dialog_message);
        TextView dialogButton = dialog.findViewById(R.id.dialog_button);

        title.setText("WELCOME TO ECLIPSE, " + userName.getText().toString() + "!");
        message.setText("Your journey to better learning and planning begings here.");

        SpannableString sht = new SpannableString("Let's Get Started!");
        sht.setSpan(new UnderlineSpan(), 0, sht.length(),0);
        dialogButton.setText(sht);

        //align the message and title in the dialog....
        //fix the message and title ....

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String fn = firstName.getText().toString().trim();
                String ln = lastName.getText().toString().trim();

                String bd = birthDate.getText().toString().trim();
                String cn = contactNo.getText().toString().trim();


                if (user.isEmpty() || pass.isEmpty() || fn.isEmpty() || ln.isEmpty() || bd.isEmpty() || cn.isEmpty()) {
                    Toast.makeText(SignUP.this, "Put input in everything!!", Toast.LENGTH_SHORT).show();
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


                            Toast.makeText(SignUP.this, "Register Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUP.this, Nav.class);
                            User.getInstance().setUserID(userID);

                            startActivity(i);
                        } else {
                            Toast.makeText(SignUP.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUP.this, "User already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
