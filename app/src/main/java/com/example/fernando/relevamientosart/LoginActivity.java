package com.example.fernando.relevamientosart;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements LoginFragment.OnRecuperarContraseniaListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ValidarSesionDeUsuario();
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new LoginFragment()).commit();
    }

    @Override
    public void onRecuperarContrasenia() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new restorePasswordFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginSuccess() {
        AbrirMainActivity();
    }

    private void AbrirMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void ValidarSesionDeUsuario(){
        Integer idUsuario = PreferenceManager.getDefaultSharedPreferences(this).getInt("idUsuario",-1);
        if(idUsuario > 0){
            AbrirMainActivity();;
        }
    }


}

