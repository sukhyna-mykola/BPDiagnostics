package com.example.bpdiagnostics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bpdiagnostics.OnFragmentSearchLisntener;
import com.example.bpdiagnostics.R;
import com.example.bpdiagnostics.UsersAdapter;
import com.example.bpdiagnostics.helpers.DBManager;
import com.example.bpdiagnostics.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class UserSearchFragment extends Fragment implements OnFragmentSearchLisntener {


    private OnFragmentSearchLisntener mListener;

    @BindView(R.id.editText_search_name)
    EditText editTextsearchName;
    @BindView(R.id.users_list)
    RecyclerView userList;

    private Unbinder unbinder;
    private UsersAdapter adapter;
    private ArrayList<User> users;
    private DBManager dbManager;


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static UserSearchFragment newInstance() {
        UserSearchFragment fragment = new UserSearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = DBManager.getInstance(getContext());
        users = new ArrayList<>();
        adapter = new UsersAdapter(users, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_search, container, false);

        unbinder = ButterKnife.bind(this, v);

        userList.setLayoutManager(new LinearLayoutManager(getContext()));
        userList.setAdapter(adapter);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSearchLisntener) {
            mListener = (OnFragmentSearchLisntener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentSearchLisntener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @OnClick(R.id.button_search)
    public void search() {
        String query = editTextsearchName.getText().toString();
        users.clear();
        users.addAll(dbManager.getUserByName(query));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void showUserFragment(long id) {
        mListener.showUserFragment(id);
    }


}
