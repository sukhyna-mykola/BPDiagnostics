package com.example.bpdiagnostics.helpers;

import android.content.Context;
import android.graphics.Color;

import com.example.bpdiagnostics.models.UserDataDTO;

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

    private List<Column> columnValues;

    public final float normaSmin = 110;
    public final float normaSmax = 135;
    public final float normaDmin = 80;
    public final float normaDmax = 90;

    public final float giperSmin = 140;
    public float giperSmax = 200;
    public final float giperDmin = 90;
    public float giperDmax = 100;


    private float minS = Float.MAX_VALUE, minD = Float.MAX_VALUE, maxS = Float.MIN_VALUE, maxD = Float.MIN_VALUE;

    private int state;
    private String recomendation;

    public String getRecomendation() {
        return recomendation;
    }

    public StatisticsHelper(Context context, long userID) {
        dbManager = DBManager.getInstance(context);
        init(userID);
    }

    public List<Column> getColumnValues() {
        return columnValues;
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

        if (!userDataDTOs.isEmpty()) {

            calcMinMax();

            buildEntrys();

            buildNorma();
            buildGiper();

            buildLast();
            buildAverage();

            calcMin();

            calcAreaSet();

            calcStatistics();

            calcState();
        } else {
            minS = minD = maxD = maxS = 0;
        }
    }

    public ArrayList<PointValue> getMin() {
        return min;
    }

    private void calcMin() {
        min = new ArrayList<>();
        min.add(new PointValue(minS - 20, minD - 10));
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

    private void buildAverage() {
        float s = 0, d = 0;
        average = new ArrayList<>();
        for (UserDataDTO data : userDataDTOs) {
            s += data.getSistolic();
            d += data.getDiastolic();
        }
        s /= userDataDTOs.size();
        d /= userDataDTOs.size();
        average.add(new PointValue(s, d));

    }

    private void buildLast() {
        last = new ArrayList<>();
        if (userDataDTOs.size() > 0) {
            UserDataDTO lastData = userDataDTOs.get(userDataDTOs.size() - 1);
            last.add(new PointValue(lastData.getSistolic(), lastData.getDiastolic()));
        }
    }


    private void calcStatistics() {
        int in = 0, out = 0;
        columnValues = new ArrayList<>();
        for (UserDataDTO data : userDataDTOs) {
            if (data.getSistolic() > normaSmin && data.getSistolic() < normaSmax &&
                    data.getDiastolic() > normaDmin && data.getDiastolic() < normaDmax) {
                in++;
            } else {
                out++;
            }
        }

        SubcolumnValue invalue = new SubcolumnValue(in, Color.GREEN);
        SubcolumnValue outvalue = new SubcolumnValue(out, Color.RED);

        List<SubcolumnValue> invalues = new ArrayList<>();
        List<SubcolumnValue> outvalues = new ArrayList<>();

        invalues.add(invalue);
        outvalues.add(outvalue);


        Column inc = new Column(invalues);
        Column outc = new Column(outvalues);

        columnValues.add(outc);
        columnValues.add(inc);

    }

    public int getState() {
        return state;
    }

    private void calcState() {

        float inNorm = 0, outNorm = 0;
        for (UserDataDTO data : userDataDTOs) {
            if (data.getSistolic() > normaSmin && data.getSistolic() < normaSmax &&
                    data.getDiastolic() > normaDmin && data.getDiastolic() < normaDmax) {
                inNorm++;
            } else {
                outNorm++;
            }
        }

        float inNormPercent = inNorm / (inNorm + outNorm);//%
        float outNormPercent = outNorm / (inNorm + outNorm);


        float inGiper = 0, outGiper = 0;
        for (UserDataDTO data : userDataDTOs) {
            if (data.getSistolic() > giperSmin && data.getDiastolic() > giperDmin) {
                inGiper++;
            } else {
                outGiper++;
            }
        }


        float inGiperPercent = inGiper / (inGiper + outGiper);
        float outGiperPrecent = outGiper / (inGiper + outGiper);

        PointValue av = average.get(0);
        PointValue l = last.get(0);

        int e = 10;

        if ((inNormPercent > 0.5)
                && (av.getX() > normaSmin && av.getX() < normaSmax && av.getY() > normaDmin && av.getY() < normaDmax)
                && (Math.abs(l.getX() - av.getX()) < e && Math.abs(l.getY() - av.getY()) < e)
                && (inGiper == 0)) {
            state = 1;
        } else if ((inNormPercent > 0.4) &&
                ((av.getX() > normaSmin && av.getX() < normaSmax &&
                        av.getY() > normaDmin && av.getY() < normaDmax) || checInsideOrNiarNorm(av)) &&
                (Math.abs(l.getX() - av.getX()) < 2 * e && Math.abs(l.getY() - av.getY()) < 2 * e) &&
                (inGiper < 5) &&
                (maxS - minS < 50)) {
            state = 2;

        } else state = 3;


        recomendation = dbManager.getRecomendation(state);

    }


    private void calcMinMax() {

        for (UserDataDTO data : userDataDTOs) {
            minS = Math.min(minS, data.getSistolic());
            minD = Math.min(minD, data.getDiastolic());

            maxS = Math.max(maxS, data.getSistolic());
            maxD = Math.max(maxD, data.getDiastolic());
        }

        giperSmax = maxS + 30;
        giperDmax = maxD + 10;
    }

    private boolean checInsideOrNiarNorm(PointValue av) {
        int e = 10;
        if (Math.abs(av.getX() - normaSmin) < e && Math.abs(av.getX() - normaSmax) < e && Math.abs(av.getY() - normaDmin) < e && Math.abs(av.getY() - normaSmax) < e)
            return true;
        return false;
    }

    private void calcAreaSet() {
        List<PointValue> points = new ArrayList<>();
        float max = 50;
        float e = (float) 10;
        float s, d;
        PointValue v = average.get(0);
        for (float a = 0; a < 2 * Math.PI; a += 0.1) {
            for (float R = max; R > 0; R -= 0.5) {
                boolean is = false;
                s = (float) (v.getX() + R * Math.sin(a));
                d = (float) (v.getY() + R * Math.cos(a));
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


}
