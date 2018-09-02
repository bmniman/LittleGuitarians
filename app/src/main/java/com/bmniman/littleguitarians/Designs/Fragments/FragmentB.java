package com.bmniman.littleguitarians.Designs.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bmniman.littleguitarians.Database.DB;
import com.bmniman.littleguitarians.R;
import com.bmniman.littleguitarians.trying;


public class FragmentB extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view  = inflater.inflate(R.layout.fragment_fragment_b, container, false);

        Button b1 = view.findViewById(R.id.btn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), trying.class));
            }
        });


        return view;
    }

}
