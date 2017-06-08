package com.example.bpdiagnostics;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bpdiagnostics.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {


    private ArrayList<User> users;

    private OnFragmentSearchLisntener lisntener;


    public UsersAdapter(ArrayList<User> users, OnFragmentSearchLisntener lisntener) {
        this.users = users;
        this.lisntener = lisntener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User user = users.get(position);
/*
        holder.textFirstName.setText(user.getFirstName());
        holder.textLastName.setText(user.getLastName());
        holder.textParentName.setText(user.getParent());*/
        holder.textName.setText(user.getLastName() + " " + user.getFirstName() + " " + user.getParent());
        holder.textEmail.setText(user.getEmail());
        holder.textBirthday.setText(user.getBirhday());
        holder.textSex.setText(user.getSex());
        holder.item.setCardBackgroundColor(Color.WHITE);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.item.setCardBackgroundColor(Color.LTGRAY);
                lisntener.showUserFragment(user.getId());
            }
        });

    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        /*    @BindView(R.id.text_first_name)
            TextView textFirstName;
            @BindView(R.id.text_last_name)
            TextView textLastName;
            @BindView(R.id.text_parent_name)
            TextView textParentName;*/
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_birthday)
        TextView textBirthday;
        @BindView(R.id.text_sex)
        TextView textSex;
        @BindView(R.id.text_email)
        TextView textEmail;
        @BindView(R.id.item)
        CardView item;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}



