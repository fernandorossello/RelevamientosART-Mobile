package com.example.fernando.relevamientosart.RGRL;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.fernando.relevamientosart.R;
import com.example.fernando.relevamientosart.VisitDetalleFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Modelo.RGRLResult;
import Modelo.Task;
import Modelo.Visit;

public class PreguntaFragment extends Fragment {

    private static final String ARG_TASK = "tarea";
    private final Calendar myCalendar = Calendar.getInstance();

    private Task mTarea;

    public PreguntaFragment() {
        // Required empty public constructor
    }

    public static PreguntaFragment newInstance(Task task) {
        PreguntaFragment fragment = new PreguntaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK,task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTarea = (Task) getArguments().getSerializable(ARG_TASK);
            if(mTarea.result == null){
                mTarea.result = new RGRLResult();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pregunta, container, false);

        cargarListenerFechaRegul(view);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void cargarListenerFechaRegul(View view) {
        final EditText tvFechaRegul = view.findViewById(R.id.fechaRegul);
        final DatePickerDialog.OnDateSetListener dateSetRegulListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFechaRegul);
            }
        };

        tvFechaRegul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateSetRegulListener , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText editText) {
        editText.setText(formatearFecha(myCalendar.getTime()));
    }

    private String formatearFecha(Date date){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date);
    }
}