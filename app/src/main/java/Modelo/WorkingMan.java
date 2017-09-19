package Modelo;

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
}
