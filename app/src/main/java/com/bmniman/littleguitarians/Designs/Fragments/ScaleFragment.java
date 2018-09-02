package com.bmniman.littleguitarians.Designs.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import com.bmniman.littleguitarians.R;

import me.grantland.widget.AutofitHelper;
import me.grantland.widget.AutofitTextView;


public class ScaleFragment extends Fragment {



    ListFragmentListener activityCommander;
    String [] scaleNames = {"Major" , "Minor" , "Major Pentatonic" , "Minor Pentatonic" ,"Dorian" };// Put The Scale/ Names Here ANd Their Values in the getter and setter and it drawfretboard class set the switch case
    AutofitTextView btn[];




    public ScaleFragment() {
        // Required empty public constructor
    }

    public interface ListFragmentListener
    {
        void designComplete(String scale , String key);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try
        {
            activityCommander = (ListFragmentListener) context; //  To know that activitycommander is not empty
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_scale, container, false);
        return  view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startDesign();
    }

    public void startDesign() {

        LinearLayout linearLayout = getView().findViewById(R.id.linearlayout);

        int count = 0; // To set the index of the button

        btn = new AutofitTextView[scaleNames.length];

        for (String scaleName : scaleNames) {


            btn[count] = new AutofitTextView(getActivity());

            AutofitHelper.create(btn[count]);

            int width = (int) getResources().getDimension(R.dimen.scaleWidth);
            int heigth = (int) getResources().getDimension(R.dimen.scaleHeight);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, heigth);
            layoutParams.setMargins(0, 3, 0, 5); // left , top , right ,bottom

            btn[count].setLayoutParams(layoutParams);
            btn[count].setBackgroundResource(R.drawable.circular_textview);
            btn[count].setBackground(getResources().getDrawable(R.drawable.curved_guitar));
            btn[count].setId(count); // Set id to the count. Meaning number of buttons




            // Text Settings
            btn[count].setText(scaleName);  // Set Text of the button
            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/AccidentalPresidency.ttf");
            btn[count].setTypeface(custom_font);
            btn[count].setTextSize(30);
            btn[count].setMinTextSize(18);
            btn[count].setMaxLines(2);

            btn[count].setEllipsize(TextUtils.TruncateAt.END);

            btn[count].setTextColor(Color.WHITE);
            btn[count].setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            btn[count].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);



            btn[count].setPadding(5, 0, 5, 5);


            btn[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // Set onclick listener for every button


                     AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


                    AutofitTextView b = (AutofitTextView) view;
                    b.startAnimation(buttonClick);
                    activityCommander.designComplete(b.getText().toString(), ""); // Pass Name Of The SCale That is clicked and key as the parameter of the key.

                }
            });

            btn[count].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;


                }
            });
            linearLayout.addView(btn[count], layoutParams);
            count++;
        }


    }
}
