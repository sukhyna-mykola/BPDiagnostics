package com.example.bpdiagnostics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bpdiagnostics.R;
import com.example.bpdiagnostics.helpers.DBManager;
import com.example.bpdiagnostics.helpers.PreferencesManager;
import com.example.bpdiagnostics.models.UserDataDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MeasureFragment extends Fragment {


    private long id;

    private ArrayAdapter<String> arrayAdapter;

    private List<String> states = new ArrayList<String>(Arrays.asList("Відмінне", "Гарне", "Погане"));


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

        return v;
    }


    @OnClick(R.id.button_save_data)
    public void saveData() {

        String date = editTextDate.getText().toString();
        int sistolic = Integer.parseInt(editTextSistolic.getText().toString());
        int diastolic = Integer.parseInt(editTextDiastolic.getText().toString());
        int state = spinner.getSelectedItemPosition() + 1;

        if (id != -1) {
            UserDataDTO dataDTO = new UserDataDTO(id, date, sistolic, diastolic, state);
            long dataId = dbManager.addUserData(dataDTO);
            Toast.makeText(getContext(), "dataId = "+dataId, Toast.LENGTH_SHORT).show();
        }

    }
}
