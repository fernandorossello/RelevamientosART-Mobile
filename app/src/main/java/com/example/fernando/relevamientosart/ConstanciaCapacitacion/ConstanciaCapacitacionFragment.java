package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fernando.relevamientosart.R;

import java.util.ArrayList;
import java.util.List;

import Modelo.Attendee;
import Modelo.CAPResult;
import Modelo.Managers.AttendeeManager;
import Modelo.Task;

public class ConstanciaCapacitacionFragment extends Fragment {


    private static final String ARG_TASK = "tarea";
    private Task mTarea;

    private OnNewAttendeeInteractionListener mListener;

    public ConstanciaCapacitacionFragment() {
        // Required empty public constructor
    }

    public static ConstanciaCapacitacionFragment newInstance(Task task) {
        ConstanciaCapacitacionFragment fragment = new ConstanciaCapacitacionFragment();
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
                mTarea.result = new CAPResult();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constancia_capacitacion, container, false);

        RecyclerView recyclerView  = view.findViewById(R.id.attendeeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        List<Attendee> attendees = new AttendeeManager().attendeesEjemplo();

        recyclerView.setAdapter(new MyAttendeeRecyclerViewAdapter(attendees, mListener));

        FloatingActionButton btnAgregarAttendee = view.findViewById(R.id.btn_agregarAttendee);
        btnAgregarAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNewAttendee(new Attendee());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewAttendeeInteractionListener) {
            mListener = (OnNewAttendeeInteractionListener) context;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewAttendeeInteractionListener {
        void onNewAttendee(Attendee attendee);
    }
}
