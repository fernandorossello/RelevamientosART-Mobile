package Modelo;

import android.widget.Toast;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;

import Modelo.Enums.EnumTareas;
import Modelo.Managers.ResultManager;

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

}
