package Modelo;


import java.io.Serializable;

public class User implements Serializable{

    public int id;

    public String name;

    public String email;

    public String encryptedPassword;

    public String type;

}