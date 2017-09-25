package com.example.fernando.relevamientosart.RAR;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fernando.relevamientosart.R;

import Modelo.WorkingMan;

public class RiskSelectorFragment extends Fragment {

    private static final String ARG_WORKING_MAN = "working_Man";
    private WorkingMan mWorkingMan;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RiskSelectorFragment() {
    }


    @SuppressWarnings("unused")
    public static RiskSelectorFragment newInstance(WorkingMan workingMan) {
        RiskSelectorFragment fragment = new RiskSelectorFragment();
        Bundle args = new Bundle();
        args.putSerializable(RiskSelectorFragment.ARG_WORKING_MAN, workingMan);
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
        View view = inflater.inflate(R.layout.fragment_riskselector_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.riskSelectorList);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyRiskSelectorRecyclerViewAdapter(mWorkingMan));

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
