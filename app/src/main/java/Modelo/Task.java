package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import Modelo.Enums.EnumTareas;

@DatabaseTable
public class Task implements Serializable {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public int status;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public Visit visit;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    public Result result;

    public String getTypeName(){
        return EnumTareas.getById(this.type).name;
    };
}
