package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fernando.relevamientosart.MainActivity;
import com.example.fernando.relevamientosart.R;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Managers.VisitManager;
import Modelo.Managers.VisitRecordManager;
import Modelo.Task;
import Modelo.Visit;
import Modelo.VisitRecord;


public class ConstanciaVisitaFragment extends Fragment {
    private static final String ARG_visita = "visita";

    private Visit mVisit;

    public ConstanciaVisitaFragment() {
        // Required empty public constructor
    }

    private static final int REQUEST_TAKE_PHOTO = 1500;
    private static final int REQUEST_READ = 2000;


    public static ConstanciaVisitaFragment newInstance(Visit visit) {
        ConstanciaVisitaFragment fragment = new ConstanciaVisitaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_visita, visit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVisit = (Visit)getArguments().getSerializable(ARG_visita);
            if(mVisit.visitRecord == null){
                mVisit.visitRecord = new VisitRecord();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_constancia_visita, container, false);

        RecyclerView recyclerView  = view.findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(mVisit.tasks));

        EditText etObservaciones = view.findViewById(R.id.et_observaciones);
        etObservaciones.setText(mVisit.visitRecord.observations);

        AppCompatImageButton btnFoto = view.findViewById(R.id.btn_camera);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });

        FloatingActionButton btnGuardar = view.findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Guardar", Toast.LENGTH_SHORT).show();
                guardarConstanciaDeVisita(view);
            }
        });

        AppCompatImageButton btnAudio = view.findViewById(R.id.btn_audio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Medir audio", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void guardarConstanciaDeVisita(View view) {
        EditText etObservaciones = ((View)view.getParent()).findViewById(R.id.et_observaciones);
        mVisit.visitRecord.observations = etObservaciones.getText().toString();

        mVisit.visitRecord.completed_at = new Date();


        DBHelper dbHelper = ((MainActivity)view.getContext()).getHelper();

        try {
            (new VisitManager(dbHelper)).persist(mVisit);
        }catch (SQLException ex){
            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void tomarFoto() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.CAMERA)) {
                Toast.makeText(getContext(), R.string.permission_rationale, Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_READ);
            }
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = crearArchivoDeImagen();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(getContext(),getContext().getApplicationContext().getPackageName()+".fileprovider", photoFile);
                Uri uriSavedImage = Uri.fromFile(photoFile);
                grantPermissionsToUri(this.getActivity(), takePictureIntent, uriSavedImage);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private static void grantPermissionsToUri(Context context, Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private File crearArchivoDeImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = mVisit.nombreInstitucion() +"_"  + timeStamp + "_";

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "ARTImages");
        imagesFolder.mkdirs();

        //File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(imagesFolder, imageFileName + ".jpg");
        return image;
    }
}
