package com.example.fernando.relevamientosart.RGRL;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import Helpers.DBHelper;
import Modelo.Enums.EnumAnswer;
import Modelo.Enums.EnumStatus;
import Modelo.Managers.QuestionManager;
import Modelo.Managers.ResultManager;
import Modelo.Question;
import Modelo.RGRLResult;
import Modelo.Result;
import Modelo.Task;

public class PreguntaFragment extends Fragment {

    private static final String ARG_TASK = "tarea";
    private Task mTarea;

    private DBHelper dbHelper;
    private RGRLResult mResult;
    private ResultManager mResultManager;
    private QuestionManager mQuestionManager;

    private ListIterator<Question> iterador;
    private Question preguntaEnCurso;

    private final Calendar myCalendar = Calendar.getInstance();

    public PreguntaFragment() {
        // Required empty public constructor
    }

    public static PreguntaFragment newInstance(Task task) {
        PreguntaFragment fragment = new PreguntaFragment();
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
            dbHelper = ((MainActivity)getActivity()).getHelper();

            mResultManager = new ResultManager(dbHelper);
            mQuestionManager = new QuestionManager(dbHelper);
            Result result = mResultManager.getResult(mTarea);

            if (result == null) {
                mResult = new RGRLResult();
                mResult.questions.addAll(mQuestionManager.generarPreguntasParaResult(mResult));
                mResult.task = mTarea;
                try {
                    mResultManager.persist(mResult);
                } catch (SQLException e) {
                    Toast.makeText(getContext(), "Error al guardar el resultado", Toast.LENGTH_SHORT).show();
                }
            } else {
                mResult = (RGRLResult) result;
            }

            iterador = (new ArrayList<>(mResult.questions)).listIterator();
            preguntaEnCurso = iterador.next();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pregunta, container, false);

        refreshQuestion(view);

        view.findViewById(R.id.tv_SI).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preguntaEnCurso.answer_code = EnumAnswer.SI.id;
                preguntaEnCurso.answer = EnumAnswer.SI.name;

                try {
                    mQuestionManager.persist(preguntaEnCurso);
                } catch (SQLException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                avanzarASiguientePregunta();
            }
        });

        view.findViewById(R.id.tv_NoAplica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preguntaEnCurso.answer = EnumAnswer.NOAPLICA.name;
                preguntaEnCurso.answer_code = EnumAnswer.NOAPLICA.id;

                try {
                    mQuestionManager.persist(preguntaEnCurso);
                } catch (SQLException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                avanzarASiguientePregunta();
            }
        });

        view.findViewById(R.id.tv_NO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preguntaEnCurso.answer = EnumAnswer.NO.name;
                preguntaEnCurso.answer_code = EnumAnswer.NO.id;
                try {
                    mQuestionManager.persist(preguntaEnCurso);
                } catch (SQLException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                avanzarASiguientePregunta();
            }
        });

        view.findViewById(R.id.tv_siguiente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avanzarASiguientePregunta();
            }
        });

        view.findViewById(R.id.tv_anterior).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iterador.hasPrevious()) {
                    Question pregunta = iterador.previous();
                    if( pregunta == preguntaEnCurso && iterador.hasPrevious()) {
                        preguntaEnCurso = iterador.previous();
                    } else  {
                        preguntaEnCurso = pregunta;
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.primera_pregunta, Toast.LENGTH_SHORT).show();
                }

                refreshQuestion(getView());
            }
        });

        refreshSelectedOption(view);

        return view;
    }

    private void refreshSelectedOption(View view) {
        int color = Color.parseColor("#dadada");
        view.findViewById(R.id.tv_SI).setBackgroundColor(color);
        view.findViewById(R.id.tv_NoAplica).setBackgroundColor(color);
        view.findViewById(R.id.tv_NO).setBackgroundColor(color);
        color = Color.parseColor("#0ae187");

        int answer = preguntaEnCurso.answer_code;

        if ( answer == EnumAnswer.SI.id)
            view.findViewById(R.id.tv_SI).setBackgroundColor(color);
        if ( answer == EnumAnswer.NO.id)
            view.findViewById(R.id.tv_NO).setBackgroundColor(color);
        if ( answer == EnumAnswer.NOAPLICA.id)
            view.findViewById(R.id.tv_NoAplica).setBackgroundColor(color);
    }

    private void avanzarASiguientePregunta() {
        if (iterador.hasNext()) {
            Question pregunta = iterador.next();

            if( pregunta == preguntaEnCurso) {
                preguntaEnCurso = iterador.next();
            } else  {
                preguntaEnCurso = pregunta;
            }

        } else {
            Toast.makeText(getContext(), R.string.ultima_pregunta, Toast.LENGTH_SHORT).show();
        }
        refreshQuestion(getView());
    }

    private void refreshQuestion(View view) {
        if(view != null) {
            TextView tv_pregunta = view.findViewById(R.id.tv_pregunta);
            tv_pregunta.setText(preguntaEnCurso.description);

            TextView tv_categoria = view.findViewById(R.id.tv_categoria);
            tv_categoria.setText(preguntaEnCurso.category);

            refreshSelectedOption(view);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(mResult.getStatus() == EnumStatus.FINALIZADA){
                mResult.completed_at = new Date();
            }
            new ResultManager(dbHelper).persist(mResult);
        } catch (SQLException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}