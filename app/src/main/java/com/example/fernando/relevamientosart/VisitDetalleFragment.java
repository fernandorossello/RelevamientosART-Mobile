package com.example.fernando.relevamientosart;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import Modelo.Enums.EnumStatus;
import Modelo.Enums.EnumTareas;
import Modelo.Institution;
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

        Institution institution = mVisit.institution;

        EditText tvNombreInstitucion = view.findViewById(R.id.tv_detalle_nombre_institucion);
        tvNombreInstitucion.setText(institution.name);

        EditText tvDireccionInstitucion = view.findViewById(R.id.tv_detalle_direccion_institucion);
        tvDireccionInstitucion.setText(institution.address);

        EditText tvProvincia = view.findViewById(R.id.tv_provincia);
        tvProvincia.setText(institution.province);

        EditText tvLocalidad = view.findViewById(R.id.tv_localidad);
        tvLocalidad.setText(institution.city);

        EditText tvCuit = view.findViewById(R.id.tv_cuit);
        tvCuit.setText(institution.cuit);

        EditText tvActividad = view.findViewById(R.id.tv_actividad);
        tvActividad.setText(institution.activity);

        EditText tvTelefono = view.findViewById(R.id.tv_telefono);
        tvTelefono.setText(institution.phone_number);

        EditText tvFechaVisita = view.findViewById(R.id.tv_fecha_visita);
        tvFechaVisita.setText(formatearFecha(mVisit.to_visit_on));

        EditText tvEstadoVista = view.findViewById(R.id.tv_estado_visita);
        tvEstadoVista.setText(EnumStatus.getById(mVisit.status).name);

        EditText tvPrioridadVisita = view.findViewById(R.id.tv_prioriodad_visita);
        tvPrioridadVisita.setText(String.valueOf(mVisit.priority));

        getActivity().setTitle(R.string.titulo_detalle_visita);

        tvNombreInstitucion.setTextColor(0xff000000);
        tvDireccionInstitucion.setTextColor(0xff000000);
        tvProvincia.setTextColor(0xff000000);
        tvLocalidad.setTextColor(0xff000000);
        tvCuit.setTextColor(0xff000000);
        tvActividad.setTextColor(0xff000000);
        tvTelefono.setTextColor(0xff000000);
        tvPrioridadVisita.setTextColor(0xff000000);
        tvEstadoVista.setTextColor(0xff000000);
        tvFechaVisita.setTextColor(0xff000000);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                super.onCreateOptionsMenu(menu, inflater);

        if(mVisit.status != EnumStatus.FINALIZADA.id && mVisit.status != EnumStatus.ENVIADA.id) {
            MenuItem menuItem = menu.findItem(R.id.action_rar);
            menuItem.setVisible(mVisit.tieneTarea(EnumTareas.RAR));

            menuItem = menu.findItem(R.id.action_rgrl);
            menuItem.setVisible(mVisit.tieneTarea(EnumTareas.RGRL));

            menuItem = menu.findItem(R.id.action_capacitacion);
            menuItem.setVisible(mVisit.tieneTarea(EnumTareas.CAPACITACION));

            menuItem = menu.findItem(R.id.action_constancia);
            menuItem.setVisible(true);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String formatearFecha(Date date){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date);
    }

}
