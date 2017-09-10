package com.example.fernando.relevamientosart.RAR;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.relevamientosart.RAR.RiskSelectorFragment.OnRiskSelectorFragmentInteractionListener;
import com.example.fernando.relevamientosart.R;

import java.util.List;

import Modelo.Risk;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Risk} and makes a call to the
 * specified {@link OnRiskSelectorFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRiskSelectorRecyclerViewAdapter extends RecyclerView.Adapter<MyRiskSelectorRecyclerViewAdapter.ViewHolder> {

    private final List<Risk> mValues;
    private final OnRiskSelectorFragmentInteractionListener mListener;

    public MyRiskSelectorRecyclerViewAdapter(List<Risk> items, OnRiskSelectorFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_riskselector, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).code);
        holder.mContentView.setText(mValues.get(position).description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onRiskSelectorFragmentInteraction();
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
        public final TextView mIdView;
        public final TextView mContentView;
        public Risk mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
