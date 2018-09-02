package com.bmniman.littleguitarians.Designs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bmniman.littleguitarians.Classes.SettingsActivity;
import com.bmniman.littleguitarians.Database.CreateDocuments;
import com.bmniman.littleguitarians.Database.Login;
import com.bmniman.littleguitarians.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    FirebaseUser user;

       protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        checkNetworkConnection();

    }



    private void checkNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }



       if(haveConnectedMobile || haveConnectedWifi) // If coonected to the internet check if uer is already logged in
       {
           checkIfLoggedIn();
       }
       else { // It nit connected then shiw an error dialog box

            drawDialog();
          // user = FirebaseAuth.getInstance().getCurrentUser();
       }


//        return haveConnectedWifi || haveConnectedMobile;
    }



    private void drawDialog()
    {
          new FancyAlertDialog.Builder(this)
                .setTitle("No Internet Connection Detected!!!")
                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                .setNegativeBtnText("Retry")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Exit")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_error_outline_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                        exitApp(); // If Exit Button Is clicked app Will close
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                        checkNetworkConnection(); // On Retry Button Clicking internt connection will be checked again

                    }
                })
                .build();

    }

    private void exitApp() // This function will close athe appn along with it release all it's resorces
    {
        finishAffinity();
        System.exit(0);
    }

    private void checkIfLoggedIn()
    {
         user = FirebaseAuth.getInstance().getCurrentUser();

         if(user!=null)
         {
             startActivity(new Intent(MainActivity.this , HomeScreen.class));
             finish(); // This will not let the Splash screen to be called again
         }
         else {
             startActivity(new Intent(MainActivity.this , Login.class));
             finish(); // This will not let the Splash screen to be called again
         }

    }
}

