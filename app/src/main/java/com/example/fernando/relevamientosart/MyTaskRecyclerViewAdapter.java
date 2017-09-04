package com.example.fernando.relevamientosart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Modelo.Task;

public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private List<Task> mValues;

    public MyTaskRecyclerViewAdapter(List<Task> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTareaView.setText(mValues.get(position).getTypeName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTareaView;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTareaView = view.findViewById(R.id.tarea);
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}
