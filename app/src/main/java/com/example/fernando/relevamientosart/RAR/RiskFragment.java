package com.example.fernando.relevamientosart.RAR;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import Excepciones.ValidationException;
import Helpers.DBHelper;
import Helpers.ValidacionHelper;
import Modelo.Managers.WorkingManManager;
import Modelo.RARResult;
import Modelo.WorkingMan;

public class RiskFragment extends Fragment {

    private static final String ARG_WORKING_MAN = "working_Man";
    private WorkingMan mWorkingMan;
    private WorkingMan mWorkingManMock;

    private final Calendar myCalendar = Calendar.getInstance();

    private OnRiskFragmentInteractionListener mListener;

    private View.OnKeyListener OnBackListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                guardarWorkingMen(
                );
                return true;
            } else {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    getView().requestFocus();
                    return true;
                }
            }
            return false;
        }
    };


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RiskFragment() {
    }

    @SuppressWarnings("unused")
    public static RiskFragment newInstance(WorkingMan workingMan) {
        final RiskFragment fragment = new RiskFragment();
        Bundle args = new Bundle();
        args.putSerializable(RiskFragment.ARG_WORKING_MAN, workingMan);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(OnBackListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWorkingMan = (WorkingMan) getArguments().getSerializable(ARG_WORKING_MAN);
            mWorkingManMock = new WorkingMan();
            mWorkingManMock.fill(mWorkingMan);
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
        TextView tvSector = view.findViewById(R.id.tv_sector);

        tvSector.setText(mWorkingManMock.sector);
        tvSector.setOnKeyListener(OnBackListener);
        tvNombre.setText(mWorkingManMock.name);
        tvNombre.setOnKeyListener(OnBackListener);
        tvApellido.setText(mWorkingManMock.last_name);
        tvApellido.setOnKeyListener(OnBackListener);
        tvCuil.setText(mWorkingManMock.cuil);
        tvCuil.setOnKeyListener(OnBackListener);

        if (mWorkingManMock.checked_in_on != null)
            tvFechaIngreso.setText(formatearFecha(mWorkingManMock.checked_in_on));

        if (mWorkingManMock.exposed_from_at != null)
            tvFechaInicio.setText(formatearFecha(mWorkingManMock.exposed_from_at));

        if (mWorkingManMock.exposed_until_at != null)
            tvFechaFin.setText(formatearFecha(mWorkingManMock.exposed_until_at));


        recyclerView.setAdapter(new MyRiskRecyclerViewAdapter(mWorkingMan.risk_list));

        cargarListenerFechaIngreso(view);
        cargarListenerFechaInicio(view);
        cargarListenerFechaFin(view);

        view.findViewById(R.id.btn_agregarRiesgo)
            .setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     mListener.onNewRiskFragmentInteraction(mWorkingManMock);
                 }
             }
        );
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRiskFragmentInteractionListener) {
            mListener = (OnRiskFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRiskFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface OnRiskFragmentInteractionListener {
        void onNewRiskFragmentInteraction(WorkingMan workingMan);
    }

    private void guardarWorkingMen() {


        mWorkingManMock.name = ((EditText)getView().findViewById(R.id.tv_worker_name)).getText().toString();
        mWorkingManMock.last_name = ((EditText)getView().findViewById(R.id.tv_worker_lastName)).getText().toString();
        mWorkingManMock.cuil = ((EditText)getView().findViewById(R.id.tv_worker_cuil)).getText().toString();
        mWorkingManMock.sector = ((EditText)getView().findViewById(R.id.tv_sector)).getText().toString();


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

        try {

            String fechaIngreso =((EditText) getView().findViewById(R.id.tv_worker_fechaIngreso)).getText().toString();
            String fechaInicio = ((EditText)getView().findViewById(R.id.tv_worker_fechaInicio)).getText().toString();
            String fechaFin = ((EditText)getView().findViewById(R.id.tv_worker_fechaFin)).getText().toString();

            if (!fechaIngreso.isEmpty()) mWorkingManMock.checked_in_on = sdf.parse(fechaIngreso);
            if (!fechaInicio.isEmpty()) mWorkingManMock.exposed_from_at = sdf.parse(fechaInicio);
            if (!fechaFin.isEmpty()) mWorkingManMock.exposed_until_at = sdf.parse(fechaFin);

        } catch (ParseException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        DBHelper dbHelper = ((MainActivity)getActivity()).getHelper();
        final WorkingManManager manager = new WorkingManManager(dbHelper);
        try {
            mWorkingManMock.Validar();
            mWorkingMan.fill(mWorkingManMock);
            manager.persist(mWorkingMan);

            this.getActivity().onBackPressed();
        }
        catch (ValidationException ex){

            final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

            builder.setMessage(ex.getMessage())
                    .setTitle(R.string.Validacion)
                    .setPositiveButton(R.string.descartar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           getActivity().onBackPressed();
                        }
                    });
            builder.setNegativeButton(R.string.editar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();

            dialog.show();

        }
        catch (SQLException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
