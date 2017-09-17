package com.example.fernando.relevamientosart.RAR;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Enums.EnumTareas;
import Modelo.Managers.ResultManager;
import Modelo.Managers.TaskManager;
import Modelo.RARResult;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;
import Modelo.WorkingMan;

public class RARFragment extends Fragment {

    private static final String ARG_VISIT = "visita";
    private Task mTarea;
    private Visit mVisit;
    private RARResult mResult;
    private DBHelper dbHelper;


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
        recyclerView.setAdapter(new MyTrabajadorRecyclerViewAdapter(mResult.workingMen, mListener));

        view.findViewById(R.id.btn_agregarTrabajador).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingMan workingMan = new WorkingMan(){{result = mResult;}};
                mResult.workingMen.add(workingMan);
                mListener.onTrabajadorSeleccionado(workingMan);
            }
        });

        return view;
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
            new ResultManager(dbHelper).persist(mResult);
        } catch (SQLException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnTrabajadoresFragmentInteractionListener {
        void onTrabajadorSeleccionado(WorkingMan workingMan);
    }
}
