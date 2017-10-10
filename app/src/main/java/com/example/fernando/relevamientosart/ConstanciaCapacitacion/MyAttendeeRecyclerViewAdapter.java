package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Attendee;
import Modelo.Managers.AttendeeManager;
import Modelo.Managers.WorkingManManager;

public class MyAttendeeRecyclerViewAdapter extends RecyclerView.Adapter<MyAttendeeRecyclerViewAdapter.ViewHolder> {

    private List<Attendee> mValues;
    private ConstanciaCapacitacionFragment.OnNewAttendeeInteractionListener mListener;


    public MyAttendeeRecyclerViewAdapter(Collection<Attendee> items, ConstanciaCapacitacionFragment.OnNewAttendeeInteractionListener listener) {
        mValues = new ArrayList<>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_attendee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAttendeeView.setText(mValues.get(position).name + " " + mValues.get(position).lastName);
        holder.mCuilView.setText(mValues.get(position).cuil);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onNewAttendee(holder.mItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage(R.string.borrarTrabajador)
                        .setTitle(R.string.borrarTrabajador_Title)
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAttendeeView;
        public final TextView mCuilView;
        public Attendee mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAttendeeView = (TextView) view.findViewById(R.id.attendee);
            mCuilView = (TextView) view.findViewById(R.id.cuil);
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}
