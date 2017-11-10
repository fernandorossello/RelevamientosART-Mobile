package Modelo;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;
import java.io.Serializable;
import java.util.Date;
import Modelo.Enums.EnumStatus;


@DatabaseTable
public abstract class Result extends FirebaseFile implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public int type;

    @Expose
    @DatabaseField
    public Date completed_at;

    @Expose
    @DatabaseField(foreign = true)
    public Task task;

    public String toJson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(this);
    }

    public abstract EnumStatus getStatus();
}
