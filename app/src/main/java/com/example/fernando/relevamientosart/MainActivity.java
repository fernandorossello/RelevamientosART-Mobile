package com.example.fernando.relevamientosart;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.fernando.relevamientosart.ConstanciaCapacitacion.ConstanciaCapacitacionFragment;
import com.example.fernando.relevamientosart.ConstanciaCapacitacion.NewAttendeeFragment;
import com.example.fernando.relevamientosart.ConstanciaVisita.ImageFragment;
import com.example.fernando.relevamientosart.Login.LoginActivity;
import com.example.fernando.relevamientosart.ConstanciaVisita.MedidorDeRuidoFragment;
import com.example.fernando.relevamientosart.RAR.RARFragment;
import com.example.fernando.relevamientosart.RAR.RiskFragment;
import com.example.fernando.relevamientosart.ConstanciaVisita.ConstanciaVisitaFragment;
import com.example.fernando.relevamientosart.RAR.RiskSelectorFragment;
import com.example.fernando.relevamientosart.RGRL.PreguntaFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import Helpers.DBHelper;
import Modelo.Attendee;
import Modelo.Enums.EnumTareas;
import Modelo.Image;
import Modelo.Managers.VisitManager;
import Modelo.Managers.WorkingManManager;
import Modelo.Noise;
import Modelo.Task;
import Modelo.Visit;
import Modelo.WorkingMan;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,VisitFragment.OnVisitSelectedListener,
        RARFragment.OnTrabajadoresFragmentInteractionListener,
        ConstanciaCapacitacionFragment.OnNewAttendeeInteractionListener,
        ConstanciaVisitaFragment.OnEventoConstanciaListener,
        ImageFragment.OnImageListFragmentInteractionListener,
        RiskFragment.OnRiskFragmentInteractionListener,
        MedidorDeRuidoFragment.OnNoiseListFragmentInteractionListener
{
    private static final int REQUEST_TAKE_PHOTO = 1500;
    private static final int REQUEST_READ = 2000;
    private static final String TAG_CONSTANCIA_VISITA = "ConstanciaVisitaTag";
    private static final String TAG_FRAGMENT_IMAGENES = "ListaImagensTag";
    private static final String TAG_WORKING_MAN = "WorkingManTag";
    private final String TAG_FRAGMENT_MEDICION_RUIDO = "tag_frg_medicion_ruido";


    private DBHelper mDBHelper;

    private Visit mVisitaEnCurso;

    private Uri uriSavedImage = null;

    private StrictMode.VmPolicy.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Allow camera works in SDK targets above v24
        mBuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(mBuilder.build());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new VisitFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                super.onBackPressed();
        }
    }

    private String getCurrentFragmentTag(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Task selectedTask;
        switch (item.getItemId()) {
            case R.id.action_rgrl: {

                selectedTask = mVisitaEnCurso.obtenerTarea(EnumTareas.RGRL);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PreguntaFragment.newInstance(selectedTask))
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            case R.id.action_constancia:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConstanciaVisitaFragment.newInstance(mVisitaEnCurso),TAG_CONSTANCIA_VISITA)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_capacitacion:

                selectedTask = mVisitaEnCurso.obtenerTarea(EnumTareas.CAPACITACION);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConstanciaCapacitacionFragment.newInstance(selectedTask))
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_rar:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, RARFragment.newInstance(mVisitaEnCurso))
                        .addToBackStack(null)
                        .commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Logout();
            return true;
        } else if (id == R.id.nav_sincronizar)
        {
            //TODO: No lo pongo en un asynTask porque manejaremos asincronismo cuando agreguemos conexión con el endpoint
            Toast.makeText(this, getString(R.string.msj_sincronizandoVisitas), Toast.LENGTH_SHORT).show();
            sincronizarVisitas();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sincronizarVisitas() {
        VisitManager managerVisitas = new VisitManager(this.getHelper());
        List<Visit> visitas = managerVisitas.simuladorParaTraerVisitasDelEndpoint();
        try {
            managerVisitas.persist(visitas);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new VisitFragment())
                    .commit();
        }
        catch (SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void borrarDatos(){
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .remove("nombreUsuario")
                .remove("idUsuario")
                .apply();
    }

    private void redireccionarALogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void Logout(){
        borrarDatos();
        redireccionarALogin();
    }

    @Override
    public void OnVisitSelected(Visit visit) {

        mVisitaEnCurso = visit;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, VisitDetalleFragment.newInstance(visit))
                .addToBackStack(null)
                .commit();
        }

    //************************************************DB HELPER************************************************
    public DBHelper getHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }

    @Override
    public void onTrabajadorSeleccionado(WorkingMan workingMan) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RiskFragment().newInstance(workingMan),TAG_WORKING_MAN)
                .addToBackStack(null)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void OnTomarFoto() {
        tomarFoto();
    }

    @Override
    public void OnVerFotosClick(Visit visit) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ImageFragment.newInstance(visit),TAG_FRAGMENT_IMAGENES)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onNewAttendee(Attendee attendee) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NewAttendeeFragment().newInstance(attendee))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void OnGuardarConstanciaDeVisita() {
        getSupportFragmentManager().popBackStack();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void tomarFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, R.string.permission_rationale, Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_READ);
            }
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, R.string.permission_rationale, Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ);
            }
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String name = mVisitaEnCurso.institution.name +"_"  + timeStamp + "_";
                photoFile = crearArchivoDeImagen(name);
            } catch (IOException ex) {
                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                mBuilder.detectFileUriExposure();
                uriSavedImage = Uri.fromFile(photoFile);
                grantPermissionsToUri(this, takePictureIntent, uriSavedImage);
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

    private File crearArchivoDeImagen(String fileName) throws IOException {
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "ARTImages");
        if(!imagesFolder.exists()){
            imagesFolder.mkdir();
            Log.i(this.toString(),"Creada carpeta de imagenes");
        }

        File image = new File(imagesFolder, fileName + ".jpg");
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO){
            if (resultCode == RESULT_OK){
                Image imagen = new Image(){{
                    visit = mVisitaEnCurso;
                    URLImage = uriSavedImage.toString();
                }};

            mVisitaEnCurso.images.add(imagen);
            }

            Fragment frg = getSupportFragmentManager().findFragmentByTag(TAG_CONSTANCIA_VISITA);
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(frg)
                    .attach(frg)
                    .commit();
        }
    }

    @Override
    public void onImagenPressed(final Image imagen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.borrarImagen)
                .setTitle(R.string.borrarImagen_Title)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrarImagen(imagen);
                    }
                });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void borrarImagen(Image imagen) {

        mVisitaEnCurso.images.remove(imagen);

        //TODO: Esto en realidad no lo está borrando, si nos queda tiempo revisarlo
        new File(imagen.URLImage).delete();

        if(mVisitaEnCurso.images.isEmpty()){
            getSupportFragmentManager().popBackStack();
        } else {
            Fragment frg = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_IMAGENES);
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(frg)
                    .attach(frg)
                    .commit();
        }

    }
    
    public void onNewRiskFragmentInteraction(WorkingMan workingMan) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, RiskSelectorFragment.newInstance(workingMan))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDescartar(WorkingMan workingMan) {
        super.onBackPressed();
    }

    @Override
    public void OnMedirRuido() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, MedidorDeRuidoFragment.newInstance(mVisitaEnCurso),TAG_FRAGMENT_MEDICION_RUIDO)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRuidoPressed(final Noise ruido) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.borrarRuido)
                .setTitle(R.string.borrarMedicion_Title)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrarRuido(ruido);
                    }
                });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void borrarRuido(Noise ruido) {

        mVisitaEnCurso.noises.remove(ruido);

        Fragment frg = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_MEDICION_RUIDO);
        getSupportFragmentManager()
                .beginTransaction()
                .detach(frg)
                .attach(frg)
                .commit();
    }


}
