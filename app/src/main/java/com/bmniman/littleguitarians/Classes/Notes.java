package com.bmniman.littleguitarians.Classes;

import android.util.Log;

import com.bmniman.littleguitarians.Interfaces.NotesInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by bmnim on 22-Dec-17.
 */



public class Notes
{
    public void getNote(final String stringnotes , final NotesInterface listener)
    {

                String[] items = stringnotes.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

                int[] notes = new int[items.length];

                for (int i = 0; i < items.length; i++) {
                    try {
                        notes[i] = Integer.parseInt(items[i]);
                    } catch (NumberFormatException nfe) {
                        //NOTE: write something here if you need to recover from formatting errors
                    }
                }



                if(notes.length!=0)
                {
                    listener.process(notes);
                }

    }






}
