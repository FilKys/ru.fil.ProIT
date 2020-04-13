package API.Data;

public class DataOIV {
    private Long id;
    private String name, headOrg, oiv, statusSign;

    public DataOIV(long id, String name, String headOrg, String oiv, String statusSign) {
        this.id = id;
        this.name = name;
        this.headOrg = headOrg;
        this.oiv = oiv;
        this.statusSign = statusSign;
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
