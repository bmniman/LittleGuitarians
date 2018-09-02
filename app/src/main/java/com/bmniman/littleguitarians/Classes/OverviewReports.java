package com.bmniman.littleguitarians.Classes;

import android.util.Log;

import com.bmniman.littleguitarians.Interfaces.DetailedReportsInterface;
import com.bmniman.littleguitarians.Interfaces.OverviewReportsInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class OverviewReports
{

    private String values;
    private long tymSpent;
    public void getValues(final OverviewReportsInterface listener)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DocumentReference docref = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Overview/Data" );


        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {

                     values  = documentSnapshot.getString("Values");
                    tymSpent  = documentSnapshot.getLong("Time Spent");



                    if(values!=null) // If the days data is present then send it update it.
                    {

                        listener.process(stringToArray(values) , tymSpent);

                    }
                    else // and if the data is not present then send null. This will mostly happen when the day will change and the document for the has not been created
                    {
                        listener.process(null , 0);
                    }


                }
                else {
                    listener.process(null , 0);
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

                Log.d("Array", String.valueOf(notes[i]));

            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            }
        }

        return notes;
    }


}
