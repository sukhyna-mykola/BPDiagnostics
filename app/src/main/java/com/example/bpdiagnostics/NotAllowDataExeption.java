package com.example.bpdiagnostics;

/**
 * Created by mykola on 08.06.17.
 */

public class NotAllowDataExeption extends Exception {
    public NotAllowDataExeption(String message) {
        super(message);
    }
}