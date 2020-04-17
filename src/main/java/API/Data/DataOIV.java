package API.Data;

import java.sql.Timestamp;

public class DataOIV {
    private Long id;
    private String name, headOrg, oiv, statusSign;
    private Timestamp timeChange;

    public DataOIV(long id, String name, String headOrg, String oiv, String statusSign, Timestamp timeChange) {
        this.id = id;
        this.name = name;
        this.headOrg = headOrg;
        this.oiv = oiv;
        this.statusSign = statusSign;
        this.timeChange = timeChange;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHeadOrg() {
        return headOrg;
    }

    public String getOiv() {
        return oiv;
    }

    public String getStatusSign() {
        return statusSign;
    }
}
