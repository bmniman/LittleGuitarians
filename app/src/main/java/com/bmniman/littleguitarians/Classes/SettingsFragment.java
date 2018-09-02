package com.bmniman.littleguitarians.Classes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Button;

import com.bmniman.littleguitarians.Database.Login;
import com.bmniman.littleguitarians.Designs.DrawFretboard;
import com.bmniman.littleguitarians.Designs.MainActivity;
import com.bmniman.littleguitarians.R;
import com.bmniman.littleguitarians.trying;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by bmnim on 19-Feb-18.
 */

public class SettingsFragment extends PreferenceFragmentCompat
{


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);

        Preference btn = getPreferenceManager().findPreference("exitlink");

        Preference survey = getPreferenceManager().findPreference("survey");

        if(survey!=null)
        {
            survey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://goo.gl/forms/ro1IBMdZ4iTWjEhs2"));
                    startActivity(browserIntent);

                    return true;
                }
            });
        }



        if(btn!=null)
        {
            btn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                /**
                 * Called when a Preference has been clicked.
                 *
                 * @param preference The Preference that was clicked.
                 * @return True if the click was handled.
                 */
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    showDialog();


                    return true;
                }
            });
        }
    }

    private void showDialog()
    {
        AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(getContext());

        builder.setTitle("SignOut!!!")
                .setMessage("Are you sure you want to signout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), Login.class); // When a button is clicked call DrawFretboard activity


                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .show();
    }
}
