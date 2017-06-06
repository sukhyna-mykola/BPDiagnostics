package com.example.bpdiagnostics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bpdiagnostics.R;
import com.example.bpdiagnostics.RegistrationListener;
import com.example.bpdiagnostics.helpers.DBManager;
import com.example.bpdiagnostics.helpers.PreferencesManager;
import com.example.bpdiagnostics.models.User;
import com.example.bpdiagnostics.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class RegistarationFragment extends Fragment {


    private RegistrationListener mListener;


    @BindView(R.id.editText_email)
    EditText editTextEmail;
    @BindView(R.id.editText_password)
    EditText editTextPassword;
    @BindView(R.id.editText_first_name)
    EditText editTextFirstName;
    @BindView(R.id.editText_last_name)
    EditText editTextLastName;
    @BindView(R.id.editText_parent_name)
    EditText editTextParentName;
    @BindView(R.id.editText_birthday)
    EditText editTextBirthday;
    @BindView(R.id.checkBox_doctor_no)
    CheckBox editTextDoctorNo;
    @BindView(R.id.checkBox_doctor_yes)
    CheckBox editTextDoctorYes;
    @BindView(R.id.checkBox_men)
    CheckBox editTextMen;
    @BindView(R.id.checkBox_women)
    CheckBox editTextWomen;

    private Unbinder unbinder;

    private DBManager dbManager;
    private PreferencesManager prefManager;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static RegistarationFragment newInstance() {
        RegistarationFragment fragment = new RegistarationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = DBManager.getInstance(getContext());
        prefManager = PreferencesManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registaration, container, false);
        unbinder = ButterKnife.bind(this, v);

        editTextDoctorNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextDoctorYes.setChecked(false);
                } else {
                    editTextDoctorYes.setChecked(true);
                }
            }
        });
        editTextDoctorYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextDoctorNo.setChecked(false);
                } else {
                    editTextDoctorNo.setChecked(true);
                }
            }
        });

        editTextWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextMen.setChecked(false);
                } else {
                    editTextMen.setChecked(true);
                }
            }
        });

        editTextMen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextWomen.setChecked(false);
                } else {
                    editTextWomen.setChecked(true);
                }
            }
        });


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

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String parentName = editTextParentName.getText().toString();
        String birthday = editTextBirthday.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextEmail.getText().toString();

        int doctor = Constants.DOCTOR_NO;

        if (editTextDoctorYes.isChecked()) {
            doctor = Constants.DOCTOR_YES;
        }

        String sex = Constants.MEN;

        if (editTextWomen.isChecked()) {
            sex = Constants.WOMEN;
        }

        User user = new User(firstName, lastName, parentName, birthday, sex, doctor, email, password);

        long id = dbManager.addUser(user);


        if (id != -1) {
            prefManager.saveUserId(id);
            mListener.signIn(id);
        } else {
            Toast.makeText(getContext(), "user did not added, ERROR!!!", Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.button_cencel)
    public void cancel() {
        mListener.cancel();
    }

}
