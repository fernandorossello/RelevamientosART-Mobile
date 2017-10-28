package com.example.fernando.relevamientosart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fernando.relevamientosart.VisitFragment.OnVisitSelectedListener;


import java.text.SimpleDateFormat;
import java.util.List;

import Modelo.Enums.EnumStatus;
import Modelo.Visit;

public class MyVisitRecyclerViewAdapter extends RecyclerView.Adapter<MyVisitRecyclerViewAdapter.ViewHolder> {

    private final List<Visit> mListaVisitas;
    private final OnVisitSelectedListener mListener;

    public MyVisitRecyclerViewAdapter(List<Visit> items, OnVisitSelectedListener listener) {
        mListaVisitas = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_visit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mListaVisitas.get(position);
        holder.mInstitutionNameView.setText(holder.mItem.institution.name);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        holder.mFechaVisita.setText(sdf.format(holder.mItem.to_visit_on));
        holder.mEstadoVisita.setText(EnumStatus.getById(holder.mItem.status).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.OnVisitSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListaVisitas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mInstitutionNameView;
        public final ImageView mBadge;
        public final TextView mFechaVisita;
        public final TextView mEstadoVisita;
        public Visit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mInstitutionNameView = view.findViewById(R.id.nombreInstitucionVisita);
            mBadge = view.findViewById(R.id.iconoVisita);
            mFechaVisita = view.findViewById(R.id.fechaVisita);
            mEstadoVisita = view.findViewById(R.id.tv_estadoVisita);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mInstitutionNameView.getText() + "'";
        }
    }
}
