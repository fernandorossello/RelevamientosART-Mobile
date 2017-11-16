package com.example.fernando.relevamientosart.RAR;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.Date;
import java.util.List;

import Excepciones.ValidationException;
import Helpers.DBHelper;
import Modelo.Enums.EnumStatus;
import Modelo.Enums.EnumTareas;
import Modelo.Managers.ResultManager;
import Modelo.Managers.WorkingManManager;
import Modelo.RARResult;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;
import Modelo.WorkingMan;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class RARFragment extends Fragment {

    private static final String ARG_VISIT = "visita";
    private Task mTarea;
    private Visit mVisit;
    private RARResult mResult;
    private DBHelper dbHelper;
    private ResultManager mResultManager;



    private OnTrabajadoresFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RARFragment() {
    }

    @SuppressWarnings("unused")
    public static RARFragment newInstance(Visit visit) {
        RARFragment fragment = new RARFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_VISIT,visit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mVisit = (Visit) getArguments().getSerializable(ARG_VISIT);
            mTarea = mVisit.obtenerTarea(EnumTareas.RAR);
            dbHelper = ((MainActivity)getActivity()).getHelper();
            mResultManager = new ResultManager(dbHelper);
            Result result = new ResultManager(dbHelper).getResult(mTarea);

            if(result == null){
                mResult = new RARResult();
                mResult.task = mTarea;
            } else {
                mResult = (RARResult) result;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rar, container, false);

        RecyclerView recyclerView  = view.findViewById(R.id.workerList);
        
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DepurarListaEmpleados();

        TextView emptyView = view.findViewById(R.id.empty_view);

        if (mResult.working_men.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(new MyTrabajadorRecyclerViewAdapter(mResult.working_men, mListener));


        view.findViewById(R.id.btn_agregarTrabajador).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingMan workingMan = new WorkingMan(){{result = mResult;}};
                mResult.working_men.add(workingMan);
                mListener.onTrabajadorSeleccionado(workingMan);
            }
        });

        getActivity().setTitle(R.string.titulo_RAR);



        return view;
    }

    private void DepurarListaEmpleados() {
        WorkingManManager workingManManager = new WorkingManManager(dbHelper);

        for (WorkingMan wm : mResult.working_men) {
            try{
                wm.Validar();
            }catch (ValidationException ex){
                try {
                    mResult.working_men.remove(wm);
                    workingManManager.delete(wm);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrabajadoresFragmentInteractionListener) {
            mListener = (OnTrabajadoresFragmentInteractionListener) context;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(mResult.getStatus() == EnumStatus.FINALIZADA){
                mResult.completed_at = new Date();
            }
            new ResultManager(dbHelper).persist(mResult);
        } catch (SQLException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnTrabajadoresFragmentInteractionListener {
        void onTrabajadorSeleccionado(WorkingMan workingMan);
        void onBorrarTrabajador(WorkingMan workingMan);
    }

    @Override
    public void onResume() {
        super.onResume();

        DepurarListaEmpleados();
    }
}
