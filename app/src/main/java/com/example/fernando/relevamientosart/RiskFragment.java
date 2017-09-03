package com.example.fernando.relevamientosart;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Modelo.Managers.RiskManager;
import Modelo.Risk;
import Modelo.WorkingMan;

public class RiskFragment extends Fragment {

    private static final String ARG_WORKING_MAN = "working_Man";
    private WorkingMan mWorkingMan;

    private final Calendar myCalendar = Calendar.getInstance();
    private final List<Risk> riesgos = new ArrayList<>();
    private final MyRiskRecyclerViewAdapter madapter = new MyRiskRecyclerViewAdapter(riesgos);


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RiskFragment() {
    }

    @SuppressWarnings("unused")
    public static RiskFragment newInstance(WorkingMan workingMan) {
        RiskFragment fragment = new RiskFragment();
        Bundle args = new Bundle();

        if (workingMan != null) {
            args.putSerializable(RiskFragment.ARG_WORKING_MAN, workingMan);
        } else {
            args.putSerializable(RiskFragment.ARG_WORKING_MAN, new WorkingMan());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWorkingMan = (WorkingMan) getArguments().getSerializable(ARG_WORKING_MAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riesgos_trabajador, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.riskList);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        TextView tvNombre = view.findViewById(R.id.tv_worker_name);
        TextView tvApellido = view.findViewById(R.id.tv_worker_lastName);
        TextView tvCuil = view.findViewById(R.id.tv_worker_cuil);
        TextView tvFechaIngreso = view.findViewById(R.id.tv_worker_fechaIngreso);
        TextView tvFechaInicio = view.findViewById(R.id.tv_worker_fechaInicio);
        TextView tvFechaFin = view.findViewById(R.id.tv_worker_fechaFin);


        tvNombre.setText(mWorkingMan.name);
        tvApellido.setText(mWorkingMan.lastName);
        tvCuil.setText(mWorkingMan.cuil);

        if(mWorkingMan.checked_in_on != null)
            tvFechaIngreso.setText(formatearFecha(mWorkingMan.checked_in_on));

        if(mWorkingMan.exposed_from_at != null)
            tvFechaInicio.setText(formatearFecha(mWorkingMan.exposed_from_at));

        if(mWorkingMan.exposed_until_at != null)
            tvFechaFin.setText(formatearFecha(mWorkingMan.exposed_until_at));

        recyclerView.setAdapter(madapter);

        cargarListenerFechaIngreso(view);
        cargarListenerFechaInicio(view);
        cargarListenerFechaFin(view);

        view.findViewById(R.id.btn_agregarRiesgo)
            .setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     riesgos.add(new Risk(){{
                         code="00001";
                         description="Agente de riesgo";
                     }});
                    madapter.notifyDataSetChanged();
                 }
             }
        );

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

    private void cargarListenerFechaIngreso(View view) {
        final EditText tvFechaIngreso= view.findViewById(R.id.tv_worker_fechaIngreso);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFechaIngreso);
            }
        };

        tvFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void cargarListenerFechaInicio(View view) {
        final EditText tvFechaInicio= view.findViewById(R.id.tv_worker_fechaInicio);
        final DatePickerDialog.OnDateSetListener dateSetInicioListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFechaInicio);
            }
        };

        tvFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateSetInicioListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void cargarListenerFechaFin(View view) {
        final EditText tvFechaFin= view.findViewById(R.id.tv_worker_fechaFin);
        final DatePickerDialog.OnDateSetListener dateSetFinListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFechaFin);
            }
        };

        tvFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateSetFinListener, myCalendar
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
