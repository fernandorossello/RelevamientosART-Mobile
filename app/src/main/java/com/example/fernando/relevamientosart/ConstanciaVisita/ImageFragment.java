package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fernando.relevamientosart.R;

import Modelo.Image;
import Modelo.Visit;

public class ImageFragment extends Fragment {


    private static final String ARG_VISIT = "visita";
    private OnImageListFragmentInteractionListener mListener;
    private Visit mVisit;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageFragment() {
    }

    @SuppressWarnings("unused")
    public static ImageFragment newInstance(Visit visit) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_VISIT, visit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mVisit = (Visit) getArguments().getSerializable(ARG_VISIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyImageRecyclerViewAdapter(mVisit.images, mListener));
        }

        getActivity().setTitle(R.string.titulo_imagenes);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageListFragmentInteractionListener) {
            mListener = (OnImageListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnImageListFragmentInteractionListener {
        void onImagenPressed(Image imagen);
    }
}
