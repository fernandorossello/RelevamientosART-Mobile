package com.example.fernando.relevamientosart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.relevamientosart.VisitFragment.OnListFragmentInteractionListener;


import java.util.List;

import Modelo.Visit;

public class MyVisitRecyclerViewAdapter extends RecyclerView.Adapter<MyVisitRecyclerViewAdapter.ViewHolder> {

    private final List<Visit> mListaVisitas;
    private final OnListFragmentInteractionListener mListener;

    public MyVisitRecyclerViewAdapter(List<Visit> items, OnListFragmentInteractionListener listener) {
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
        holder.mContentView.setText(holder.mItem.nombreInstitucion());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mContentView;
        public Visit mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
