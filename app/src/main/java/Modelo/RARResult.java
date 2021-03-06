package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

import Modelo.Enums.EnumStatus;
import Modelo.Enums.EnumTareas;
import java.io.Serializable;

@DatabaseTable
public class RARResult extends Result implements Serializable {

    public RARResult(){

        if(working_men == null) {
            working_men = new ArrayList<>();
        }

        type = EnumTareas.RAR.id;
        status = EnumStatus.ENPROCESO.id;
    }

    @Expose
    @ForeignCollectionField
    public Collection<WorkingMan> working_men;

    @Override
    public EnumStatus getStatus() {

        if(this.status == EnumStatus.ENVIADA.id){
            return EnumStatus.ENVIADA;
        }

        if(this.working_men.isEmpty()){
            return EnumStatus.PENDIENTE;
        }
        return EnumStatus.FINALIZADA;

    }
}
