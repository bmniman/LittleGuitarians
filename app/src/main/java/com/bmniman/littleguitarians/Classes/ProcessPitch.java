package com.bmniman.littleguitarians.Classes;

import android.util.Log;

import static be.tarsos.dsp.beatroot.AgentList.count;

/**
 * Created by bmnim on 05-Dec-17.
 */



/*

The basic formula for the frequencies of the notes of the equal tempered scale is given by
fn = f0 * (a)n
where
f0 = the frequency of one fixed note which must be defined. A common choice is setting the A above middle C (A4) at f0 = 440 Hz.
n = the number of half steps away from the fixed note you are. If you are at a higher note, n is positive. If you are on a lower note, n is negative.
fn = the frequency of the note n half steps away.
a = (2)1/12 = the twelth root of 2 = the number which when multiplied by itself 12 times equals 2 = 1.059463094359...

The wavelength of the sound for the notes is found from
Wn = c/fn
where W is the wavelength and c is the speed of sound. The speed of sound depends on temperature, but is approximately 345 m/s at "room temperature."

Examples using A4 = 440 Hz:

C5 = the C an octave above middle C. This is 3 half steps above A4 and so the frequency is
f3 = 440 * (1.059463..)3 = 523.3 Hz
If your calculator does not have the ability to raise to powers, then use the fact that
(1.059463..)3 = (1.059463..)*(1.059463..)*(1.059463..)
That is, you multiply it by itself 3 times.

Middle C is 9 half steps below A4 and the frequency is:
f -9 = 440 * (1.059463..)-9 = 261.6 Hz
If you don't have powers on your calculator, remember that the negative sign on the power means you divide instead of multiply. For this example, you divide by (1.059463..) 9 times.



 */

public class ProcessPitch
{

    public boolean checkPitch(int  row , int col , float[] pitch)
    {

        boolean correct = false;

        float currentpitch = pitch[0];
        float actualpitch = getActualPitch(row , col);


        Log.d("Notes" , "Actual Pitch " + actualpitch + " Current Pitch " + currentpitch);



        float max  = actualpitch + 5;
         float min =  actualpitch - 5;

        if( currentpitch < max && currentpitch > min)
        {
            correct =  true;
        }

        return correct;
    }

    private float getActualPitch(int row, int col)
    {
        float actualnotes [] = {329.6f , 246.9f , 196.0f , 146.8f , 110.0f , 82.41f}; // e b g d a E

        float pitch;

        pitch = (float) (actualnotes[row] * Math.pow(1.059463 , col));

        return pitch;


    }


}
