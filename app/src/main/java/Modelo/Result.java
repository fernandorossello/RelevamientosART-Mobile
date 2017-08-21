package Modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Result {

    //TODO: En caso de ser necesario, migrar completo el modelo de resultados.
    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public Date completed_at;

}
