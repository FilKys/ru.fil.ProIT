package API.Data;

public class DataDocs {
    private String name, oiv, xsdSchemas, statusOpen,
            note, elecDoc, realDoc, statusSign, webServices;
    private Long id;


    public DataDocs(long id,
                    String name,
                    String oiv,
                    String xsdSchemas,
                    String statusOpen,
                    String note,
                    String elecDoc,
                    String realDoc,
                    String statusSign,
                    String webServices) {
        this.id = id;
        this.name = name;
        this.oiv = oiv;
        this.xsdSchemas = xsdSchemas;
        this.statusOpen = statusOpen;
        this.note = note;
        this.elecDoc = elecDoc;
        this.realDoc = realDoc;
        this.statusSign = statusSign;
        this.webServices = webServices;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOiv() {
        return oiv;
    }

    public String getXsdSchemas() {
        return xsdSchemas;
    }

    public String getStatusOpen() {
        return statusOpen;
    }

    public String getNote() {
        return note;
    }

    public String getElecDoc() {
        return elecDoc;
    }

    public String getRealDoc() {
        return realDoc;
    }

    public String getStatusSign() {
        return statusSign;
    }

    public String getWebServices() {
        return webServices;
    }
}
