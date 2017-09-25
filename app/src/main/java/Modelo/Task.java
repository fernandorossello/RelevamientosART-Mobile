package Modelo;

import com.j256.ormlite.field.DatabaseField;
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

    @DatabaseField(foreign = true)
    public Visit visit;

    public String getTypeName(){
        return EnumTareas.getById(this.type).name;
    };

    public String getTypeShortName(){
        return EnumTareas.getById(this.type).shortName;
    };

}
