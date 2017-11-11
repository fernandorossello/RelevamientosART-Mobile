package com.example.fernando.relevamientosart;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Modelo.HelpQuestion;

public class HelpQuestionFragment extends Fragment {
    private static final String ARG_PREGUNTA = "pregunta";

    private HelpQuestion mPregunta;

    public HelpQuestionFragment() {
        // Required empty public constructor
    }

    public static HelpQuestionFragment newInstance(HelpQuestion question) {
        HelpQuestionFragment fragment = new HelpQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PREGUNTA, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPregunta = (HelpQuestion)getArguments().getSerializable(ARG_PREGUNTA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_question, container, false);

        TextView tvPregunta = view.findViewById(R.id.tv_pregunta);
        tvPregunta.setText(mPregunta.Question);

        TextView tvRespuesta = view.findViewById(R.id.tv_respuesta);
        tvRespuesta.setText(mPregunta.Answer);

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
