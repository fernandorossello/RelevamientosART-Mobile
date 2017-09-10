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

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRiskSelectorFragmentInteractionListener}
 * interface.
 */
public class RiskSelectorFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_WORKING_MAN = "working_Man";
    private WorkingMan mWorkingMan;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnRiskSelectorFragmentInteractionListener mListener;
    private final MyRiskSelectorRecyclerViewAdapter madapter = new MyRiskSelectorRecyclerViewAdapter(mWorkingMan.riskList, mListener);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RiskSelectorFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RiskSelectorFragment newInstance(WorkingMan workingMan) {
        RiskSelectorFragment fragment = new RiskSelectorFragment();
        Bundle args = new Bundle();
        if (workingMan != null) {
            args.putSerializable(RiskSelectorFragment.ARG_WORKING_MAN, workingMan);
        } else {
            args.putSerializable(RiskSelectorFragment.ARG_WORKING_MAN, new WorkingMan());
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
        View view = inflater.inflate(R.layout.fragment_riskselector_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.riskSelectorList);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(madapter);
        //recyclerView.setAdapter(new MyRiskSelectorRecyclerViewAdapter(mWorkingMan.riskList, mListener));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRiskSelectorFragmentInteractionListener) {
            mListener = (OnRiskSelectorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRiskSelectorFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRiskSelectorFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRiskSelectorFragmentInteraction();
    }
}
