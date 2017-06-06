package com.example.bpdiagnostics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bpdiagnostics.R;
import com.example.bpdiagnostics.RegistrationListener;
import com.example.bpdiagnostics.helpers.DBManager;
import com.example.bpdiagnostics.helpers.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SignInFragment extends Fragment {

    private RegistrationListener mListener;

    private DBManager dbManager;
    private PreferencesManager prefManager;

    @BindView(R.id.editText_email)
    EditText editTextEmail;
    @BindView(R.id.editText_password)
    EditText editTextPassword;

    private Unbinder unbinder;


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = DBManager.getInstance(getContext());
        prefManager = PreferencesManager.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationListener) {
            mListener = (RegistrationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.button_registration)
    public void registration() {
        mListener.registration();
    }

    @OnClick(R.id.button_sign_in)
    public void signIn() {

        String email = editTextEmail.getText().toString();
        String password = editTextEmail.getText().toString();

        long id = dbManager.getIdByEmailAndPassword(email, password);

        if (id != -1) {
            mListener.signIn(id);
            prefManager.saveUserId(id);
        } else {
            Toast.makeText(getContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
        }

    }

}
