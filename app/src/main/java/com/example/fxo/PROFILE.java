package com.example.fxo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;

public class PROFILE extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;

    ImageButton backBtn;
    TextView fullname, username, name, contact, birthdate, settings, hs, logout;
    ImageView profileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_p_r_o_f_i_l_e, container, false);
        backBtn = view.findViewById(R.id.back_btn);
        fullname = view.findViewById(R.id.pfFULLNAME);
        username = view.findViewById(R.id.pfUSERNAME);
        name = view.findViewById(R.id.pfName);
        contact = view.findViewById(R.id.pfContact);
        birthdate = view.findViewById(R.id.pfBirthdate);
        logout = view.findViewById(R.id.pfLogOUT);
        profileImage = view.findViewById(R.id.profile_image);

        fullname.setText(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
        username.setText("@" + User.getInstance().getUserName());
        name.setText("NAME: " + User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
        contact.setText("CONTACT: " + User.getInstance().getContactNo());
        birthdate.setText("BIRTHDATE: " + User.getInstance().getBirthDate());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Nav.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        return view;
    }

    private void logoutUser() {
        // Clear the user session (assuming you store it in SharedPreferences)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
        startActivity(intent);
        getActivity().finish(); // Close the current activity
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    profileImage.setImageBitmap(bitmap);
                    // Save the image URI or bitmap to persist the profile picture change
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
