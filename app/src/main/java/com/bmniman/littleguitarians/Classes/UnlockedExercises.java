package com.bmniman.littleguitarians.Classes;

import com.bmniman.littleguitarians.Interfaces.UnlockedExerciseInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by bmnim on 02-Mar-18.
 */

public class UnlockedExercises
{
    private FirebaseUser user;
    StringBuilder unlockedExercises = new StringBuilder();
    public void getExercise(final UnlockedExerciseInterface listener)
    {

        user  = FirebaseAuth.getInstance().getCurrentUser();

        final DocumentReference docref = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Profile Details/Data" );

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                    unlockedExercises.append(documentSnapshot.getString("Unlocked Exercises"));
                    listener.process(unlockedExercises);



            }
        });


    }
}
