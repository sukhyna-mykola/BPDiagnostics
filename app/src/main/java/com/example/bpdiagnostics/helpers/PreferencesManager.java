package com.example.bpdiagnostics.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesManager {


    private SharedPreferences sPref;

    public static final String SHARED_PREFERENCES = "bppreferences";
    public static final String KEY_ID = "id";

    private static PreferencesManager preferencesManager;

    public static PreferencesManager getInstance(Context c) {
        if (preferencesManager == null)
            preferencesManager = new PreferencesManager(c);
        return preferencesManager;
    }

    private PreferencesManager(Context context) {
        sPref = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
    }

    public void saveUserId(long userId) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(KEY_ID, userId);
        ed.commit();
    }

    public long readUserId() {
        return sPref.getLong(KEY_ID, -1);
    }
}
