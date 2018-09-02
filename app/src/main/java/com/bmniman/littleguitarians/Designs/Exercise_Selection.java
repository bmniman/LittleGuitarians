package com.bmniman.littleguitarians.Designs;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bmniman.littleguitarians.R;

public class Exercise_Selection extends AppCompatActivity {

    TextView defExer,customExer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        setContentView(R.layout.activity_exercise__selection);

    }

    public void defaultClicked(View v)
    {
        Intent intent = new Intent(getApplicationContext(),DefaultExerciseList.class);
        startActivity(intent);

    }



    public void customClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CustomExerciseList.class);
        startActivity(intent);

    }
}
