package com.example.fxo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fxo.databinding.ActivityNavBinding;

public class Nav extends AppCompatActivity {

    private ActivityNavBinding binding;
    private boolean isProfileFragmentVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        // Initialize UI components
        navigationBar();

        // Initially load the HOME fragment
        replaceFragment(new HOME());
    }

    private void navigationBar() {
        binding.bottomAppBar.setVisibility(View.VISIBLE);
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        binding.fab.setVisibility(View.VISIBLE);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.btnhome) {
                replaceFragment(new HOME());
            } else if (itemId == R.id.btnfolder) {
                replaceFragment(new LIBRARY());
            } else if (itemId == R.id.btncalendar) {
                replaceFragment(new CALENDAR());
            } else if (itemId == R.id.btnprofile) {
                replaceFragment(new PROFILE());
            }
            return true;
        });

        binding.fab.setOnClickListener(view -> showDialog());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(binding.main.getId(), fragment);
        ft.commit();

        // Update UI visibility based on current fragment
        if (fragment instanceof PROFILE) {
            isProfileFragmentVisible = true;
            hideNavigationUI();
        } else {
            isProfileFragmentVisible = false;
            showNavigationUI();
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.bottomsheetlayout);

        dialog.findViewById(R.id.Flashcards).setOnClickListener(v -> {
            dialog.dismiss();
            Intent i = new Intent(Nav.this, AddFlashcardFolderActivity.class);
            startActivity(i);
            Toast.makeText(Nav.this, "Flashcards", Toast.LENGTH_SHORT).show();
        });

        dialog.findViewById(R.id.Events).setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(Nav.this, "Events", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void hideNavigationUI() {
        binding.bottomAppBar.setVisibility(View.GONE);
        binding.bottomNavigationView.setVisibility(View.GONE);
        binding.fab.setVisibility(View.GONE);
    }

    private void showNavigationUI() {
        binding.bottomAppBar.setVisibility(View.VISIBLE);
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        binding.fab.setVisibility(View.VISIBLE);
    }
}
