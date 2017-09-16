package Modelo;

import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
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

    @ForeignCollectionField
    public Collection<WorkingMan> workingMen;
}
