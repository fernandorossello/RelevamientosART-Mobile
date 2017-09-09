package com.example.fernando.relevamientosart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fernando.relevamientosart.ConstanciaCapacitacion.ConstanciaCapacitacionFragment;
import com.example.fernando.relevamientosart.RAR.RARFragment;
import com.example.fernando.relevamientosart.RAR.RiskFragment;
import com.example.fernando.relevamientosart.ConstanciaVisita.ConstanciaVisitaFragment;
import com.example.fernando.relevamientosart.RGRL.PreguntaFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import Helpers.DBHelper;
import Modelo.Enums.EnumTareas;
import Modelo.Managers.VisitManager;
import Modelo.Task;
import Modelo.Visit;
import Modelo.WorkingMan;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,VisitFragment.OnVisitSelectedListener,
        RARFragment.OnTrabajadoresFragmentInteractionListener {

    private DBHelper mDBHelper;

    private Visit mVisitaEnCurso;

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
                        .replace(R.id.fragment_container, new ConstanciaVisitaFragment())
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
                .commit();
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

}
