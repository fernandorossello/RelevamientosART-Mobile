package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;
import com.example.fernando.relevamientosart.RAR.RiskFragment;

import java.sql.SQLException;

import Excepciones.ValidationException;
import Helpers.DBHelper;
import Modelo.Attendee;
import Modelo.CAPResult;
import Modelo.Managers.AttendeeManager;
import Modelo.Managers.WorkingManManager;

public class NewAttendeeFragment extends Fragment {
    private static final String ARG_ATTENDEE = "Attendee";
    private Attendee mAttendee;

    private View.OnKeyListener OnBackListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                guardarAttendee();
                return true;
            }
            return false;
        }
    };

    public NewAttendeeFragment() {
        // Required empty public constructor
    }

    public static NewAttendeeFragment newInstance(Attendee attendee) {
        NewAttendeeFragment fragment = new NewAttendeeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ATTENDEE, attendee);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAttendee = (Attendee) getArguments().getSerializable(ARG_ATTENDEE);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(OnBackListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_attendee, container, false);

        TextView tv_name = (view.findViewById(R.id.tv_worker_name));
        tv_name.setText(mAttendee.name);
        tv_name.setOnKeyListener(OnBackListener);

        TextView tv_lastName = (view.findViewById(R.id.tv_worker_lastName));
        tv_lastName.setText(mAttendee.lastName);
        tv_lastName.setOnKeyListener(OnBackListener);

        TextView tv_cuil = (view.findViewById(R.id.tv_worker_cuil));
        tv_cuil.setText(mAttendee.cuil);
        tv_cuil.setOnKeyListener(OnBackListener);

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach() { super.onDetach();}

    private void guardarAttendee() {

        Attendee attendee = new Attendee();

        attendee.name = ((EditText)getView().findViewById(R.id.tv_worker_name)).getText().toString();
        attendee.lastName = ((EditText)getView().findViewById(R.id.tv_worker_lastName)).getText().toString();
        attendee.cuil = ((EditText)getView().findViewById(R.id.tv_worker_cuil)).getText().toString();

        DBHelper dbHelper = ((MainActivity)getActivity()).getHelper();
        try {
            attendee.Validar();
            mAttendee.fill(attendee);
            new AttendeeManager(dbHelper).persist(mAttendee);
            this.getActivity().onBackPressed();
        }
        catch (ValidationException ex){

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

            builder.setMessage(ex.getMessage())
                    .setTitle(R.string.Validacion)
                    .setPositiveButton(R.string.descartar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().onBackPressed();
                        }
                    });
            builder.setNegativeButton(R.string.editar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();

            dialog.show();

        }catch (SQLException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }
}
