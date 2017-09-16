package com.example.fernando.relevamientosart;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Modelo.Visit;

public class VisitDetalleFragment extends Fragment {

    public static final String ARG_VISITA = "visita";

    private Visit mVisit;

    public VisitDetalleFragment() {
        // Required empty public constructor
    }

    public static VisitDetalleFragment newInstance(Visit visita) {
        VisitDetalleFragment fragment = new VisitDetalleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_VISITA, visita);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVisit = (Visit) getArguments().getSerializable(ARG_VISITA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visita_detalle, container, false);

        TextView tvNombreInstitucion = view.findViewById(R.id.tv_detalle_nombre_institucion);
        tvNombreInstitucion.setText(mVisit.nombreInstitucion());

        setHasOptionsMenu(true);

        return view;
    }

    @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                super.onCreateOptionsMenu(menu, inflater);


        MenuItem menuItem = menu.findItem(R.id.action_rar);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.action_rgrl);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.action_capacitacion);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.action_constancia);
        menuItem.setVisible(true);

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
