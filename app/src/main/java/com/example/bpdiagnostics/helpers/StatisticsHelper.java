package com.example.bpdiagnostics.helpers;

import android.content.Context;
import android.graphics.Color;

import com.example.bpdiagnostics.models.AgePress;
import com.example.bpdiagnostics.models.UserDataDTO;
import com.example.bpdiagnostics.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;

public class StatisticsHelper {

    private DBManager dbManager;
    private ArrayList<UserDataDTO> userDataDTOs;

    private ArrayList<PointValue> entry1;
    private ArrayList<PointValue> entry2;
    private ArrayList<PointValue> entry3;

    private ArrayList<PointValue> entryAll;

    private ArrayList<PointValue> norma;
    private ArrayList<PointValue> giper;

    private ArrayList<PointValue> min;

    private ArrayList<PointValue> last;
    private ArrayList<PointValue> average;

    private List<SubcolumnValue> columnIn;
    private List<SubcolumnValue> columnOut;

    public List<SubcolumnValue> getColumnOut() {
        return columnOut;
    }

    public List<SubcolumnValue> getColumnIn() {
        return columnIn;
    }


    public float normaSmin = 110;
    public float normaSmax = 135;
    public float normaDmin = 80;
    public float normaDmax = 90;

    public final float giperSmin = 140;
    public float giperSmax = 200;
    public final float giperDmin = 90;
    public float giperDmax = 100;


    private float minS = Float.MAX_VALUE, minD = Float.MAX_VALUE, maxS = Float.MIN_VALUE, maxD = Float.MIN_VALUE;
    private float MS, MD, SS, SD;
    private float lastS, lastD;
    private int inNorma, inGiper;

    private int count;

    private int state;
    private String recomendation;

    public String getRecomendation() {
        return recomendation;
    }

    public StatisticsHelper(Context context, long userID) {
        dbManager = DBManager.getInstance(context);
        init(userID);
    }


    public float getMinS() {
        return minS;
    }

    public float getMinD() {
        return minD;
    }

    public float getMaxS() {
        return maxS;
    }

    public float getMaxD() {
        return maxD;
    }

    private void init(long userId) {
        userDataDTOs = (ArrayList<UserDataDTO>) dbManager.getUserData(userId);
        count = userDataDTOs.size();

        if (count > 0) {

            int age = dbManager.getAgeById(userId);

            configureParemeters(age);

            buildEntrys();

            buildNorma();
            buildGiper();

            buildLast();
            buildAverage();

            buildMin();
            buildAreaSet();

            calcColumnStatistics();

            calcState();

            calcRecomendation(age);
        } else {
            minS = minD = maxD = maxS = 0;
        }
    }


    public ArrayList<PointValue> getMin() {
        return min;
    }

    public ArrayList<PointValue> getEntry1() {
        return entry1;
    }

    public ArrayList<PointValue> getEntry2() {
        return entry2;
    }

    public ArrayList<PointValue> getEntry3() {
        return entry3;
    }

    public ArrayList<PointValue> getEntryAll() {
        return entryAll;
    }

    public ArrayList<PointValue> getNorma() {
        return norma;
    }

    public ArrayList<PointValue> getGiper() {
        return giper;
    }

    public ArrayList<PointValue> getLast() {
        return last;
    }

    public ArrayList<PointValue> getAverage() {
        return average;
    }

    public int getState() {
        return state;
    }


    private void buildMin() {
        min = new ArrayList<>();
        min.add(new PointValue(minS - 20, minD - 10));
    }

    private void buildAverage() {
        average = new ArrayList<>();
        average.add(new PointValue(MS, MD));
    }

    private void buildLast() {
        last = new ArrayList<>();
        last.add(new PointValue(lastS, lastD));

    }


    private void calcColumnStatistics() {
        columnIn = new ArrayList<>();
        columnOut = new ArrayList<>();

        int inPrecent = (int) Math.ceil(((float) inNorma) / count * 100);
        int outPercent = 100 - inPrecent;

        columnIn.add(new SubcolumnValue(inPrecent, Color.GREEN));
        columnOut.add(new SubcolumnValue(outPercent, Color.RED));

    }


    private void calcState() {

        float inNormPercent = (((float) inNorma) / count) * 100;

        int e = 10;

        state = 3;

        if ((inNormPercent > 40) &&//40%
                ((MS > normaSmin && MS < normaSmax &&
                        MD > normaDmin && MD < normaDmax) || checInsideOrNiarNorm()) &&
                (Math.abs(lastS - MS) < 2 * e && Math.abs(lastD - MD) < 2 * e) &&
                (inGiper < 5) &&
                (maxS - minS < 50)) {
            state = 2;

        }

        if ((inNormPercent > 50)//50%

                && (MS > normaSmin && MS < normaSmax && MD > normaDmin && MD < normaDmax)

                && (Math.abs(lastS - MS) < e && Math.abs(lastD - MD) < e)

                && (inGiper == 0)) {
            state = 1;
        }


    }


    private void configureParemeters(int age) {

        for (UserDataDTO data : userDataDTOs) {
            minS = Math.min(minS, data.getSistolic());
            minD = Math.min(minD, data.getDiastolic());

            maxS = Math.max(maxS, data.getSistolic());
            maxD = Math.max(maxD, data.getDiastolic());
        }

        minS = Math.min(minS, normaSmin);
        minD = Math.min(minD, normaDmin);
        //-----------------
        AgePress ap = Constants.searchNeedData(age);
        if (ap != null) {
            normaSmax = ap.getMaxS();
            normaDmax = ap.getMaxD();
            normaSmin = ap.getMinS();
            normaDmin = ap.getMinD();
        }
        //---------------------

        giperSmax = maxS + 30;
        giperDmax = maxD + 10;

        //-----------------------

        calcM();
        calcD();
        //-----------------

        for (UserDataDTO data : userDataDTOs) {
            if (data.getSistolic() > normaSmin && data.getSistolic() < normaSmax &&
                    data.getDiastolic() > normaDmin && data.getDiastolic() < normaDmax) {
                inNorma++;
            }
            if (data.getSistolic() > giperSmin && data.getSistolic() < giperSmax &&
                    data.getDiastolic() > giperDmin && data.getDiastolic() < giperDmax) {
                inGiper++;
            }
        }
        //------------------

        UserDataDTO lastData = userDataDTOs.get(count - 1);
        lastS = lastData.getSistolic();
        lastD = lastData.getDiastolic();
        //-------------------


    }

    private boolean checInsideOrNiarNorm() {
        int e = 10;
        if (Math.abs(MS - normaSmin) < e && Math.abs(MS - normaSmax) < e && Math.abs(MD - normaDmin) < e && Math.abs(MD - normaSmax) < e)
            return true;
        return false;
    }

    private void buildAreaSet() {

        List<PointValue> points = new ArrayList<>();

        float dS = Math.max(Math.abs(MS - minS), Math.abs(MS - maxS));
        float dD = Math.max(Math.abs(MD - minD), Math.abs(MD - maxD));
        float maxR = Math.max(dD, dS);
        float e = (float) 10;

        float s, d;

        for (float a = 0; a < 2 * Math.PI; a += 0.1) {
            for (float R = maxR; R > 0; R -= 0.5) {
                boolean is = false;
                s = (float) (MS + R * Math.sin(a));
                d = (float) (MD + R * Math.cos(a));
                for (PointValue p : entryAll) {
                    if (Math.abs(s - p.getX()) < e && Math.abs(d - p.getY()) < e) {
                        points.add(p);
                        is = true;
                        break;
                    }
                }

                if (is) break;
            }
        }
        if (points.size() > 0)
            points.add(points.get(0));

        entryAll = new ArrayList<>(points);
    }

    private void buildNorma() {

        norma = new ArrayList<>();

        norma.add(new PointValue(normaSmin, normaDmin));
        norma.add(new PointValue(normaSmax, normaDmin));
        norma.add(new PointValue(normaSmax, normaDmax));
        norma.add(new PointValue(normaSmin, normaDmax));
        norma.add(new PointValue(normaSmin, normaDmin));
    }


    private void buildGiper() {

        giper = new ArrayList<>();
        giper.add(new PointValue(giperSmin, giperDmin));
        giper.add(new PointValue(giperSmax, giperDmin));
        giper.add(new PointValue(giperSmax, giperDmax));
        giper.add(new PointValue(giperSmin, giperDmax));
        giper.add(new PointValue(giperSmin, giperDmin));


    }

    private void buildEntrys() {

        entry1 = new ArrayList<>();
        entry2 = new ArrayList<>();
        entry3 = new ArrayList<>();

        entryAll = new ArrayList<>();

        for (UserDataDTO data : userDataDTOs) {
            PointValue e = new PointValue(data.getSistolic(), data.getDiastolic());
            entryAll.add(e);
            switch (data.getState()) {
                case 1:
                    entry1.add(e);
                    break;
                case 2:
                    entry2.add(e);
                    break;
                case 3:
                    entry3.add(e);
                    break;
            }

        }

        if (entryAll.size() > 0) {
            PointValue p = entryAll.get(0);
            entryAll.add(new PointValue(p.getX(), p.getY()));
        }
    }


    private void calcD() {
        for (UserDataDTO u : userDataDTOs) {
            SS += Math.pow((u.getDiastolic() - MS), 2);
            SD += Math.pow((u.getDiastolic() - MD), 2);
        }
        SS /= (count - 1);
        SD /= (count - 1);

        SS = (float) Math.sqrt(SS);
        SD = (float) Math.sqrt(SD);
    }

    private void calcM() {
        for (UserDataDTO u : userDataDTOs) {
            MS += u.getSistolic();
            MD += u.getDiastolic();
        }
        MS /= count;
        MD /= count;

    }

    private void calcRecomendation(int age) {
        int progressState = 0;

        double PS = Constants.searchNeedData(age).getNormS();
        double PD = Constants.searchNeedData(age).getNormD();

        boolean leftP = (lastS - PS > 0) ? false : true;
        boolean leftM = (lastS - MS > 0) ? false : true;

        boolean topP = (lastD - PD < 0) ? false : true;
        boolean topM = (lastD - MD < 0) ? false : true;

        boolean toPS = (!leftP && leftM) || (leftP && !leftM);
        boolean toPD = (!topP && topM) || (topP && !topM);

        boolean inHalfS = Math.abs(lastS - MS) > SS / 2 ? false : true;
        boolean inHalfD = Math.abs(lastD - MD) > SD / 2 ? false : true;

        boolean inS = Math.abs(lastS - MS) > SS ? false : true;
        boolean inD = Math.abs(lastD - MD) > SD ? false : true;

        boolean inMidleS = !inHalfS && inS;
        boolean inMidleD = !inHalfD && inD;


        if (inHalfD && inHalfS)
            progressState = 5;


        if (!inS && !inD && !toPD && !toPS)
            progressState = 1;

        if ((!inS && inD && !toPD && !toPS) || (inS && !inD && !toPD && !toPS))
            progressState = 2;

        if (inS && inD && !toPD && !toPS)
            progressState = 3;

        if (inS && inD && !toPD && !toPS)
            progressState = 3;

        if ((inS && inMidleD && !toPD && !toPS) || (inMidleS && inD && !toPD && !toPS))
            progressState = 4;


        if ((inS && inMidleD && toPD && toPS) || (inMidleS && inD && toPD && toPS))
            progressState = 6;

        if (inS && inD && toPD && toPS)
            progressState = 7;

        if ((!inS && inD && toPD && toPS) || (inS && !inD && toPD && toPS))
            progressState = 8;

        if (!inS && !inD && !toPD && !toPS)
            progressState = 9;

        recomendation = dbManager.getRecomendation(progressState);
    }
}
