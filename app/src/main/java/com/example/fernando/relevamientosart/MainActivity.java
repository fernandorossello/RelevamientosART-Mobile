package com.example.fernando.relevamientosart;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Helpers.DBHelper;
import Modelo.Arquitectura.GestorFirebase;
import Modelo.Attendee;
import Modelo.Enums.EnumStatus;
import Modelo.Enums.EnumTareas;
import Modelo.FirebaseFile;
import Modelo.HelpQuestion;
import Modelo.Image;

import Modelo.Institution;
import Modelo.Managers.ImageManager;
import Modelo.Managers.ResultManager;

import Modelo.Managers.AttendeeManager;

import Modelo.Managers.VisitManager;
import Modelo.Managers.WorkingManManager;
import Modelo.Noise;

import Modelo.Result;

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
        MedidorDeRuidoFragment.OnNoiseListFragmentInteractionListener,
        HelpFragment.OnListFragmentInteractionListener
{
    private static final int REQUEST_TAKE_PHOTO = 1500;
    private static final int REQUEST_READ = 2000;
    private static final String TAG_CONSTANCIA_VISITA = "ConstanciaVisitaTag";
    private static final String TAG_FRAGMENT_IMAGENES = "ListaImagensTag";
    private static final String TAG_WORKING_MAN = "WorkingManTag";
    private static final String TAG_WORKING_MAN_LIST = "WorkingManListTag";
    private static final String TAG_ATTENDEE_LIST = "AttendeeListTag";
    private final String TAG_FRAGMENT_MEDICION_RUIDO = "tag_frg_medicion_ruido";

    private final String URL_ENDPOINT_VISITAS_LIST = "https://relevamientos-art.herokuapp.com/visits";
    private final String URL_ENDPOINT_VISITAS_DETALLE = "https://relevamientos-art.herokuapp.com/visits";
    private final String URL_ENDPOINT_RESULTADOS = "https://relevamientos-art.herokuapp.com/tasks/";
    private final String URL_ENDPOINT_INSTITUCIONES = "https://relevamientos-art.herokuapp.com/institutions";
    private final String URL_ENDPOINT_VISITA_ENVIO = "https://relevamientos-art.herokuapp.com/visits/";

    private DBHelper mDBHelper;

    private Visit mVisitaEnCurso;

    private Uri uriSavedImage = null;

    private StrictMode.VmPolicy.Builder mBuilder;

    RequestQueue requestQueue;

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

        requestQueue = Volley.newRequestQueue(this);

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
                        .replace(R.id.fragment_container, ConstanciaCapacitacionFragment.newInstance(selectedTask),TAG_ATTENDEE_LIST)
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_rar:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, RARFragment.newInstance(mVisitaEnCurso),TAG_WORKING_MAN_LIST)
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

        switch (id){
            case R.id.nav_ayuda:{

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, HelpFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

                break;
            }

            case R.id.nav_logout:{
                Logout();
                return true;
            }

            case R.id.nav_sincronizar:{
                try {

                    if(!TieneAccesoAInternet()) {
                        Toast.makeText(this, R.string.internet_requerido, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    Toast.makeText(this, getString(R.string.msj_sincronizandoVisitas), Toast.LENGTH_SHORT).show();
                    obtenerVisitasDesdeEnpoint();
                    enviarVisitasCompletadas();

                }catch (Exception e){
                    Toast.makeText(this, R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.nav_inicio:{

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, VisitFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void RecargarListaDeVisitas(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new VisitFragment())
                .commit();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }

    //************************************************Interacción fragments************************************************
    @Override
    public void OnVisitSelected(Visit visit) {

        mVisitaEnCurso = visit;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, VisitDetalleFragment.newInstance(visit))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onTrabajadorSeleccionado(WorkingMan workingMan) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, RiskFragment.newInstance(workingMan),TAG_WORKING_MAN)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBorrarTrabajador(WorkingMan workingMan) {

        WorkingManManager manager = new WorkingManManager(getHelper());
        try {

            manager.delete(workingMan);

            Toast.makeText(this, R.string.trabajador_borrado, Toast.LENGTH_SHORT).show();

            Fragment frg = getSupportFragmentManager().findFragmentByTag(TAG_WORKING_MAN_LIST);
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(frg)
                    .attach(frg)
                    .commit();

        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                .replace(R.id.fragment_container, NewAttendeeFragment.newInstance(attendee))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBorrarTrabajador(Attendee attendee) {
        AttendeeManager manager = new AttendeeManager(getHelper());
        try {
            manager.delete(attendee);

            Toast.makeText(this, R.string.trabajador_borrado, Toast.LENGTH_SHORT).show();

            Fragment frg = getSupportFragmentManager().findFragmentByTag(TAG_ATTENDEE_LIST);
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(frg)
                    .attach(frg)
                    .commit();

        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                    url_file = uriSavedImage.toString();
                }};
            mVisitaEnCurso.images.add(imagen);

            GestorFirebase gestorFirebase = new GestorFirebase(){
                @Override
                public void persistirEntidad(FirebaseFile archivo) throws SQLException {
                    new ImageManager(getHelper()).persist((Image)archivo);
                }

                @Override
                public String getNombreCarpetaRemota() {
                    return "images/";
                }
            };

            gestorFirebase.subirArchivo(imagen);

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
        new File(imagen.url_file).delete();

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
    //************************************************DB HELPER************************************************

    public DBHelper getHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return mDBHelper;
    }

    //************************************************ Endpoints ************************************************

    private void obtenerVisitasDesdeEnpoint() {
        try {
            Integer idUsuario = ObtenerIdUsuarioLogueado();
            String estadoVisita = "assigned";

            String URL = URL_ENDPOINT_VISITAS_LIST + "?user_id=" + idUsuario + "&status=" + estadoVisita;

            Log.i("Sincronizar","Obteniendo visitas desde "+ URL);

            final List<Integer>  idVisitas = new ArrayList<>();

            JsonArrayRequest jsonRequest = new JsonArrayRequest
                    (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject visitaJson= response.getJSONObject(i);
                                    idVisitas.add(visitaJson.getInt("id"));
                                }

                                Log.i("Sincronizar","Visitas obtenidas " + response.toString() );

                                obtenerDetalleDeVisitas(idVisitas);

                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
                            Log.e("Sincronizar","Error al obtener visitas:" + new String(error.networkResponse.data) );
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
                    return null;
                }
            };

            requestQueue.add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void obtenerDetalleDeVisitas(List<Integer> idVisitas) throws SQLException {

        final VisitManager managerVisitas = new VisitManager(this.getHelper());

        for (Integer idVisita:idVisitas) {

            if (!managerVisitas.existe(idVisita)) {

                String idBuscado = idVisita.toString();

                final int userId = ObtenerIdUsuarioLogueado();

                String URL = URL_ENDPOINT_VISITAS_DETALLE + "/" + idBuscado;

                Log.i("Sincronizar","Obteniendo detalle de visita desde:" + URL);

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int idInstitucion = response.getInt("institution_id");
                                    Visit visit = new GsonBuilder().create().fromJson(response.toString(), Visit.class);
                                    visit.user_id = userId;
                                    Log.i("Sincronizar","Se obtuvo correctamente el detalle de visita de la visita :" + response.toString() );
                                    completarInstitucion(visit, idInstitucion);
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
                                Log.e("Sincronizar","Error al obtener un detalle de visita:" + new String(error.networkResponse.data) );
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept", "application/json");

                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        return null;
                    }
                };

                requestQueue.add(jsonRequest);
            }
        }
    }

    private void completarInstitucion(final Visit visit, final int idInstitucion) {
        final VisitManager managerVisitas = new VisitManager(this.getHelper());

        String URL = URL_ENDPOINT_INSTITUCIONES + "/" + idInstitucion;

        Log.i("Sincronizar","Obteniendo institución desde:" + URL);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET,URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        visit.institution = new GsonBuilder().create().fromJson(response.toString(), Institution.class);
                        visit.institution.id = idInstitucion;

                        Log.i("Sincronizar","Obtenida institución: " + response.toString());

                        try {
                            managerVisitas.persist(visit);
                            ConfirmarRecepcionAlServidor(visit);
                        } catch (SQLException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(MainActivity.this, R.string.error_carga_institucion, Toast.LENGTH_SHORT).show();
                       Log.e("Sincronizar","Error al obtener la institución id:" + idInstitucion + " -- " +
                               new String(error.networkResponse.data) );
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
                return null;
            }
        };

        requestQueue.add(jsonRequest);
    }

    private void enviarVisitasCompletadas() throws SQLException {
        VisitManager managerVisitas = new VisitManager(getHelper());

        Integer idUsuario = ObtenerIdUsuarioLogueado();

        List<Visit> visitasCompletadas = managerVisitas.obtenerVisitasCompletadas(idUsuario);


        for (Visit visit : visitasCompletadas) {
            visit.status = EnumStatus.ENVIANDO.id;
            managerVisitas.persist(visit);
            enviarVisitaAEndpoint(visit);
        }
    }

    private void enviarVisitaAEndpoint(Visit visit) throws SQLException {
        //Debe mandar todos los resultados y las imágenes
        ResultManager resultManager =  new ResultManager(getHelper());

        VisitManager visitManager = new VisitManager(getHelper());

        if(!visitManager.DatosSubidosAFirebase(visit)){
            Toast.makeText(this, R.string.visita_reintentar_luego, Toast.LENGTH_SHORT).show();
            visit.status = EnumStatus.FINALIZADA.id;
            visitManager.persist(visit);
            return;
        }

        Log.i("Sincronizar","Enviando visita: " + visit.toString());

        for (Task task: visit.tasks) {
            Result resultado = resultManager.getResult(task);
            if (resultado != null && resultado.getStatus() != EnumStatus.ENVIADA){
                enviarResultado(resultado);
            }
        }
    }

    private void enviarResultado(final Result resultado) {
        try {
            String URL = URL_ENDPOINT_RESULTADOS + resultado.task.id +"/completion";

            Log.i("Sincronizar","Enviando resultado : " + EnumTareas.getById(resultado.type).name +
                    " de la visita " + resultado.task.visit.toString());

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.PUT, URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Sincronizar",EnumTareas.getById(resultado.type).name +
                                    " de la visita " + resultado.task.visit.toString() + " enviado correctamente");

                            resultado.status = EnumStatus.ENVIADA.id;

                            try {
                                new ResultManager(mDBHelper).persist(resultado);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            if(new VisitManager(mDBHelper).tieneTodosLosResultadosEnviados(resultado.task.visit)){
                                enviarConstanciaDeVisita(resultado.task.visit);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            resultado.task.visit.status = EnumStatus.FINALIZADA.id;
                            try {
                                new VisitManager(mDBHelper).persist(resultado.task.visit);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, R.string.error_envio_visitas, Toast.LENGTH_SHORT).show();
                            Log.e("Sincronizar","Error al enviar el resultado " +EnumTareas.getById(resultado.type).name +
                                    " de la visita " + resultado.task.visit.toString() + ". " + new String(error.networkResponse.data));
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
                    try {
                        String prueba = resultado.toJson();
                        return prueba.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };

            requestQueue.add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarConstanciaDeVisita(final Visit visit) {
        try {

            final VisitManager visitManager = new VisitManager(getHelper());
            visit.status = EnumStatus.FINALIZADA.id;

            try {
                visitManager.persist(visit);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String URL = URL_ENDPOINT_VISITA_ENVIO + visit.id + "/completion";

            Log.i("Sincronizar","Enviando constancia de visita : " + visit.toString());

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.PUT, URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            visit.status = EnumStatus.ENVIADA.id;
                            try {
                                visitManager.persist(visit);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            RecargarListaDeVisitas();

                            Log.i("Sincronizar","Visita " + visit.toString() + " enviada correctamente.");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            visit.status = EnumStatus.FINALIZADA.id;
                            try {
                                visitManager.persist(visit);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, R.string.error_envio_visitas, Toast.LENGTH_SHORT).show();
                            Log.e("Sincronizar","Error al completar visita " + visit.toString() + ". "
                                    + new String(error.networkResponse.data));

                            RecargarListaDeVisitas();
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
                    try {
                        visit.completed_at = new Date();
                        String prueba = visit.toJson();
                        return prueba.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };

            requestQueue.add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQuestionSelected(HelpQuestion item) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,HelpQuestionFragment.newInstance(item) )
                .addToBackStack(null)
                .commit();
    }

    private int ObtenerIdUsuarioLogueado(){
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("idUsuario",-1);
    }

    public void setTitle(int id){
        getSupportActionBar().setTitle(id);
    }

    private void ConfirmarRecepcionAlServidor(final Visit visit){

        final VisitManager managerVisitas = new VisitManager(this.getHelper());

        String URL = URL_ENDPOINT_VISITA_ENVIO + visit.id + "/in_process";

        Log.i("Sincronizar","Enviando confirmación de recepción de visita " + visit.toString() + " a " + URL);

        StringRequest jsonRequest = new StringRequest
                (Request.Method.PUT,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RecargarListaDeVisitas();
                        Log.i("Sincronizar","Confirmación de recepción de visita " + visit.toString() + " recibida correctamente.");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, R.string.error_carga_visitas, Toast.LENGTH_SHORT).show();
                        try {
                            managerVisitas.borrarVisita(visit);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        RecargarListaDeVisitas();
                        Log.e("Sincronizar","Error al enviar confirmación de recepción de visita " + visit.toString() + ". "
                                + new String(error.networkResponse.data));
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
                return null;
            }
        };

        requestQueue.add(jsonRequest);
    }

    private boolean TieneAccesoAInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
}
