package Modelo;


import com.google.gson.GsonBuilder;

import org.json.JSONObject;


import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import Modelo.Enums.EnumAnswer;
import Modelo.Enums.EnumStatus;
import Modelo.Enums.EnumTareas;

@DatabaseTable
public class RGRLResult extends Result implements Serializable{

    public RGRLResult(){
        if (questions == null) {
            questions = new ArrayList<>();
        }

        type = EnumTareas.RGRL.id;
    }

    @ForeignCollectionField
    public Collection<Question> questions;

    @Override
    public EnumStatus getStatus() {
        Boolean tieneContestadas = false;
        Boolean tieneSinContestar = false;

        for (Question pregunta : questions) {

            if(pregunta.answer == EnumAnswer.NULL.id){
                tieneSinContestar = true;
            } else {
                tieneContestadas = true;
            }

            if(tieneContestadas && tieneSinContestar){
                return EnumStatus.ENPROCESO;
            }
        }

        if(tieneContestadas){
            return EnumStatus.FINALIZADA;
        }

        return  EnumStatus.PENDIENTE;
    }
}
