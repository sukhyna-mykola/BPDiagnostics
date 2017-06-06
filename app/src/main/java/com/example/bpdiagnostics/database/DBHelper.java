package com.example.bpdiagnostics.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bp_database";

    public static final String TABLE_USERS = "users";
    public static final String TABLE_USERS_DATA = "users_data";

    public static final String INTEGER = "integer";
    public static final String TEXT = "text";

    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_LAST_NAME = "lastname";
    public static final String KEY_PARENT_NAME = "parentname";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_SEX = "sex";
    public static final String KEY_DOCTOR = "doctor";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID_PATIENT = "id_patient";
    public static final String KEY_SISTOLIC = "sistolic";
    public static final String KEY_DIASTOLIC = "diastolic";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_STATE = "state";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_USERS + " ("
                + KEY_ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT,"
                + KEY_FIRST_NAME + " " + TEXT + ","
                + KEY_LAST_NAME + " " + TEXT + ","
                + KEY_PARENT_NAME + " " + TEXT + ","
                + KEY_BIRTHDAY + " " + TEXT + ","
                + KEY_PASSWORD + " " + TEXT + ","
                + KEY_EMAIL + " " + TEXT + ","
                + KEY_SEX + " " + TEXT + ","
                + KEY_DOCTOR + " " + INTEGER
                + ");");

        db.execSQL("create table " + TABLE_USERS_DATA + " ("
                + KEY_ID + " " + INTEGER + " PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID_PATIENT + " " + INTEGER + ","
                + KEY_SISTOLIC + " " + INTEGER + ","
                + KEY_DIASTOLIC + " " + INTEGER + ","
                + KEY_STATE + " " + INTEGER + ","
                + KEY_DATE + " " + TEXT
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

