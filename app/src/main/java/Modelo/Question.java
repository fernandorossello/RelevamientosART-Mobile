package Modelo;


import java.io.Serializable;

import Modelo.Enums.EnumAnswer;

public class Question implements Serializable{

    public int id;

    public String description;

    public EnumAnswer answer;
}
