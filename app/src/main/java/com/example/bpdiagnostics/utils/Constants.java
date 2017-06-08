package com.example.bpdiagnostics.utils;

import com.example.bpdiagnostics.models.AgePress;

import java.util.ArrayList;
import java.util.List;

public final class Constants {


    public static final String MEN = "м";
    public static final String WOMEN = "ж";

    public static final int DOCTOR_NO = 0;
    public static final int DOCTOR_YES = 1;

    public static List<AgePress> agePresses = new ArrayList<>();

    static {
        agePresses.add(new AgePress(Integer.MIN_VALUE, 24, 108, 75, 132, 83, 120, 79));
        agePresses.add(new AgePress(25, 29, 109, 76, 133, 84, 121, 80));
        agePresses.add(new AgePress(30, 34, 110, 77, 134, 85, 122, 81));
        agePresses.add(new AgePress(35, 39, 111, 78, 135, 86, 123, 82));
        agePresses.add(new AgePress(35, 39, 111, 78, 135, 86, 123, 82));
        agePresses.add(new AgePress(40, 44, 112, 79, 137, 87, 125, 83));
        agePresses.add(new AgePress(45, 49, 115, 80, 139, 88, 127, 84));
        agePresses.add(new AgePress(50, 54, 116, 81, 142, 89, 129, 85));
        agePresses.add(new AgePress(55, 59, 118, 82, 144, 90, 131, 86));
        agePresses.add(new AgePress(60, Integer.MAX_VALUE, 121, 83, 147, 91, 134, 87));
    }

    public static AgePress searchNeedData(int age) {
        for (AgePress ap : agePresses) {
            if (ap.checkAge(age))
                return ap;

        }
        return null;
    }
}

