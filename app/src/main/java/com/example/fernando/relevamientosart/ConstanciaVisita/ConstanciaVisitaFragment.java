package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;
import com.itextpdf.text.DocumentException;

import java.sql.SQLException;

import Excepciones.ValidationException;
import Helpers.PDFHelper;
import Modelo.Arquitectura.GestorFirebase;
import Modelo.Arquitectura.GestorPDF;
import Modelo.Enums.EnumStatus;
import Modelo.FirebaseFile;
import Modelo.Managers.ResultManager;
import Modelo.Result;
import Modelo.Visit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import Helpers.DBHelper;
import Modelo.Managers.VisitManager;
import Modelo.VisitRecord;


public class ConstanciaVisitaFragment extends Fragment {
    private static final String ARG_visita = "visita";

    private Visit mVisit;
    private OnEventoConstanciaListener mListener;

    public ConstanciaVisitaFragment() {
        // Required empty public constructor
    }

    public static ConstanciaVisitaFragment newInstance(Visit visit) {
        ConstanciaVisitaFragment fragment = new ConstanciaVisitaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_visita, visit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVisit = (Visit)getArguments().getSerializable(ARG_visita);
            if(mVisit.visit_record == null){
                mVisit.visit_record = new VisitRecord();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_constancia_visita, container, false);

        RecyclerView recyclerView  = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(mVisit.tasks));

        EditText etObservaciones = view.findViewById(R.id.et_observaciones);
        etObservaciones.setText(mVisit.visit_record.observations);


        TextView tvVerFotos = view.findViewById(R.id.tv_ver_fotos);

        if(mVisit.images == null || mVisit.images.isEmpty()) {
            tvVerFotos.setVisibility(View.INVISIBLE);
        }
        tvVerFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnVerFotosClick(mVisit);
            }
        });

        @SuppressLint("WrongViewCast") AppCompatImageButton btnFoto = view.findViewById(R.id.btn_camera);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnTomarFoto();
            }
        });

        FloatingActionButton btnGuardar = view.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guardarConstanciaDeVisita(view);

                    if(garantizarPermisosDeEscritura()) {

                        DBHelper helper = ((MainActivity)getActivity()).getHelper();
                        ResultManager resultManager = new ResultManager(helper);
                        List<Result> results = resultManager.getResults(mVisit);

                        for (Result result: results) {
                            if(result == null || result.getStatus() != EnumStatus.FINALIZADA) {
                                throw new ValidationException("Debe completar todas las tareas primero");
                            }
                        }

                        new PDFTask(helper).execute(mVisit);

                        Toast.makeText(getContext(), R.string.guardadoYpdf, Toast.LENGTH_SHORT).show();

                        mListener.OnGuardarConstanciaDeVisita();
                    }
                }catch (ValidationException ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(getContext(), R.string.error_pdf_constancia_visita, Toast.LENGTH_SHORT).show();
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

        @SuppressLint("WrongViewCast") AppCompatImageButton btnAudio = view.findViewById(R.id.btn_audio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnMedirRuido();
            }
        });

        return view;
    }

    private void guardarConstanciaDeVisita(View view) {
        EditText etObservaciones = ((View)view.getParent()).findViewById(R.id.et_observaciones);
        mVisit.visit_record.observations = etObservaciones.getText().toString();

        mVisit.visit_record.completed_at = new Date();


        DBHelper dbHelper = ((MainActivity)view.getContext()).getHelper();

        try {
            (new VisitManager(dbHelper)).persist(mVisit);
        }catch (SQLException ex){
            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventoConstanciaListener) {
            mListener = (OnEventoConstanciaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventoConstanciaListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnEventoConstanciaListener {
        void OnTomarFoto();
        void OnVerFotosClick(Visit visit);
        void OnGuardarConstanciaDeVisita();
        void OnMedirRuido();
    }

    private class PDFTask extends AsyncTask<Visit, Integer, Visit> {

        private final DBHelper _dbHelper;

        public PDFTask(DBHelper dbHelper){
            this._dbHelper = dbHelper;
        }
        
        protected Visit doInBackground(Visit... visit) {

            Visit visita = visit[0];

            try {
                new GestorPDF(_dbHelper).GenerarPDFParaVisita(visita);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return visita;
        }

        @Override
        protected void onPostExecute(Visit visit) {
            GestorFirebase gestorFirebase = new GestorFirebase(){

                @Override
                public void persistirEntidad(FirebaseFile archivo) throws SQLException {
                    new ResultManager(_dbHelper).persist((Result)archivo);
                }

                @Override
                public String getNombreCarpetaRemota() {
                    return "PDF/";
                }
            };

            List<Result> results = new ResultManager(_dbHelper).getResults(visit);

            for (Result result:results) {
                gestorFirebase.subirArchivo(result);
            }
        }

    }

}
