package com.bmniman.littleguitarians.Classes;

import android.util.Log;

import com.bmniman.littleguitarians.Interfaces.MonthYearInterface;

import java.util.Calendar;

/**
 * Created by bmnim on 17-Feb-18.
 */

public class MonthYear
{
    public void getMonthYear(final MonthYearInterface listener)
    {
        String months [] = {"Jan" , "Feb" , "March" , "April" , "May" , "June" , "July" , "Augst" , "Sept" , "Oct" , "Nov" ,"Dec"};

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Log.d("Day" , months[month] + " " + year );
        listener.process(months[month] + " " + year);
    }
}
