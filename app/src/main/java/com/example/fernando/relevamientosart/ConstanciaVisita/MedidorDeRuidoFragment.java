package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fernando.relevamientosart.R;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import Modelo.Noise;
import Modelo.Visit;

public class MedidorDeRuidoFragment extends Fragment {
    private static final String ARG_VISIT = "visita";

    private static final Double MIN_DB = -80d;
    private static final Double MAX_DB = 0d;

    private Visit mVisit;
    private MediaRecorder mRecorder;
    private boolean listening = false;
    private EditText mDecibelesET;
    private Double maxNoise = MIN_DB;

    private OnNoiseListFragmentInteractionListener mListener;


    public MedidorDeRuidoFragment() {
        // Required empty public constructor
    }

    public static MedidorDeRuidoFragment newInstance(Visit visit) {
        MedidorDeRuidoFragment fragment = new MedidorDeRuidoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_VISIT, visit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVisit = (Visit)getArguments().getSerializable(ARG_VISIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medidor_de_ruido, container, false);

        mDecibelesET = view.findViewById(R.id.etDecibeles);

        Button btnMedir = view.findViewById(R.id.btn_medir);

        btnMedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listening) {
                    listening = false;
                    ((Button)view).setText(R.string.medir);
                    habilitarRegistrar();
                } else {
                    listening = true;
                    new Ear().execute();
                    ((Button)view).setText(R.string.parar);
                }
            }
        });

        final RecyclerView recyclerView = view.findViewById(R.id.noise_list);
        recyclerView.setAdapter(new MyNoiseRecyclerViewAdapter(mVisit.noises,mListener));

        final EditText etDescripcion = view.findViewById(R.id.etDescripcionDecibeles);
        Button btnRegistrar = view.findViewById(R.id.btn_registrarRuido);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Noise noise = new Noise(){{
                    decibels = maxNoise;
                    visit = mVisit;
                }};
                noise.description = etDescripcion.getText().toString();
                mVisit.noises.add(noise);

                recyclerView.setAdapter(new MyNoiseRecyclerViewAdapter(mVisit.noises,mListener));
                inicializar();
            }
        });

        return view;
    }


    private void inicializar(){
        EditText etDescripcion = getView().findViewById(R.id.etDescripcionDecibeles);
        etDescripcion.setText("");
        etDescripcion.setEnabled(false);


        EditText etDecibeles = getView().findViewById(R.id.etDecibeles);
        etDecibeles.setText("");

        Button btnRegistrar = getView().findViewById(R.id.btn_registrarRuido);
        btnRegistrar.setEnabled(false);
    }

    private void habilitarRegistrar(){
        Button btnRegistrar = getView().findViewById(R.id.btn_registrarRuido);
        btnRegistrar.setEnabled(true);

        EditText etDescripcion = getView().findViewById(R.id.etDescripcionDecibeles);
        etDescripcion.setEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoiseListFragmentInteractionListener) {
            mListener = (OnNoiseListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoiseListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void startRecording() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            mRecorder.start();
        }
    }

    //Para la escucha
    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    //Devuelve la mayor amplitud del sonido captado desde la ultima vez que se llamo al método
    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public class Ear extends AsyncTask<Void, Double, Void> {

        protected void onPreExecute() {
            startRecording();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            while(listening) {
                SystemClock.sleep(300); // Si es menor casi siempre da 0
                Double amplitude = 20 * Math.log10(getAmplitude() / 32768.0);
                publishProgress(amplitude);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            Double value = values[0];

            if (value < MIN_DB) {
                value = new Double(MIN_DB);
            } else if (value > MAX_DB) {
                value = new Double(MAX_DB);
            }

            String db = formatearDecibeles(value);

            mDecibelesET.setText(db + " dB");

            if(value > maxNoise){
                maxNoise = value;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            stopRecording();
            String db = formatearDecibeles(maxNoise);
            mDecibelesET.setText("Máximo: " + db + " dB");
        }

    }

    public String formatearDecibeles(Double decibeles){
        return new Formatter().format("%03.1f", decibeles).toString();
    }

    public interface OnNoiseListFragmentInteractionListener {
        void onRuidoPressed(Noise ruido);
    }
}
