package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import Excepciones.ValidationException;
import Helpers.ValidacionHelper;

public class WorkingMan extends Employee implements Serializable {

    public WorkingMan(){
        if(riskList == null) {
            riskList = new ArrayList<>();
        }
    }

    @DatabaseField
    public Date checked_in_on;

    @DatabaseField
    public Date exposed_from_at;

    @DatabaseField
    public Date exposed_until_at;

    @DatabaseField(foreign = true)
    public RARResult result;

    @ForeignCollectionField
    public Collection<Risk> riskList;

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

        for (Risk riesgo: this.riskList) {
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
    }
}
