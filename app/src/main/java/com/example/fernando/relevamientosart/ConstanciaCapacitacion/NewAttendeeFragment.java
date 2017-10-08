package com.example.fernando.relevamientosart.ConstanciaCapacitacion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;

import java.sql.SQLException;

import Helpers.DBHelper;
import Modelo.Attendee;
import Modelo.Managers.AttendeeManager;

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

        ((TextView) view.findViewById(R.id.tv_worker_name)).setText(mAttendee.name);
        ((TextView) view.findViewById(R.id.tv_worker_lastName)).setText(mAttendee.last_name);
        ((TextView) view.findViewById(R.id.tv_worker_cuil)).setText(mAttendee.cuil);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mAttendee.name = ((EditText)getView().findViewById(R.id.tv_worker_name)).getText().toString();
        mAttendee.last_name = ((EditText)getView().findViewById(R.id.tv_worker_lastName)).getText().toString();
        mAttendee.cuil = ((EditText)getView().findViewById(R.id.tv_worker_cuil)).getText().toString();

        DBHelper dbHelper = ((MainActivity)getActivity()).getHelper();

        try {
            new AttendeeManager(dbHelper).persist(mAttendee);
        }
        catch (SQLException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
