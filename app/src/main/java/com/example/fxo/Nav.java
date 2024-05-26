package com.example.fxo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fxo.databinding.ActivityNavBinding;

public class Nav extends AppCompatActivity {

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

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main,fragment);
        ft.commit();
    }
}