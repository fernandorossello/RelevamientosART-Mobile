package Modelo;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Result implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public int type;

    @DatabaseField
    public Date completed_at;

    @Expose
    @DatabaseField(foreign = true)
    public Task task;


    public String toJson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }


}
