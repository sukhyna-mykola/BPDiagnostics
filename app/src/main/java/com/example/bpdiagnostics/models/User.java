package com.example.bpdiagnostics.models;

/**
 * Created by mykola on 06.06.17.
 */

public class User {
    private int id;
    private String firstName, lastName, parent;
    private String birhday;
    private String sex;
    private int doctor; ///в SQLite3 немає boolean, тому 0 - false, 1  -true;
    private String email, password;


    public User(String firstName, String lastName, String parent, String birhday, String sex, int doctor, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
        this.birhday = birhday;
        this.sex = sex;
        this.doctor = doctor;
        this.email = email;
        this.password = password;
    }

    public User(int id, String firstName, String lastName, String parent, String birhday, String sex, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
        this.birhday = birhday;
        this.sex = sex;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getParent() {
        return parent;
    }

    public String getBirhday() {
        return birhday;
    }

    public String getSex() {
        return sex;
    }

    public int getDoctor() {
        return doctor;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
