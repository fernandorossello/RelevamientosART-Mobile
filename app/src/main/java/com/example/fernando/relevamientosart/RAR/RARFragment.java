package com.example.fernando.relevamientosart.RAR;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fernando.relevamientosart.R;

import java.util.List;

import Modelo.Managers.WorkingManManager;
import Modelo.RARResult;
import Modelo.Task;
import Modelo.WorkingMan;

public class RARFragment extends Fragment {

    private static final String ARG_TASK = "tarea";
    private Task mTarea;

    private OnTrabajadoresFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RARFragment() {
    }

    @SuppressWarnings("unused")
    public static RARFragment newInstance(Task task) {
        RARFragment fragment = new RARFragment();

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
                mTarea.result = new RARResult();
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
        List<WorkingMan> lista = new WorkingManManager().trabajadoresEjemplo();
        recyclerView.setAdapter(new MyTrabajadorRecyclerViewAdapter(lista, mListener));

        view.findViewById(R.id.btn_agregarTrabajador).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mListener.onTrabajadorNuevo();
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

    public interface OnTrabajadoresFragmentInteractionListener {
        void onTrabajadorNuevo();
        void onTrabajadorSeleccionado(WorkingMan workingMan);
    }
}
