package com.example.fxo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fxo.databinding.ActivityNavBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Nav extends AppCompatActivity {

    FloatingActionButton fab;
    int folderID, userID;
    String folderName;
    @NonNull ActivityNavBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HOME());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.btnhome){
                replaceFragment(new HOME());
            } else if (itemId == R.id.btnfolder){
                replaceFragment(new LIBRARY());
            } else if (itemId == R.id.btncalendar){
                replaceFragment(new CALENDAR());
            } else if (itemId == R.id.btnprofile){
                replaceFragment(new PROFILE());
            }

            return true;
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call method to show dialog
                showDialog();
            }
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main,fragment);
        ft.commit();
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout fc = dialog.findViewById(R.id.Flashcards);
        LinearLayout e = dialog.findViewById(R.id.Events);

        folderID = getIntent().getIntExtra("FOLDERID", 0);
        folderName = getIntent().getStringExtra("FOLDERNAME");
        userID = User.getInstance().getUserID();
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(Nav.this, AddFlashcardFolderActivity.class);
                i.putExtra("FOLDERID", folderID);
                i.putExtra("FOLDERNAME", folderName);
                i.putExtra("USERID", userID);
                startActivity(i);
                Toast.makeText(Nav.this, "Flashcards", Toast.LENGTH_SHORT).show();

            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(Nav.this, "Events", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        // Set dialog parameters (width, height, background, animations, gravity) as needed
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}