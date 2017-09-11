package Modelo;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.LazyForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WorkingMan extends Employee implements Serializable {

    public WorkingMan(){
        riskList = new ArrayList<>();
    }

    public Date checked_in_on;

    public Date exposed_from_at;

    public Date exposed_until_at;

    @ForeignCollectionField
    public Collection<Risk> riskList;

}
