package Modelo;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import Modelo.Enums.EnumTareas;
import java.io.Serializable;

@DatabaseTable
public class RARResult extends Result implements Serializable {

    public RARResult(){

        if(workingMen == null) {
            workingMen = new ArrayList<>();
        }

        type = EnumTareas.RAR.id;
    }

    public String topic;

    @Expose
    @ForeignCollectionField
    public Collection<WorkingMan> workingMen;
}
