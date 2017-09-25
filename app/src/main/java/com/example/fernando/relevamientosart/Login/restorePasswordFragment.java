package com.example.fernando.relevamientosart.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.fernando.relevamientosart.R;


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
        //TODO: Si giro la pantalla, vuelve a aparecer el LoginFragment
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

        //TODO: Esta parte de código es comun con LoginFragment
        //Posiblemente se pueda poner en un lugar común para que ambos lo utilicen
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Toast.makeText(this.getActivity(), "Su nueva contraseña ha sido enviada a su mail", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: Este es un metodo comun que comparte con LoginFragment
    //Posiblemente se pueda poner en un lugar común para que ambos lo utilicen
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRestorePasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRestorePasswordView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRestorePasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mRestorePasswordView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRestorePasswordView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRestorePasswordView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mRestorePasswordView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRestorePasswordView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //TODO: Este es un metodo comun que comparte con LoginFragment
    //Posiblemente se pueda poner en un lugar común para que ambos lo utilicen
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
