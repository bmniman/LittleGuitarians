package com.bmniman.littleguitarians.Database;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CreateDocuments extends IntentService {

    FirebaseUser user;
    DocumentReference docRef , docRef2;
    String firstExerName = "Exercise 1"; //  Write The Name of The First Exercise Here, So Thaat On login the first exercise will be automatically unlocked for the user


    public CreateDocuments() {
        super("CreateDocuments");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("Service" , "Create Documents Service Started");
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkDataDocExists(); // This function will check if user Details documnet exists and if not it will create it. This will happen when it's a new user and this will happen only once in the lifetime of the app
        checkProfileDocExists(); // The same goes for this function and this function is used for the document to store the unlocked exercises, by dfault the first exercise is unlocked for the user


    }



    private void checkDataDocExists()
    {
        docRef = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/User Details/Data" );

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(!documentSnapshot.exists()) // If the document doesn't exists.
                {

                    Map<String, Object> docData = new HashMap<>();
                    for (UserInfo profile : user.getProviderData()) {   // The for loop is to get the user info from their respective providers eg: Google or facebook

                        // Id of the provider (ex: google.com)
                        String providerId = profile.getProviderId();

                        // UID specific to the provider
                        String uid = profile.getUid();

                        // Name, email address, and profile photo Url
                        String name = profile.getDisplayName();
                        String email = profile.getEmail();
                        Uri photoUrl = profile.getPhotoUrl();

                        docData.put("Name", name);
                        docData.put("E-Mail" , email);
                        assert photoUrl != null;
                        docData.put("Profile Img" ,photoUrl.toString());
                        docData.put("Provide Id" , providerId);
                        docData.put("User Id" , uid);


                    }

                    docRef.set(docData, SetOptions.merge()); // Always use Setoptions.merge function else data will be ovewritten

                }

            }
        });

    }


    private void checkProfileDocExists()
    {

        docRef2 = FirebaseFirestore.getInstance().document("Users/" + user.getUid() + "/Profile Details/Data" );

        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(!documentSnapshot.exists()) // If the document doesn't exists
                {

                    Map<String, Object> docData = new HashMap<>();

                    docData.put("Unlocked Exercises" , firstExerName );

                    docRef2.set(docData, SetOptions.merge()); // Always use Setoptions.merge function else data will be ovewritten

                }

            }
        });










    }

}
