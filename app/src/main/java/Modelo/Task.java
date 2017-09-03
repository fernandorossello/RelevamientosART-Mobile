package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Task {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public int status;

    @DatabaseField(foreign = true)
    public Result result;

    public String getTypeName(){
        return EnumTareas.getById(this.type).name;
    };
}
