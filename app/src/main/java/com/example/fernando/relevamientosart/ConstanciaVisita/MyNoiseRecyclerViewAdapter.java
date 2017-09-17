package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.fernando.relevamientosart.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import Modelo.Noise;


public class MyNoiseRecyclerViewAdapter extends RecyclerView.Adapter<MyNoiseRecyclerViewAdapter.ViewHolder> {

    private final List<Noise> mValues;

    public MyNoiseRecyclerViewAdapter(Collection<Noise> items) {
        mValues = new ArrayList<>(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_noise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).description);
        holder.mDbView.setText(formatearDecibeles(mValues.get(position).decibels) + " dB");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDbView;
        public final TextView mContentView;
        public Noise mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDbView = view.findViewById(R.id.medida_decibeles);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public String formatearDecibeles(Double decibeles){
        return new Formatter().format("%03.1f", decibeles).toString();
    }
}
