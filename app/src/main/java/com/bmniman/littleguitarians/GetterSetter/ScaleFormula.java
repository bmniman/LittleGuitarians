package com.bmniman.littleguitarians.GetterSetter;

/**
 * Created by bmnim on 05-Mar-18.
 */

public class ScaleFormula
{
    private int [] major = {0,2,2,1,2,2,2,1};
    private int [] minor = {0,2,1,2,2,1,2,2};
    private int [] minorPent = {0,3,2,2,3,2};
    private int [] majorPent = {0,2,2,3,2,3};
    private int [] dorian = {0,2,1,2,2,2,1,2};




    public ScaleFormula() {
    }

    public int[] getMajor() {
        return major;
    }

    public int[] getMinor() {
        return minor;
    }

    public int[] getMinorPent() {
        return minorPent;
    }

    public int[] getMajorPent() {
        return majorPent;
    }

    public int[] getDorian() {
        return dorian;
    }
}
