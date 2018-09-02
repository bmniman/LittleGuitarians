package com.bmniman.littleguitarians;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bmniman.littleguitarians.Designs.MainActivity;
import com.google.firebase.auth.FirebaseAuth;


public class trying extends AppCompatActivity {

    Button delete , signout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trying);

        delete = findViewById(R.id.delete);
        signout = findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = firebaseAuth -> {

            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(trying.this, MainActivity.class));  // If user is already signed in the method to be called is placed here
            }
        };

        delete.setOnClickListener(view -> {
            mAuth.getCurrentUser().delete();
            Toast.makeText(getApplicationContext() , " Account Deleted" , Toast.LENGTH_SHORT).show();
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext() , "Signed Out " , Toast.LENGTH_SHORT).show();
            }
        });


    }



}
