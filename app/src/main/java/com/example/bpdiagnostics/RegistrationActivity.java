package com.example.bpdiagnostics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.bpdiagnostics.fragments.RegistarationFragment;
import com.example.bpdiagnostics.fragments.SignInFragment;
import com.example.bpdiagnostics.helpers.PreferencesManager;

public class RegistrationActivity extends AppCompatActivity implements RegistrationListener {
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = SignInFragment.newInstance();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        getSupportActionBar().setTitle("Вхід");

    }

    @Override
    public void registration() {
        getSupportActionBar().setTitle("Реєстрація");
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = RegistarationFragment.newInstance();
        manager.beginTransaction().add(R.id.fragment_container, fragment).addToBackStack("").commit();
    }

    @Override
    public void signIn(long id) {
        Intent i = new Intent();
        i.putExtra(PreferencesManager.KEY_ID, id);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void cancel() {
        getSupportActionBar().setTitle("Вхід");
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }
}
