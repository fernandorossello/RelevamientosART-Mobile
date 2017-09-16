package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.fernando.relevamientosart.RAR.RARFragment;


import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Date;

import Helpers.DBHelper;
import Modelo.Managers.VisitManager;
import Modelo.Visit;
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
            if(mVisit.visitRecord == null){
                mVisit.visitRecord = new VisitRecord();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constancia_visita, container, false);

        RecyclerView recyclerView  = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(mVisit.tasks));

        EditText etObservaciones = view.findViewById(R.id.et_observaciones);
        etObservaciones.setText(mVisit.visitRecord.observations);

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

        AppCompatImageButton btnFoto = view.findViewById(R.id.btn_camera);
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
                Toast.makeText(view.getContext(), R.string.guardado, Toast.LENGTH_SHORT).show();
                guardarConstanciaDeVisita(view);
                mListener.OnGuardarConstanciaDeVisita();
            }
        });

        AppCompatImageButton btnAudio = view.findViewById(R.id.btn_audio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Medir audio", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void guardarConstanciaDeVisita(View view) {
        EditText etObservaciones = ((View)view.getParent()).findViewById(R.id.et_observaciones);
        mVisit.visitRecord.observations = etObservaciones.getText().toString();

        mVisit.visitRecord.completed_at = new Date();


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
                    + " must implement OnTrabajadoresFragmentInteractionListener");
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
    }

}
