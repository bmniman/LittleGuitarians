package com.bmniman.littleguitarians.Designs.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmniman.littleguitarians.Classes.MonthYear;
import com.bmniman.littleguitarians.Interfaces.MonthYearInterface;
import com.bmniman.littleguitarians.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChartsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BarChart chart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChartsFragment() {
        // Required empty public constructor
    }


    public static ChartsFragment newInstance(String param1, String param2) {
        ChartsFragment fragment = new ChartsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_charts, container, false);;
        chart = view.findViewById(R.id.chart);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAdded()) {
            new Charts().getValues(new ChartsInterface() {
                @Override
                public void process(int[] inCorrectNotesAvg, int[] correctNotesAvg, float[] date) {
                    super.process(inCorrectNotesAvg, correctNotesAvg, date);


                    Log.d("Ans" , "Iterated");

                    startChartDesign(inCorrectNotesAvg,correctNotesAvg,date);


                    for(int i= 1 ;i <31; i++)
                    {
                        Log.d("Ans" ,date[i] +  "  =  " + inCorrectNotesAvg[i]);
                    }


                }
            });
        }
    }

    private void startChartDesign(int[] inCorrectNotesAvg, int[] correctNotesAvg, float[] date)
    {

        String []temp;
        temp= floatToString(date);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(temp));
        xAxis.setGranularity(5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



        List<BarEntry> entries = new ArrayList<>();



        for(int i = 1 ; i<31; i++)
        {

            entries.add(new BarEntry(date[i], inCorrectNotesAvg[i]));
            Log.d("Datass", String.valueOf(inCorrectNotesAvg[i]));

        }

        BarDataSet set = new BarDataSet(entries, "Incorrect Notes");



        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        set.setDrawValues(false);

        chart.setData(data);

        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
//        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getXAxis().setDrawAxisLine(true);
        chart.setScaleEnabled(false);
        chart.setDragEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.animateY(3000, Easing.EasingOption.EaseOutBack);
        chart.animateY(3000, Easing.EasingOption.EaseInBounce);
        chart.invalidate(); // refresh









    }


    public String[] floatToString(float date[])
    {
        String [] temp = new String[32];
        for(int i=1 ; i<31 ;i++)
        {
            temp[i] = String.valueOf(date[i]);

        }
        return temp;
    }

    public String[] intToString(int date[])
    {
        String [] temp = new String[32];
        for(int i=1 ; i<31 ;i++)
        {
            temp[i] = String.valueOf(date[i]);

        }
        return temp;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

class ChartsInterface {
    public ChartsInterface() {

    }
    public void process(int[] inCorrectNotesAvg, int[] correctNotesAvg, float[] date) {

    }
}

class Charts {
    Date dbDate;
    private FirebaseUser user;
    private String monthYear;
    private Map<String, Object> values = new HashMap<>();
    private float[] date = new float[32];
    int[] avg;
    public int[] inCorrectNotesAvg = new int[32];
    public int[] correctNotesAvg = new int[32];

    void getValues(final ChartsInterface listener) {

        user = FirebaseAuth.getInstance().getCurrentUser();

        new MonthYear().getMonthYear(new MonthYearInterface() {
            @Override
            public void process(String monthYears) {
                super.process(monthYears);
                monthYear = monthYears; // This is done to get the current month and year . ex : Feb 2108 or Dec 2019 etc
            }
        });
        FirebaseFirestore.getInstance().collection("Users/" + user.getUid() + "/Detailed Reports/")
                .limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                values.putAll(document.getData());
                            }

                            Log.d("Values" , String.valueOf(values.get("23-2")));


                                fillDateArray(); // This function will fill the date array
                                fillValuesArray(); // This function will fill the values array
                                listener.process(inCorrectNotesAvg,correctNotesAvg,date);

                        } else {
                            Log.d("h", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void fillDateArray() // Index of Days Start From 1 And Not Zero
    {
        int count = 1;
        String months [] = {"Jan" , "Feb" , "March" , "April" , "May" , "June" , "July" , "Augst" , "Sept" , "Oct" , "Nov" ,"Dec"};
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH); // This to get todays date eg : 31, 1 ,4 ,5 etc
        int month  = calendar.get(Calendar.MONTH);

        while(count<=31 && day>0) // This loop will go till todays day and save the values in the date array
        {
            date[count] = day;
            day--;
            count++;
        }

        int prevDate = getLastMonthLastDate();

        while (count < 31) // The left out values will be taken from the previous month with the help of this loop
        {
            date[count] = prevDate;
            count++;
            prevDate--;

        }
    }



    private int getLastMonthLastDate() // This function will return the last date if the previous month eg : 31 , 30 ,28 etc
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.YEAR);
        calendar.set(Calendar.MONTH, -1);
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        Log.d("Days", String.valueOf(numDays));

        return numDays;
    }


    private void fillValuesArray() { // This function will get the data  received from databse of the last thirty days and store it in the array
        int count = 1;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH); // This to get todays date eg : 31, 1 ,4 ,5 etc
        int month = calendar.get(Calendar.MONTH) + 1; //  This will return the current month .  Eg  : For4 Januray   It Returns 0  and thus + 1is added

        while(count<=31 && day>0) // This loop will go till todays day and save the values in the date array
        {
            String tempString = (String) values.get(day + "-" + month);  // This will get the values the get value key example is  : 12-02. Where 12 Being the date and 2 being the month
            int[] tempArray;

            if (tempString == null) { // If no data is present for the particular date then make the data zero which means the user did not open the app on than day
                inCorrectNotesAvg[day] = correctNotesAvg[day] = 0;
            } else {
                tempArray = stringToArray(tempString); //  Convert the data got from the databse to String


                Log.d("Datas" , String.valueOf(tempArray[0]));

                int total = tempArray[0] + tempArray[1];

               try{
                   inCorrectNotesAvg[day] = ((tempArray[0] * 100) / total); // Finding the percent of incorrect notes played that day
                   correctNotesAvg[day] = ((tempArray[1] * 100) / total); // Finding the percent of correct notes played that day
               }
               catch (ArithmeticException e) // If divide by zero exceeption occurs
               {
                   inCorrectNotesAvg[day] = 0;
                   correctNotesAvg[day] = 0;
               }

                Log.d("Perc", String.valueOf(inCorrectNotesAvg[count]));

            }

            day--;
            count++;
        }

        Log.d("Counter" , String.valueOf(count));


        int prevDate = getLastMonthLastDate(); // This will store the last date of the previous month

        while (count < 31) // The left out values will be taken from the previous month with the help of this loop
        {
            String tempString = (String) values.get(prevDate + "-" + (month-1)); // Since we have to start from the last date prevDate is -- that is dcreased by one after each iteration
            int[] tempArray = new int[2];

            if (tempString == null) {
                inCorrectNotesAvg[prevDate] = correctNotesAvg[prevDate] = 0;
            } else {
                tempArray = stringToArray(tempString);

                Log.d("Datas" , String.valueOf(tempArray[0]));

                int total = tempArray[0] + tempArray[1];

                try{
                    inCorrectNotesAvg[prevDate] = ((tempArray[0] * 100) / total); // Finding the percent of incorrect notes played that day
                    correctNotesAvg[prevDate] = ((tempArray[1] * 100) / total); // Finding the percent of correct notes played that day
                }
                catch (ArithmeticException e) // If divide by zero exceeption occurs
                {
                    inCorrectNotesAvg[day] = 0;
                    correctNotesAvg[day] = 0;
                }

            }

            count++;
            prevDate--;
        }
    }

    private int[] stringToArray(String values) {
        String[] items = values.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        int[] notes = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                notes[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            }
        }

        return notes;

    }

}

class MyXAxisValueFormatter implements IAxisValueFormatter
{
    private String[] mValues;

    public MyXAxisValueFormatter(String[] values)
    {
        this.mValues=values;

    }

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value];
    }
}

