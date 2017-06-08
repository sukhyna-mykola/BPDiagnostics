package com.example.bpdiagnostics.models;

/**
 * Created by mykola on 08.06.17.
 */

public class AgePress {
    private int minAge, maxAge;

    public AgePress(int minAge, int maxAge, int minS, int minD, int maxS, int maxD, int normS, int normD) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minS = minS;
        this.minD = minD;
        this.maxS = maxS;
        this.maxD = maxD;
        this.normS = normS;
        this.normD = normD;
    }

    private int minS, minD, maxS, maxD, normS, normD;

    public int getMinS() {
        return minS;
    }

    public int getMinD() {
        return minD;
    }

    public int getMaxS() {
        return maxS;
    }

    public int getMaxD() {
        return maxD;
    }

    public int getNormS() {
        return normS;
    }

    public int getNormD() {
        return normD;
    }



    public boolean checkAge(int age) {
        if (age >= minAge && age <= maxAge) {
            return true;
        }
        return false;
    }
}
