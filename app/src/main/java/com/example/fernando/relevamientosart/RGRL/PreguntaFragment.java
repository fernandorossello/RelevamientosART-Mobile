package com.example.fernando.relevamientosart.RGRL;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;
import com.example.fernando.relevamientosart.VisitDetalleFragment;

import org.w3c.dom.Text;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Enums.EnumAnswer;
import Modelo.Managers.QuestionManager;
import Modelo.Managers.ResultManager;
import Modelo.Question;
import Modelo.RGRLResult;
import Modelo.Result;
import Modelo.Task;
import Modelo.Visit;

public class PreguntaFragment extends Fragment {

    private static final String ARG_TASK = "tarea";
    private Task mTarea;
    private List<Question> mQuestions;
    private int mIndexQuestion = 0;
    private DBHelper dbHelper;
    private RGRLResult mResult;

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
            mQuestions = new QuestionManager().questionsEjemplo();
            Result result = new ResultManager(dbHelper).getResult(mTarea);

            if (result == null) {
                mResult = new RGRLResult();
                mResult.questions = new ArrayList<>(mQuestions);
                mResult.task = mTarea;
            } else {
                mResult = (RGRLResult) result;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pregunta, container, false);

        final TextView tv_pregunta = view.findViewById(R.id.tv_pregunta);
        refreshQuestion(tv_pregunta);

        view.findViewById(R.id.tv_SI).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((List<Question>)mResult.questions).get(mIndexQuestion).answer = EnumAnswer.SI.id;

                try {
                    new ResultManager(((MainActivity)getActivity()).getHelper()).persist(mResult);
                } catch (SQLException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                limiteDeMaxima();
                refreshQuestion(tv_pregunta);
            }
        });

        view.findViewById(R.id.tv_NoAplica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((List<Question>)mResult.questions).get(mIndexQuestion).answer = EnumAnswer.NOAPLICA.id;
                try {
                    new ResultManager(dbHelper).persist(mResult);
                } catch (SQLException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                limiteDeMaxima();
                refreshQuestion(tv_pregunta);
            }
        });

        view.findViewById(R.id.tv_NO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((List<Question>)mResult.questions).get(mIndexQuestion).answer = EnumAnswer.NO.id;
                try {
                    new ResultManager(dbHelper).persist(mResult);
                } catch (SQLException ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                limiteDeMaxima();
                refreshQuestion(tv_pregunta);
            }
        });

        view.findViewById(R.id.tv_siguiente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limiteDeMaxima();
                refreshQuestion(tv_pregunta);
                refreshSelectedOption();
            }
        });

        view.findViewById(R.id.tv_anterior).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIndexQuestion > 0)
                    mIndexQuestion--;
                refreshQuestion(tv_pregunta);
                refreshSelectedOption();
            }
        });

        cargarListenerFechaRegul(view);

        return view;
    }

    private void refreshSelectedOption() {
        int color = Color.parseColor("#dadada");
        getView().findViewById(R.id.tv_SI).setBackgroundColor(color);
        getView().findViewById(R.id.tv_NoAplica).setBackgroundColor(color);
        getView().findViewById(R.id.tv_NO).setBackgroundColor(color);
        color = Color.parseColor("#0ae187");
        if (((List<Question>)mResult.questions).get(mIndexQuestion).answer == EnumAnswer.SI.id)
            getView().findViewById(R.id.tv_SI).setBackgroundColor(color);
        if (((List<Question>)mResult.questions).get(mIndexQuestion).answer == EnumAnswer.NO.id)
            getView().findViewById(R.id.tv_NO).setBackgroundColor(color);
        if (((List<Question>)mResult.questions).get(mIndexQuestion).answer == EnumAnswer.NOAPLICA.id)
            getView().findViewById(R.id.tv_NoAplica).setBackgroundColor(color);
    }

    private void limiteDeMaxima() {
        if (mIndexQuestion < mQuestions.size() - 1)
            mIndexQuestion++;
    }

    private void refreshQuestion(TextView tv_pregunta) {
        String q = mQuestions.get(mIndexQuestion).description;
        tv_pregunta.setText(q);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void cargarListenerFechaRegul(View view) {
        final EditText tvFechaRegul = view.findViewById(R.id.fechaRegul);
        final DatePickerDialog.OnDateSetListener dateSetRegulListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tvFechaRegul);
            }
        };

        tvFechaRegul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateSetRegulListener , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText editText) {
        editText.setText(formatearFecha(myCalendar.getTime()));
    }

    private String formatearFecha(Date date){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        DBHelper dbHelper = ((MainActivity)getActivity()).getHelper();

        try {
            new ResultManager(dbHelper).persist(mResult);
        } catch (SQLException ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}