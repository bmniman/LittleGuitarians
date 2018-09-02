package com.bmniman.littleguitarians.Designs;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bhargavms.dotloader.DotLoader;
import com.bmniman.littleguitarians.Designs.Fragments.CustomExerFragment;
import com.bmniman.littleguitarians.R;

public class CustomExerciseList extends AppCompatActivity implements CustomExerFragment.ListFragmentListener {


    DotLoader dotLoader;
    TextView loading;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_custom_exercise_list);

        dotLoader = findViewById(R.id.text_dot_loader);
        loading = findViewById(R.id.textView);

    }




    @Override
    public void designComplete() { // CAlled when the data is recieved from the firebase

        count++;
        if(count ==1) {
            dotLoader.setVisibility(View.INVISIBLE); // Make the lodaing animation invisible
            loading.setVisibility(View.INVISIBLE); // Make the textview invisible
        }

        Log.d("Track" , String.valueOf(count));

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}

