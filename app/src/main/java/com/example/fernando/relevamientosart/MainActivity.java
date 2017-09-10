package com.example.fernando.relevamientosart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fernando.relevamientosart.ConstanciaCapacitacion.ConstanciaCapacitacionFragment;
import com.example.fernando.relevamientosart.ConstanciaVisita.ImageFragment;
import com.example.fernando.relevamientosart.Login.LoginActivity;
import com.example.fernando.relevamientosart.RAR.RARFragment;
import com.example.fernando.relevamientosart.RAR.RiskFragment;
import com.example.fernando.relevamientosart.ConstanciaVisita.ConstanciaVisitaFragment;
import com.example.fernando.relevamientosart.RGRL.PreguntaFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Enums.EnumTareas;
import Modelo.Image;
import Modelo.Managers.VisitManager;
import Modelo.Task;
import Modelo.Visit;
import Modelo.WorkingMan;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,VisitFragment.OnVisitSelectedListener,
        RARFragment.OnTrabajadoresFragmentInteractionListener,
        ConstanciaVisitaFragment.OnEventoConstanciaListener,
        ImageFragment.OnImageListFragmentInteractionListener {

    private static final int REQUEST_TAKE_PHOTO = 1500;
    private static final int REQUEST_READ = 2000;

    private static final String TAG_CONSTANCIA_VISITA = "ConstanciaVisitaTag";

    private DBHelper mDBHelper;

    private Visit mVisitaEnCurso;

    private Uri uriSavedImage = null;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Task task;
        switch (item.getItemId()) {
            case R.id.action_rgrl: {

                task = mVisitaEnCurso.obtenerTarea(EnumTareas.RGRL);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PreguntaFragment.newInstance(task))
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

                task = mVisitaEnCurso.obtenerTarea(EnumTareas.CAPACITACION);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConstanciaCapacitacionFragment.newInstance(task))
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_rar:
                task = mVisitaEnCurso.obtenerTarea(EnumTareas.RAR);
                
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, RARFragment.newInstance(task))
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
                .replace(R.id.fragment_container, new VisitDetalleFragment().newInstance(visit))
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
    public void onTrabajadorNuevo() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RiskFragment().newInstance(null))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onTrabajadorSeleccionado(WorkingMan workingMan) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RiskFragment().newInstance(workingMan))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnTomarFoto(Visit visit) {
        tomarFoto(visit);
    }

    @Override
    public void OnVerFotosClick(Visit visit) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ImageFragment().newInstance(visit))
                .addToBackStack(null)
                .commit();
    }

    private void tomarFoto(Visit visit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, R.string.permission_rationale, Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_READ);
            }
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String name = visit.nombreInstitucion() +"_"  + timeStamp + "_";
                photoFile = crearArchivoDeImagen(name);
            } catch (IOException ex) {
                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
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
    public void onImagenPressed(Image imagen) {
        Toast.makeText(this, imagen.URLImage, Toast.LENGTH_SHORT).show();
    }
}
