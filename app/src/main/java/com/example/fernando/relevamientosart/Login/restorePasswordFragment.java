package com.example.fernando.relevamientosart.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link restorePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link restorePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class restorePasswordFragment extends Fragment {

    private AutoCompleteTextView mEmailView;

    private View mRestorePasswordView;

    private OnFragmentInteractionListener mListener;
    private final String URL_ENDPOINT_RECUPERAR_CONTRASEÑA = "https://relevamientos-art.herokuapp.com/users/new_password_request";


    public restorePasswordFragment() {
        // Required empty public constructor
    }

    public static restorePasswordFragment newInstance() {
        return new restorePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restore_password, container, false);

        mEmailView = view.findViewById(R.id.email);

        mRestorePasswordView = view.findViewById(R.id.restore_password_form);

        Button mRestorePasswordInButton = view.findViewById(R.id.restore_password_button);
        mRestorePasswordInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                restorePassword();
            }
        });

        return view;
    }

    private void restorePassword() {
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        }
        else {
            solicitarCambioDeContraseña(email);
        }
    }

    private void solicitarCambioDeContraseña(final String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = URL_ENDPOINT_RECUPERAR_CONTRASEÑA;

        StringRequest jsonRequest = new StringRequest
                (Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Su nueva contraseña será enviada a su correo", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.networkResponse.statusCode == 404 ) {
                            Toast.makeText(getActivity(), R.string.error_usuario_inexistente, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.error_cambio_contraseña, Toast.LENGTH_SHORT).show();
                        }

                        VolleyError err = error;
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers =  new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Accept","application/json");

                return headers;
            }

            @Override
            public byte[] getBody(){
                String cuerpo = "{\"user_email\": \""+ email.trim() + "\" }";
                try {
                    return cuerpo.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        requestQueue.add(jsonRequest);
    }

    //TODO: Este es un metodo comun que comparte con LoginFragment
    //Posiblemente se pueda poner en un lugar común para que ambos lo utilicen
    private boolean isEmailValid(String email) {
        return email.contains("@");
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
