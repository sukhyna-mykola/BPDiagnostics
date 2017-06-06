package com.example.bpdiagnostics.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bpdiagnostics.database.DBHelper;
import com.example.bpdiagnostics.models.User;
import com.example.bpdiagnostics.models.UserDataDTO;

import java.util.ArrayList;
import java.util.List;

import static com.example.bpdiagnostics.database.DBHelper.KEY_BIRTHDAY;
import static com.example.bpdiagnostics.database.DBHelper.KEY_DATE;
import static com.example.bpdiagnostics.database.DBHelper.KEY_DIASTOLIC;
import static com.example.bpdiagnostics.database.DBHelper.KEY_DOCTOR;
import static com.example.bpdiagnostics.database.DBHelper.KEY_EMAIL;
import static com.example.bpdiagnostics.database.DBHelper.KEY_FIRST_NAME;
import static com.example.bpdiagnostics.database.DBHelper.KEY_ID;
import static com.example.bpdiagnostics.database.DBHelper.KEY_ID_PATIENT;
import static com.example.bpdiagnostics.database.DBHelper.KEY_LAST_NAME;
import static com.example.bpdiagnostics.database.DBHelper.KEY_PARENT_NAME;
import static com.example.bpdiagnostics.database.DBHelper.KEY_PASSWORD;
import static com.example.bpdiagnostics.database.DBHelper.KEY_SEX;
import static com.example.bpdiagnostics.database.DBHelper.KEY_SISTOLIC;
import static com.example.bpdiagnostics.database.DBHelper.KEY_STATE;
import static com.example.bpdiagnostics.database.DBHelper.TABLE_USERS;
import static com.example.bpdiagnostics.database.DBHelper.TABLE_USERS_DATA;


public class DBManager {

    private DBHelper dbHelper;

    private static DBManager manager;

    public static DBManager getInstance(Context c) {
        if (manager == null)
            manager = new DBManager(c);
        return manager;
    }

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }


    public long getIdByEmailAndPassword(String email, String password) {

        long id;

        String selection = KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = new String[]{email, password};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{KEY_ID}, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            int idColumn = c.getColumnIndex(KEY_ID);
            id = c.getLong(idColumn);
        } else {
            id = -1;
        }
        c.close();

        return id;
    }

    public List<User> getUserByName(String name) {
        List<User> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_USERS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColumn = c.getColumnIndex(KEY_ID);
            int firstNameColumn = c.getColumnIndex(KEY_FIRST_NAME);
            int lastNameColumn = c.getColumnIndex(KEY_LAST_NAME);
            int parentNameColumn = c.getColumnIndex(KEY_PARENT_NAME);
            int birthdayColumn = c.getColumnIndex(KEY_BIRTHDAY);
            int sexColumn = c.getColumnIndex(KEY_SEX);
            int emailColumn = c.getColumnIndex(KEY_EMAIL);

            do {
                int id = c.getInt(idColumn);
                String firstName = c.getString(firstNameColumn);
                String lastName = c.getString(lastNameColumn);
                String parentName = c.getString(parentNameColumn);
                String birthday = c.getString(birthdayColumn);
                String sex = c.getString(sexColumn);
                String email = c.getString(emailColumn);

                if (checkContaints(name, firstName) || checkContaints(name, lastName) || checkContaints(name, parentName)) {
                    result.add(new User(id, firstName, lastName, parentName, birthday, sex, email));
                }

            } while (c.moveToNext());
        }
        c.close();

        return result;
    }


    private boolean checkContaints(String s1, String s2) {

        s1 = new String(s1.toLowerCase());
        s2 = new String(s2.toLowerCase());

        if (s1.length() > 0 && s2.length() > 0)
            if (s1.length() > s2.length()) {
                if (s1.contains(s2))
                    return true;
            } else {
                if (s2.contains(s1))
                    return true;
            }
        return false;
    }

    public int getDoctorStatusByEmailAndPassword(long id) {
        int doctorStatus;

        String selection = KEY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{KEY_DOCTOR}, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            int doctorStatusColumn = c.getColumnIndex(KEY_DOCTOR);
            doctorStatus = c.getInt(doctorStatusColumn);
        } else {
            doctorStatus = -1;
        }
        c.close();
        return doctorStatus;
    }


    public long addUser(User user) {
        long id;//result

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        cv.put(KEY_FIRST_NAME, user.getFirstName());
        cv.put(KEY_LAST_NAME, user.getLastName());
        cv.put(KEY_PARENT_NAME, user.getParent());
        cv.put(KEY_BIRTHDAY, user.getBirhday());
        cv.put(KEY_EMAIL, user.getEmail());
        cv.put(KEY_PASSWORD, user.getPassword());
        cv.put(KEY_DOCTOR, user.getDoctor());
        cv.put(KEY_SEX, user.getSex());

        id = db.insert(TABLE_USERS, null, cv);
        db.close();

        return id;
    }

    public long addUserData(UserDataDTO userDataDTO) {
        long id;//result

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        cv.put(KEY_ID_PATIENT, userDataDTO.getUserId());
        cv.put(KEY_DATE, userDataDTO.getDate());
        cv.put(KEY_SISTOLIC, userDataDTO.getSistolic());
        cv.put(KEY_DIASTOLIC, userDataDTO.getDiastolic());
        cv.put(KEY_STATE, userDataDTO.getState());


        id = db.insert(TABLE_USERS_DATA, null, cv);
        db.close();

        return id;
    }

    public List<UserDataDTO> getUserData(long idPatient) {
        List<UserDataDTO> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = KEY_ID_PATIENT + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(idPatient)};
        Cursor c = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            int idColumn = c.getColumnIndex(KEY_ID);
            int idPatientColumn = c.getColumnIndex(KEY_ID_PATIENT);
            int dateColumn = c.getColumnIndex(KEY_DATE);
            int stateColumn = c.getColumnIndex(KEY_STATE);
            int sistolicColumn = c.getColumnIndex(KEY_SISTOLIC);
            int diastolicColumn = c.getColumnIndex(KEY_DIASTOLIC);

            do {
                int id = c.getInt(idColumn);
                int idUser = c.getInt(idPatientColumn);
                int state = c.getInt(stateColumn);
                int sistolic = c.getInt(sistolicColumn);
                int diastolic = c.getInt(diastolicColumn);
                String date = c.getString(dateColumn);

                result.add(new UserDataDTO(idUser, date, sistolic, diastolic, state));

            } while (c.moveToNext());
        }
        c.close();

        return result;
    }


    /**
     * Видаляє всі записи з бази даних
     */
    public void clearDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.close();
    }

    /**
     * Видаляє запис з бази даних по фільтру
     *//*
    public void removeLessonFromDB(LessonDTO lesson) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.LESSON_WEEK+"=? AND "+Constants.DAY_NUMBER +
                "=? AND "+Constants.LESSON_NUMBER+"=?",new String[]{lesson.getLessonWeek(),lesson.getDayNumber(),lesson.getLessonNumber()});
        db.close();
    }

    *//**
     * Оновлює запис в базі по фільтру
     * @param lesson
     *//*
    public void updateLessonInDB(LessonDTO lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(Constants.TEACHER_NAME, lesson.getTeacherName());
        cv.put(Constants.LESSON_NAME, lesson.getLessonName());
        cv.put(Constants.LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(Constants.LESSON_ROOM, lesson.getLessonRoom());
        cv.put(Constants.LESSON_TYPE, lesson.getLessonType());
        cv.put(Constants.DAY_NUMBER, lesson.getDayNumber());
        cv.put(Constants.LESSON_WEEK, lesson.getLessonWeek());

        db.update(Constants.TABLE_NAME,cv,Constants.LESSON_WEEK+"=? AND "+Constants.DAY_NUMBER +
                "=? AND "+Constants.LESSON_NUMBER+"=?",new String[]{lesson.getLessonWeek(),lesson.getDayNumber(),lesson.getLessonNumber()});
        db.close();
    }*/
}
