package Modelo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import Modelo.Enums.EnumTareas;

@DatabaseTable
public class Task implements Serializable {

    @Expose
    @DatabaseField(id = true)
    public int id;

    @Expose
    @DatabaseField
    public int task_type;

    @DatabaseField
    public int status;

    @DatabaseField(foreign = true)
    public Visit visit;

    public String getTypeName(){
        return EnumTareas.getById(this.task_type).name;
    };

    public String getTypeShortName(){
        return EnumTareas.getById(this.task_type).shortName;
    };

}
