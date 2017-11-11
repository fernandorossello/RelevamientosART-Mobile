package com.example.fernando.relevamientosart;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Managers.VisitManager;
import Modelo.Visit;


public class VisitFragment extends Fragment {

    private OnVisitSelectedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VisitFragment() {
    }

    @SuppressWarnings("unused")
    public static VisitFragment newInstance() {
        return new VisitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_list, container, false);


            RecyclerView recyclerView = view.findViewById(R.id.visit_list);

            try {
                DBHelper helper = ((MainActivity) getActivity()).getHelper();
                final int userId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("idUsuario",-1);
                List<Visit> visitas = new VisitManager(helper).obtenerVisitasSincronizadas(userId);
                recyclerView.setAdapter(new MyVisitRecyclerViewAdapter(visitas, mListener));

                TextView emptyView = view.findViewById(R.id.empty_view);

                if (visitas.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            } catch(SQLException e){
                Toast.makeText(getContext(), R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
            }

        setHasOptionsMenu(false);
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
