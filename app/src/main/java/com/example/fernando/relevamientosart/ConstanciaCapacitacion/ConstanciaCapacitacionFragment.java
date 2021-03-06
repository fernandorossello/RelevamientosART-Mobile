package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.sql.SQLException;
import java.util.Date;

import Excepciones.ValidationException;
import Helpers.DBHelper;
import Modelo.Attendee;
import Modelo.CAPResult;
import Modelo.Enums.EnumStatus;
import Modelo.Managers.AttendeeManager;
import Modelo.Managers.ResultManager;
import Modelo.Managers.VisitManager;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;

public class ConstanciaCapacitacionFragment extends Fragment {
    private static final String ARG_TASK = "tarea";
    private Task mTarea;
    private Visit mVisit;
    private CAPResult mResult;
    private DBHelper dbHelper;
    private ResultManager mResultManager;

    private OnNewAttendeeInteractionListener mListener;

    public ConstanciaCapacitacionFragment() {
        // Required empty public constructor
    }

    public static ConstanciaCapacitacionFragment newInstance(Task task) {
        ConstanciaCapacitacionFragment fragment = new ConstanciaCapacitacionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK ,task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTarea = (Task) getArguments().getSerializable(ARG_TASK);
            dbHelper = ((MainActivity)getActivity()).getHelper();
            mResultManager = new ResultManager(dbHelper);
            Result result = mResultManager.getResult(mTarea);
            if(result == null){
                mResult = new CAPResult();
                mResult.task = mTarea;
            } else {
                mResult = (CAPResult) result;
            }

            try {
                mResultManager.persist(mResult);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constancia_capacitacion, container, false);

        ((EditText) view.findViewById(R.id.tv_capr_course)).setText(mResult.course_name);
        ((EditText) view.findViewById(R.id.tv_capr_content)).setText(mResult.contents);
        ((EditText) view.findViewById(R.id.tv_capr_methodology)).setText(mResult.methodology);

        RecyclerView recyclerView  = view.findViewById(R.id.attendeeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DepurarYCargarListaEmpleados();

        recyclerView.setAdapter(new MyAttendeeRecyclerViewAdapter(mResult.attendees, mListener));

        TextView emptyView = view.findViewById(R.id.empty_view);
        if (mResult.attendees.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        FloatingActionButton btnAgregarAttendee = view.findViewById(R.id.btn_agregarAttendee);
        btnAgregarAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attendee attendee = new Attendee(){{result = mResult;}};
                mResult.attendees.add(attendee);
                mListener.onNewAttendee(attendee);
            }
        });

        getActivity().setTitle(R.string.titulo_constancia_capacitacion);

        return view;
    }

    private void DepurarYCargarListaEmpleados() {

        AttendeeManager attendeeManager = new AttendeeManager(dbHelper);

        for (Attendee attendee : mResult.attendees) {
            try{
                attendee.Validar();
            }catch (ValidationException ex){
                try {
                    mResult.attendees.remove(attendee);
                    attendeeManager.delete(attendee);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

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
        void onBorrarTrabajador(Attendee mItem);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mResult.course_name = ((EditText)getView().findViewById(R.id.tv_capr_course)).getText().toString();
        mResult.contents = ((EditText)getView().findViewById(R.id.tv_capr_content)).getText().toString();
        mResult.methodology = ((EditText)getView().findViewById(R.id.tv_capr_methodology)).getText().toString();

        try {

            if(mResult.getStatus() == EnumStatus.FINALIZADA){
                mResult.completed_at = new Date();
            }

            new ResultManager(dbHelper).persist(mResult);
            new VisitManager(dbHelper).cambiarEstadoVisita(mResult);

        } catch (SQLException ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        DepurarYCargarListaEmpleados();
    }
}
