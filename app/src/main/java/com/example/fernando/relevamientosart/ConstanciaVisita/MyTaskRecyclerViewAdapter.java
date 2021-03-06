package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Excepciones.ValidationException;
import Helpers.DBHelper;
import Helpers.PDFHelper;
import Modelo.Arquitectura.GestorPDF;
import Modelo.Enums.EnumStatus;
import Modelo.Managers.ResultManager;
import Modelo.Result;
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
                        try {
                            if (garantizarPermisosDeEscritura()) {
                                DBHelper helper = ((MainActivity) view.getContext()).getHelper();
                                new GestorPDF(helper).GenerarPDFParaTarea(holder.mItem);
                                Toast.makeText(view.getContext(), R.string.pdfGenerado, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ValidationException e) {
                            Toast.makeText(view.getContext(), R.string.tareaNoFinalizada, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    private boolean garantizarPermisosDeEscritura() {
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) view.getContext(), Manifest.permission.CAMERA)) {
                                Toast.makeText(view.getContext(), R.string.permission_rationale, Toast.LENGTH_LONG).show();
                                return false;
                            } else {
                                ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                return false;
                            }
                        }
                        return true;
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
