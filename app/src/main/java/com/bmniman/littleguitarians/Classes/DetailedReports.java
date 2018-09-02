package com.bmniman.littleguitarians.Classes;

import com.bmniman.littleguitarians.Interfaces.MonthYearInterface;
import com.bmniman.littleguitarians.Interfaces.DetailedReportsInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedReports
{
    Date dbDate;
    private String monthYear;
    public void getValues(final DetailedReportsInterface listener)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        new MonthYear().getMonthYear(new MonthYearInterface()
        {
            @Override
            public void process(String monthYears) {
                super.process(monthYears);
                monthYear = monthYears; // This is done to get the current month and year . ex : Feb 2108 or Dec 2019 etc
            }
        });

        DocumentReference docref = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Detailed Reports/" + monthYear );




        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {

                    String values  = documentSnapshot.getString(new SimpleDateFormat("dd-M", Locale.getDefault()).format(new Date()));

                    if(values!=null) // If the days data is present then send it update it.
                    {

                        listener.process(stringToArray(values));

                    }
                    else // and if the data is not present then send null. This will mostly happen when the day will change and the document for the has not been created
                    {
                        listener.process(null);
                    }


                }
                else {
                    listener.process(null);
                }

            }
        });

    }



    private int[] stringToArray(String values)
    {
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
