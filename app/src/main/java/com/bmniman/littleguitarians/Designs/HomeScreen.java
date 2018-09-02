package com.bmniman.littleguitarians.Designs;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.bmniman.littleguitarians.Classes.CustomViewPager;
import com.bmniman.littleguitarians.Classes.SettingsFragment;
import com.bmniman.littleguitarians.Designs.Fragments.ChartsFragment;
import com.bmniman.littleguitarians.Designs.Fragments.PlayOption;
import com.bmniman.littleguitarians.R;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


public class HomeScreen extends AppCompatActivity implements ChartsFragment.OnFragmentInteractionListener {

    SpaceTabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home_screen);


        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ChartsFragment());
        fragmentList.add(new PlayOption());
        fragmentList.add(new SettingsFragment());

        ViewPager viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.spaceTabLayout);


        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);


    }


    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
