package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fernando.relevamientosart.R;

import Modelo.Attendee;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewAttendeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewAttendeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAttendeeFragment extends Fragment {
    private static final String ARG_ATTENDEE = "Attendee";
    private Attendee mAttendee;

    private OnFragmentInteractionListener mListener;

    public NewAttendeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment NewAttendeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAttendeeFragment newInstance(Attendee attendee) {
        NewAttendeeFragment fragment = new NewAttendeeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ATTENDEE, attendee);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAttendee = (Attendee) getArguments().getSerializable(ARG_ATTENDEE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_attendee, container, false);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach() { super.onDetach(); }

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
