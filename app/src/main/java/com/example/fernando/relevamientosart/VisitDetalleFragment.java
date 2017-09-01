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

public class VisitDetalleFragment extends Fragment {

    public static final String ARG_ID = "idVisita";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public VisitDetalleFragment() {
        // Required empty public constructor
    }

    public static VisitDetalleFragment newInstance(int idVisita) {
        VisitDetalleFragment fragment = new VisitDetalleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, idVisita);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visita_detalle, container, false);
        String texto =  "Se ha elegido la visita nro" + this.getArguments().getInt(ARG_ID);

        TextView txt =  view.findViewById(R.id.texto_detalle_visita);
        txt.setText(texto);

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

        menuItem = menu.findItem(R.id.action_exportar);
        menuItem.setVisible(true);
        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
