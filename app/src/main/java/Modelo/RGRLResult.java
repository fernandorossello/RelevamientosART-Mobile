package Modelo;

import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public List<Question> questions;
}
