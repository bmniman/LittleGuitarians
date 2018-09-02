package com.bmniman.littleguitarians.Designs;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bhargavms.dotloader.DotLoader;
import com.bmniman.littleguitarians.Designs.Fragments.KeyFragment;
import com.bmniman.littleguitarians.Designs.Fragments.ScaleFragment;
import com.bmniman.littleguitarians.R;

public class ScaleList extends AppCompatActivity implements ScaleFragment.ListFragmentListener , KeyFragment.ListFragmentListener {

    DotLoader dotLoader;
    TextView loading;
    int count = 0;

    String scale = "";

    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_scale_list);

        dotLoader = findViewById(R.id.text_dot_loader);
        dotLoader.setVisibility(View.INVISIBLE);
        loading = findViewById(R.id.textView);
        loading.setVisibility(View.INVISIBLE);

    }

    @Override
    public void designComplete(String scale , String key)
    {

        if(!scale.equals(""))
        {
            this.scale = scale;
        }

        if(!key.equals(""))
        {
            this.key=key;
        }


        if(!this.scale.isEmpty()  && !this.key.isEmpty())
        {

            Intent intent = new Intent(getApplicationContext() , DrawFretboard.class);
            intent.putExtra("Type" , "scale");
            intent.putExtra("Scale" , this.scale);
            intent.putExtra("Key" , this.key);

            startActivity(intent);
            Log.d("Scale" , scale);
            Log.d("Key" , key);
            Log.d("Wel" , this.scale + this.key);
        }


    }
}
