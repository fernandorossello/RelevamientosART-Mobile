package com.example.fernando.relevamientosart.RAR;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.relevamientosart.R;
import com.example.fernando.relevamientosart.RAR.RARFragment.OnTrabajadoresFragmentInteractionListener;

import java.util.List;

import Modelo.WorkingMan;

public class MyTrabajadorRecyclerViewAdapter extends RecyclerView.Adapter<MyTrabajadorRecyclerViewAdapter.ViewHolder> {

    private final List<WorkingMan> mValues;
    private final OnTrabajadoresFragmentInteractionListener mListener;

    public MyTrabajadorRecyclerViewAdapter(List<WorkingMan> items, RARFragment.OnTrabajadoresFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trabajador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombreView.setText(mValues.get(position).nombreCompleto());
        holder.mCuilView.setText(mValues.get(position).cuil);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTrabajadorSeleccionado(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreView;
        public final TextView mCuilView;
        public WorkingMan mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNombreView = view.findViewById(R.id.tv_wm_nombre);
            mCuilView = view.findViewById(R.id.tv_wm_cuil);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCuilView.getText() + "'";
        }
    }
}
