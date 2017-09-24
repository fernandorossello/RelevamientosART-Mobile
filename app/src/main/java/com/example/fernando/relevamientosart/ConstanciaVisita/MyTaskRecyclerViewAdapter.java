package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.R;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.query.Exists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Helpers.PDFHelper;
import Modelo.Task;

public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private List<Task> mValues;

    public MyTaskRecyclerViewAdapter(Collection<Task> items) {
        mValues = new ArrayList<Task>(items);;
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

        holder.mTareaView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                try {
                    new PDFHelper().crearPDF(holder.mItem);
                }catch (Exception ex){
                    Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(view.getContext(), "Archivo creado", Toast.LENGTH_SHORT).show();
            }
        });
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
