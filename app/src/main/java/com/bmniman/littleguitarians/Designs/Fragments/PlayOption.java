package com.bmniman.littleguitarians.Designs.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.bmniman.littleguitarians.Designs.Exercise_Selection;
import com.bmniman.littleguitarians.Designs.MainActivity;
import com.bmniman.littleguitarians.Designs.ScaleList;
import com.bmniman.littleguitarians.R;
import com.github.florent37.tutoshowcase.TutoShowcase;

public class PlayOption extends Fragment {


    Button exercise,scale;
    public PlayOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_play_option, container, false);





        exercise = view.findViewById(R.id.exercise);
        scale = view.findViewById(R.id.scale);

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openActivity("exercise");
            }
        });

        scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity("scale");
            }
        });






        return  view;
    }

    public void openActivity(String play)
    {

        if(play.equals("exercise")){
            startActivity(new Intent(getContext() , Exercise_Selection.class));

        }
        else {


            Intent intent = new Intent(getContext(), ScaleList.class); // When a button is clicked call DrawFretboard activity
//            intent.putExtra("Type", "scale"); // Pass Exercise Name to the activity
            startActivity(intent); // CAll The Activity

        }
    }
//ScaleList


}









