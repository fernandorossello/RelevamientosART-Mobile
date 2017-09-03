package Modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WorkingMan extends Employee implements Serializable {

    public Date checked_in_on;

    public Date exposed_from_at;

    public Date exposed_until_at;

    public List<Risk> riskList;

}
