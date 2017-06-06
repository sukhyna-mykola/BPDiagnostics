package com.example.bpdiagnostics.models;

/**
 * Created by mykola on 06.06.17.
 */

public class UserDataDTO {

    private long id, userId;
    private String date;
    private int sistolic, diastolic;
    private int state;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public int getSistolic() {
        return sistolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public int getState() {
        return state;
    }

    public UserDataDTO(long userId, String date, int sistolic, int diastolic, int state) {

        this.userId = userId;
        this.date = date;
        this.sistolic = sistolic;
        this.diastolic = diastolic;
        this.state = state;
    }
}
