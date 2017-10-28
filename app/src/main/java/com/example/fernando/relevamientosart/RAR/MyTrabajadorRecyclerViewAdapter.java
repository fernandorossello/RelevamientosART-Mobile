package com.example.fernando.relevamientosart.RAR;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;
import com.example.fernando.relevamientosart.RAR.RARFragment.OnTrabajadoresFragmentInteractionListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Employee;
import Modelo.Managers.AttendeeManager;
import Modelo.Managers.WorkingManManager;
import Modelo.WorkingMan;

public class MyTrabajadorRecyclerViewAdapter extends RecyclerView.Adapter<MyTrabajadorRecyclerViewAdapter.ViewHolder> {

    private final List<WorkingMan> mValues;
    private final OnTrabajadoresFragmentInteractionListener mListener;

    public MyTrabajadorRecyclerViewAdapter(Collection<WorkingMan> items, RARFragment.OnTrabajadoresFragmentInteractionListener listener) {
        mValues = new ArrayList<>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trabajador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombreView.setText(mValues.get(position).nombreCompleto());
        holder.mCuilView.setText(mValues.get(position).cuil);

        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage(R.string.borrarTrabajador)
                        .setTitle(R.string.borrarTrabajador_Title)
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                holder.mItem.result.working_men.remove(holder.mItem);
                                mListener.onBorrarTrabajador(holder.mItem);
                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                return true;
            }
        });
        
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
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
