package Modelo;

import java.io.Serializable;
import java.util.List;

public class CAPResult extends Result implements Serializable{

    public String topic;

    public int attendees_count;

    public List<Attendee> attendees;

    public String coordinators;

    public String usedMaterials;

    public String deliveredMaterials;
}
