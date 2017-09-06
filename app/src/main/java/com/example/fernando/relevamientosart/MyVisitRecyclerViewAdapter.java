package com.example.fernando.relevamientosart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fernando.relevamientosart.VisitFragment.OnVisitSelectedListener;


import org.w3c.dom.Text;

import java.util.List;

import Modelo.EnumStatus;
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
        holder.mInstitutionNameView.setText(holder.mItem.nombreInstitucion());
        holder.mFechaVisita.setText("17/08/2017");

        if(holder.mItem.status != 0) {
            EnumStatus status = EnumStatus.getById(holder.mItem.status);
            Context context = holder.mInstitutionNameView.getContext();
            /*int color;
            switch (status) {
                case ASIGNADA:
                    color = ContextCompat.getColor(context, R.color.colorAsignada);
                    break;
                case ENPROCESO:
                    color = ContextCompat.getColor(context, R.color.colorEnCurso);
                    break;
                case POSTERGADA:
                    color = ContextCompat.getColor(context, R.color.colorPostergada);
                    break;
                default:
                    color = ContextCompat.getColor(context, R.color.colorFinalizada);
                    break;
            }
            //holder.mStatusView.setText(status.name);
            holder.mBadge.setBackgroundColor(color);*/
        }

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
        public Visit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mInstitutionNameView = view.findViewById(R.id.nombreInstitucionVisita);
            mBadge = view.findViewById(R.id.iconoVisita);
            mFechaVisita = view.findViewById(R.id.fechaVisita);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mInstitutionNameView.getText() + "'";
        }
    }
}
