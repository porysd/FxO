package com.example.fxo;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class PROFILE extends Fragment {

    ImageButton backBtn;
    TextView fullname, username, name, contact, birthdate, settings, hs, logout;

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

        return view;
    }
}