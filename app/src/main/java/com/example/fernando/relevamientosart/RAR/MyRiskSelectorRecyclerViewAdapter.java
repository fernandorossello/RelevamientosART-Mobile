package com.example.fernando.relevamientosart.RAR;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fernando.relevamientosart.RAR.RiskSelectorFragment.OnRiskSelectorFragmentInteractionListener;
import com.example.fernando.relevamientosart.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Modelo.Managers.RiskManager;
import Modelo.Risk;
import Modelo.WorkingMan;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Risk} and makes a call to the
 * specified {@link OnRiskSelectorFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRiskSelectorRecyclerViewAdapter extends RecyclerView.Adapter<MyRiskSelectorRecyclerViewAdapter.ViewHolder> {

    private final List<Risk> mValues;
    private final List<Risk> mRiesgosDelTrabajador;
    private final OnRiskSelectorFragmentInteractionListener mListener;
    private final WorkingMan mWorkingMan;

    public MyRiskSelectorRecyclerViewAdapter(WorkingMan workingMan, OnRiskSelectorFragmentInteractionListener listener) {
        mValues = new RiskManager().obtenerRiesgos();
        mRiesgosDelTrabajador = new ArrayList<>(workingMan.riskList);
        mWorkingMan = workingMan;
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

        if (mRiesgosDelTrabajador.contains(holder.mItem)){
            holder.mImageView.setVisibility(View.VISIBLE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRiesgosDelTrabajador.contains(holder.mItem))
                {
                    mRiesgosDelTrabajador.remove(holder.mItem);
                    holder.mImageView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    mRiesgosDelTrabajador.add(holder.mItem);
                    holder.mImageView.setVisibility(View.VISIBLE);
                }
                    //mListener.onRiskSelectorFragmentInteraction();
                mWorkingMan.riskList = mRiesgosDelTrabajador;
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
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
            mImageView = view.findViewById(R.id.iv_selected);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
