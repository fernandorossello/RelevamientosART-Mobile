package com.example.fernando.relevamientosart.RAR;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.fernando.relevamientosart.R;
import java.util.List;

import Modelo.Managers.RiskManager;
import Modelo.Risk;
import Modelo.WorkingMan;

public class MyRiskSelectorRecyclerViewAdapter extends RecyclerView.Adapter<MyRiskSelectorRecyclerViewAdapter.ViewHolder> {

    private final List<Risk> mValues;
    private final WorkingMan mWorkingMan;

    public MyRiskSelectorRecyclerViewAdapter(WorkingMan workingMan) {
        mValues = new RiskManager().obtenerRiesgos();
        mWorkingMan = workingMan;
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
        holder.mContentView.setText(mValues.get(position).toString());

        if (mWorkingMan.riskList.contains(holder.mItem)){
            holder.mImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mImageView.setVisibility(View.INVISIBLE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWorkingMan.riskList.contains(holder.mItem))
                {
                    holder.mItem.workingMan = null;
                    mWorkingMan.riskList.remove(holder.mItem);
                    holder.mImageView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.mItem.workingMan = mWorkingMan;
                    mWorkingMan.riskList.add(holder.mItem);
                    holder.mImageView.setVisibility(View.VISIBLE);
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
        public final TextView mContentView;
        public Risk mItem;
        public final ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
            mImageView = view.findViewById(R.id.iv_selected);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
