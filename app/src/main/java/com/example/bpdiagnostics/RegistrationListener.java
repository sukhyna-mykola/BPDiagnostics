package com.example.bpdiagnostics;

/**
 * Created by mykola on 06.06.17.
 */

public interface RegistrationListener {

    void registration();

    void signIn(long id);

    void cancel();
}
