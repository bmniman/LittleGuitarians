package com.bmniman.littleguitarians.Classes;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.bmniman.littleguitarians.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by bmnim on 31-Jan-18.
 */


public class PlaySound
{
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;


    public PlaySound(Context context)
    {



        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {   // If new version then use soundpoolbuilder else use  deprecated soundpool
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttrib)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }


        soundID = soundPool.load(context, R.raw.g_one,1);


        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded =true;
            }
        });


        play();
    }

    private void play()
    {
        if(loaded)
        {
            soundPool.play(soundID,100,100,1,0,1f);
        }
    }
}
