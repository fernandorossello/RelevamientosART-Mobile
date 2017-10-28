package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import Excepciones.ValidationException;
import Helpers.ValidacionHelper;

@DatabaseTable
public class WorkingMan extends Employee implements Serializable {

    public WorkingMan(){
        if(risk_list == null) {
            risk_list = new ArrayList<>();
        }
    }

    @Expose
    @DatabaseField
    public Date checked_in_on;

    @Expose
    @DatabaseField
    public Date exposed_from_at;

    @Expose
    @DatabaseField
    public Date exposed_until_at;

    @DatabaseField(foreign = true)
    public RARResult result;

    @Expose
    @ForeignCollectionField
    public Collection<Risk> risk_list;

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof WorkingMan))return false;
        WorkingMan otherWM = (WorkingMan)other;
        return id == otherWM.id;
    }

    public String obtenerCodigosDeRiesgos() {
        String codigos = "";

        for (Risk riesgo: this.risk_list) {
            codigos += riesgo.code + "; ";
        }

        return codigos;
    }

    public void Validar() throws ValidationException{

        ValidacionHelper.NullOrEmpty(name,"nombre");
        ValidacionHelper.NullOrEmpty(lastName,"apellido");
        ValidacionHelper.CantidadCaracteres(cuil,11,"CUIL");

        ValidacionHelper.Null(checked_in_on,"fecha de ingreso");
        ValidacionHelper.Null(exposed_from_at,"fecha de inicio");

        ValidacionHelper.FechaPosterior(checked_in_on, new Date(),"fecha de ingreso");
        ValidacionHelper.FechaPosterior(checked_in_on,exposed_from_at, "fecha de ingreso");
        ValidacionHelper.FechaPosterior(exposed_from_at, new Date(),"fecha de inicio");

        if(exposed_until_at != null) {
            ValidacionHelper.FechaPosterior(exposed_from_at, exposed_until_at, "fecha de inicio");
            ValidacionHelper.FechaPosterior(exposed_until_at, new Date(),"fecha de fin");
        }

        if(riskList.isEmpty()){
            throw new ValidationException("Debe seleccionar al menos un riesgo");
        }
    }

    public void fill(WorkingMan wm) {
        this.cuil = wm.cuil;
        this.name = wm.name;
        this.lastName = wm.lastName;
        this.exposed_from_at = wm.exposed_from_at;
        this.exposed_until_at = wm.exposed_until_at;
        this.checked_in_on = wm.checked_in_on;
        for (Risk riesgo :
                wm.riskList) {
         riesgo.workingMan = this;
        }
        this.riskList = wm.riskList;
    }

}
