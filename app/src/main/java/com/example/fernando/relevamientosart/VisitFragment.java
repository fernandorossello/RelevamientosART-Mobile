package com.example.fernando.relevamientosart;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Managers.VisitManager;
import Modelo.Visit;


public class VisitFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnVisitSelectedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VisitFragment() {
    }

    @SuppressWarnings("unused")
    public static VisitFragment newInstance(int columnCount) {
        VisitFragment fragment = new VisitFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            try {
                DBHelper helper = ((MainActivity) getActivity()).getHelper();
                List<Visit> visitas = new VisitManager(helper).obtenerVisitasSincronizadas();
                 recyclerView.setAdapter(new MyVisitRecyclerViewAdapter(visitas, mListener));
            }catch(SQLException e){
                Toast.makeText(context, R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVisitSelectedListener) {
            mListener = (OnVisitSelectedListener) context;
        } else {
                throw new ClassCastException(context.toString()
                        + " must implement OnVisitSelectedListener");
            }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVisitSelectedListener {
        void OnVisitSelected(Visit visit);
    }

}
