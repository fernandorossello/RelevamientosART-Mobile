package Modelo.Managers;


import Modelo.User;

public class UserManager {


    public User ValidarAcceso(String email, String clave ){
        //TODO: Reemplazar por acceso al endpoint de validación de datos
        if(email.equals("prueba@mail.com") && clave.equals("prueba")){
            User usuario = new User();
            usuario.id = 3;

            return usuario;
        }

        return null;
    }
}
