package com.bmniman.littleguitarians.Database.SendData;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.bmniman.littleguitarians.Classes.DetailedReports;
import com.bmniman.littleguitarians.Classes.MonthYear;
import com.bmniman.littleguitarians.Classes.OverviewReports;
import com.bmniman.littleguitarians.Classes.UnlockedExercises;
import com.bmniman.littleguitarians.Interfaces.MonthYearInterface;
import com.bmniman.littleguitarians.Interfaces.DetailedReportsInterface;
import com.bmniman.littleguitarians.Interfaces.OverviewReportsInterface;
import com.bmniman.littleguitarians.Interfaces.UnlockedExerciseInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SaveDataService extends IntentService {

    int incorrectNotes = 0;
    int correctNotes = 0;
    FirebaseUser user;
    DocumentReference detailRef , overviewRef , unLockedExerRef;
    String monthYear;
    int currTymSpent;
    int count=0;

    String exerName;


    // For Overview Reports
    int  overviewIncNotes = 0;
    int overviewCorrNotes= 0;

    // For Detailed Reports
    int detailIncNotes = 0;
    int detailCorrNotes=0;


    public SaveDataService() {
        super("SaveDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {


        Log.d("Tag" , "Service Started");

       incorrectNotes = intent.getIntExtra("Incorrect Perform Notes" , incorrectNotes);
       Log.d("Tag" , String.valueOf(incorrectNotes));

       correctNotes = intent.getIntExtra("Correct Perform Notes" , correctNotes);

       currTymSpent = intent.getIntExtra("Tym Spent" , currTymSpent);

       exerName  = intent.getStringExtra("Exercise Name");

        user = FirebaseAuth.getInstance().getCurrentUser();


        Log.d("Service" , "Time = " + currTymSpent + " Incorrect Notes = " + incorrectNotes);

        if(!exerName.equals(""))
        {
            unLockExercise();
        }


        addPreviousData();
    }

    private void unLockExercise()
    {
        new UnlockedExercises().getExercise(new UnlockedExerciseInterface()
        {
            @Override
            public void process(StringBuilder exerNames) {
                super.process(exerNames);

                exerNames.append(",");
                exerNames.append(exerName);




                Map<String, Object> unLockData = new HashMap<>();

                unLockedExerRef  = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Profile Details/Data");


                unLockData.put("Unlocked Exercises" , exerNames.toString());

                unLockedExerRef
                        .set(unLockData,SetOptions.merge());




            }
        });
    }


    public void mergeData() // Convert Data to String , So that it can fit into a single field
    {
        count++;
        Log.d("Return" , Integer.toString(incorrectNotes) + Integer.toString(correctNotes));
        String detailed =  Integer.toString(detailIncNotes) + ","  + Integer.toString(detailCorrNotes);

        String overview =  Integer.toString(overviewIncNotes) + ","  + Integer.toString(overviewCorrNotes);

        Log.d("Overview" , overview);



        if(count == 2) // Wait For both overview and detailed reports to get the previous data and then save the data to database
        {
            saveToDB(detailed , overview);
            count=0;
        }

    }

    public void addPreviousData()
    {
        new DetailedReports().getValues(new DetailedReportsInterface()
        {
            @Override
            public void process(int[] values) {
                super.process(values);

               if(values!=null)
               {


                   detailIncNotes = values[0] + incorrectNotes;
                   detailCorrNotes= values[1] + correctNotes;



                   mergeData();
               }
               else {

                   mergeData();
               }

            }
        });


        new OverviewReports().getValues(new OverviewReportsInterface()
        {
            @Override
            public void process(int[] values, long tymSpent) {
                super.process(values, tymSpent);
                if(values!=null)
                {

                    overviewIncNotes = values[0] + incorrectNotes;
                    overviewCorrNotes = values[1] + correctNotes;

                    Log.d("Service" , "Previous Incorrect Is " + values[0]);

                    currTymSpent+=tymSpent;

                    mergeData();
                }
                else {

                    mergeData();
                }
            }
        });

    }


    public void saveToDB(String detailed , String overview)
    {


        new MonthYear().getMonthYear(new MonthYearInterface()
        {
            @Override
            public void process(String monthYears) {
                super.process(monthYears);
                monthYear = monthYears;
            }
        });

        // For Storing Data For Each Day
        detailRef  = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Detailed Reports/" + monthYear );
        Log.d("Day" ,"Save");


        Map<String, Object> deatiledData = new HashMap<>();


        String date = new SimpleDateFormat("dd-M", Locale.getDefault()).format(new Date());

        deatiledData.put(date, detailed); // Sample Data = 25-02-2018 : 256,32

        detailRef.set(deatiledData, SetOptions.merge()); // Always use Setoptions.merge function else data will be ovewritten



        // For Overview Data

        Map<String, Object> overviewData = new HashMap<>();

         overviewRef  = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Overview/Data");

         Log.d("Overview" , overview);
         overviewData.put("Values" , overview);
         overviewData.put("Time Spent" , currTymSpent);
         overviewRef.set(overviewData,SetOptions.merge());



    }


}

