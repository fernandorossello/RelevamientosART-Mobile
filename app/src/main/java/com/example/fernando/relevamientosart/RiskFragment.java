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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Modelo.Risk;
import Modelo.WorkingMan;

public class RiskFragment extends Fragment {

    private static final String ARG_WORKING_MAN = "working_Man";
    private WorkingMan mWorkingMan;


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
        tvNombre.setText(mWorkingMan.nombreCompleto());

        List<Risk> riesgos = new ArrayList<>();
        recyclerView.setAdapter(new MyRiskRecyclerViewAdapter(riesgos));

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
}
