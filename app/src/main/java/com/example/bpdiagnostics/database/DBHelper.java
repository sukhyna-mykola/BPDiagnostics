package com.example.bpdiagnostics.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bpdiagnostics.R;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bp_database";

    public static final String TABLE_USERS = "users";
    public static final String TABLE_USERS_DATA = "users_data";
    public static final String TABLE_USERS_RECOMENTATIONS = "users_recomendations";

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

    public static final String KEY_RECOMENDATION = "recomendation";

    private String[] recomendations  =new String[]{
            "У вашому стані необхідно негайно звернутись до лікаря. Вам слід приділити увагу своєму стану здоров'я. ",
    "Вам слід приділити увагу своєму стану здоров'я. Рекомендується випити свіжий сік моркви і петрушки, грейпфрута і апельсина. Виконуйте більше фізичних вправ. Заплануйте віхит до лікаря.",
    "Ваш стан значно гірше, ніж норма. Вам слід відмовитись від вживання солі, спиртних напоїв та тютюнових виробів. Відпочиньте.",
    "Ваш стан незначно, але гірше норми. Намайтесь вести більш рухливий спосіб життя. Включіть в свій раціон більше свіжих фруктів та овочів, особливо салат з буряка.",
    "Ви знаходитесь у своїй нормі. Стан здоров'я є прийнятним для нормального функціонування організму.",
    "Ваш стан здоров'я є кращим ніж зазвичай і ви можете почуватися вільно у будь-якій ситуації.",
    "Ваш стан здоров'я є кращим, ніж зазвичай і ви можете ні в чому собі не відмовляти.",
    "Вам слід приділити увагу своєму стану здоров'я. Рекомендується в середині дня  випивати чашку міцного солодкого чаю або кави. А також приділіть увагу кількості білків та вітамінів Е й групи В у складі вашого раціону харчування.",
    "У вашому стані необхідно негайно звернутись до лікаря. Вам слід приділити увагу своєму стану здоров'я. Рекомендовано сьогодні поспати більше 8 годин, а вранці влаштуйте собі контрасний душ і пийте більше води! ",

};

    public DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        //recomendations = context.getResources().getStringArray(R.array.recomendations);
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

        db.execSQL("create table " + TABLE_USERS_RECOMENTATIONS + " ("
                + KEY_ID + " " + INTEGER + " PRIMARY KEY ,"
                + KEY_RECOMENDATION + " " + TEXT
                + ");");

        ContentValues cv = new ContentValues();


        for (int i = 1; i <= recomendations.length; i++) {
            cv.put(KEY_ID, i);
            cv.put(KEY_RECOMENDATION, recomendations[i - 1]);
            db.insert(TABLE_USERS_RECOMENTATIONS, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

