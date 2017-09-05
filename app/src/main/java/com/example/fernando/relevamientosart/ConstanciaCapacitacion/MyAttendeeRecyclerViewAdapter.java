package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.relevamientosart.R;

import java.util.List;

import Modelo.Attendee;

public class MyAttendeeRecyclerViewAdapter extends RecyclerView.Adapter<MyAttendeeRecyclerViewAdapter.ViewHolder> {

    private List<Attendee> mValues;

    public MyAttendeeRecyclerViewAdapter(List<Attendee> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_attendee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAttendeeView.setText(mValues.get(position).name + " " + mValues.get(position).lastName);
        holder.mCuilView.setText(mValues.get(position).cuil);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAttendeeView;
        public final TextView mCuilView;
        public Attendee mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAttendeeView = (TextView) view.findViewById(R.id.attendee);
            mCuilView = (TextView) view.findViewById(R.id.cuil);
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}
