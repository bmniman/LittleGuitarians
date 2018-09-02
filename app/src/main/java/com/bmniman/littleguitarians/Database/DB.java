package com.bmniman.littleguitarians.Database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bmniman.littleguitarians.Interfaces.NotesInterface;
import com.bmniman.littleguitarians.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

public class DB extends AppCompatActivity {


    BoomMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);



        bmb = findViewById(R.id.bmb);

       /* bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {

                Log.d("Boom" , "On clicked");
            }

            @Override
            public void onBackgroundClick() {

                Log.d("Boom" , "on background click");


            }

            @Override
            public void onBoomWillHide() {

                Log.d("Boom" , "on boom will hide");


            }

            @Override
            public void onBoomDidHide() {


                Log.d("Boom" , "on boom did hide");

            }

            @Override
            public void onBoomWillShow() {

                Log.d("Boom" , "on boom will show");

            }

            @Override
            public void onBoomDidShow() {

                Log.d("Boom" , "on boom did show");


            }
        });

*/
        bmb.setOnBoomListener(new OnBoomListenerAdapter()
        {

            /**
             * When the BMB finishes hide animations.
             */
            @Override
            public void onBoomDidHide() {
                super.onBoomDidHide();
                Log.d("Boom" , "on boom did hide");

            }

            /**
             * When the BMB finished boom animations.
             */
            @Override
            public void onBoomDidShow() {
                super.onBoomDidShow();
                Log.d("Boom" , "on boom did show");

            }
        });


        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.fourth)
                    .normalText(String.valueOf(i))
                    .rippleEffect(true)
                    .shadowEffect(true);
            bmb.addBuilder(builder);
        }


    }

}
