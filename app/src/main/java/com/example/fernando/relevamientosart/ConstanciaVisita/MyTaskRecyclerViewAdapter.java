package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.query.Exists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Helpers.DBHelper;
import Helpers.PDFHelper;
import Modelo.Managers.ResultManager;
import Modelo.Task;

public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private List<Task> mValues;

    public MyTaskRecyclerViewAdapter(Collection<Task> items) {
        mValues = new ArrayList<>(items);;
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
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage(R.string.generarPDf)
                .setTitle(R.string.generarPDF_Title)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     try{
                        ResultManager resultManager = new ResultManager(((MainActivity)view.getContext()).getHelper());
                        new PDFHelper().crearPDF(resultManager.getResult(holder.mItem),holder.mItem);
                         Toast.makeText(view.getContext(), R.string.pdfGenerado, Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });

                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();

                    dialog.show();
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
