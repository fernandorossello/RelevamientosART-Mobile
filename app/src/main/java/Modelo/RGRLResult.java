package Modelo;

import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class RGRLResult extends Result implements Serializable{

    public List<Question> questions;

}
