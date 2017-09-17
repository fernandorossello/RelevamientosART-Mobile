package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fernando.relevamientosart.R;

import java.io.IOException;
import java.util.Formatter;

import Modelo.Visit;

public class MedidorDeRuidoFragment extends Fragment {
    private static final String ARG_VISIT = "visita";

    private Visit mVisit;
    private MediaRecorder mRecorder;
    private boolean listening;
    private EditText mDecibelesET;

    private OnRuidoFragmentInteractionListener mListener;

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

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRuidoFragmentInteractionListener) {
            mListener = (OnRuidoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRuidoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRuidoFragmentInteractionListener {
        void onMedirRuido(Visit visit);
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


    @Override
    public void onResume() {
        listening = true;
        new Ear().execute();
        super.onResume();
    }

    @Override
    public void onPause() {
        listening = false;
        super.onPause();
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

            if (value < -80) {
                value = new Double(-80);
            } else if (value > 0) {
                value = new Double(0);
            }

            String db = new Formatter().format("%03.1f",value).toString();

            mDecibelesET.setText(db + " dB");

            updateBar(value);

        }

        @Override
        protected void onPostExecute(Void result) {
            stopRecording();
        }

        public void updateBar(Double db) {
            Double width;

            // Factor de escala para convertir a Dips
            final float scale = getResources().getDisplayMetrics().density;

            width = (db * 250 * scale) / -80; // Anchura en pixeles

            //RelativeLayout.LayoutParams lyParams = new RelativeLayout.LayoutParams(width.intValue(), barDB.getHeight());
            //lyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //barDB.setLayoutParams(lyParams);
        }

    }
}
