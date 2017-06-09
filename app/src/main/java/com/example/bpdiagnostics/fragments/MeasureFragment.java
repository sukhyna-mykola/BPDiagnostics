package com.example.bpdiagnostics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bpdiagnostics.NotAllowDataExeption;
import com.example.bpdiagnostics.R;
import com.example.bpdiagnostics.helpers.DBManager;
import com.example.bpdiagnostics.helpers.PreferencesManager;
import com.example.bpdiagnostics.models.UserDataDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MeasureFragment extends Fragment {


    private long id;

    private ArrayAdapter<String> arrayAdapter;

    private String [] states;


    @BindView(R.id.editText_date)
    EditText editTextDate;
    @BindView(R.id.editText_sistolic)
    EditText editTextSistolic;
    @BindView(R.id.editText_diastolic)
    EditText editTextDiastolic;
    @BindView(R.id.spinner_state)
    Spinner spinner;


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private Unbinder unbinder;

    private DBManager dbManager;

    public static MeasureFragment newInstance(long id) {
        MeasureFragment fragment = new MeasureFragment();
        Bundle args = new Bundle();
        args.putLong(PreferencesManager.KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(PreferencesManager.KEY_ID);
        }
        dbManager = DBManager.getInstance(getContext());
        states = getResources().getStringArray(R.array.health_states);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_measure, container, false);
        unbinder = ButterKnife.bind(this, v);

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });

        editTextDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "            ";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 10; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 12) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {

                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));
                        int hour = Integer.parseInt(clean.substring(8, 10));
                        int minite = Integer.parseInt(clean.substring(10, 12));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);


                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        cal.set(Calendar.DAY_OF_MONTH, day);
                        if (hour > 23) hour = 23;
                        cal.set(Calendar.HOUR_OF_DAY, hour);

                        if (minite > 59) minite = 59;
                        cal.set(Calendar.MINUTE, minite);

                        clean = String.format("%02d%02d%02d%02d%02d", day, mon, year, hour, minite);
                    }

                    clean = String.format("%s-%s-%s %s:%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8),
                            clean.substring(8, 10),
                            clean.substring(10, 12)
                    );

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editTextDate.setText(current);
                    editTextDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }


    @OnClick(R.id.button_save_data)
    public void saveData() {

        try {
            String date = editTextDate.getText().toString();
            if (!checkCorrectDate(date)) throw new NotAllowDataExeption(getString(R.string.error_date_incorrect));
            int sistolic = Integer.parseInt(editTextSistolic.getText().toString());
            int diastolic = Integer.parseInt(editTextDiastolic.getText().toString());
            if (sistolic < 10 || sistolic > 200 || diastolic < 10 || diastolic > 200)
                throw new NotAllowDataExeption(getString(R.string.unallowed_data));

            int state = spinner.getSelectedItemPosition() + 1;

            if (id != -1) {
                UserDataDTO dataDTO = new UserDataDTO(id, date, sistolic, diastolic, state);
                dbManager.addUserData(dataDTO);
                Toast.makeText(getContext(), R.string.added, Toast.LENGTH_SHORT).show();
            }
        } catch (NotAllowDataExeption e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.error_incorrect_data, Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkCorrectDate(String d) {

        String date = new String(d.replaceAll("-", ""));
        date = new String(date.replaceAll(" ", ""));
        date = new String(date.replaceAll(":", ""));
        if (date.isEmpty() || !(date.matches("[0-9]+")))
            return false;
        return true;
    }
}
