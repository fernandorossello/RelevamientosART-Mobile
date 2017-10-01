package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class WorkingMan extends Employee implements Serializable {

    public WorkingMan(){
        if(riskList == null) {
            riskList = new ArrayList<>();
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
}
