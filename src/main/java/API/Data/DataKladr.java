package API.Data;

public class DataKladr {
    private String bigName,smallName,statusSign;
    private Long code,postCode,codeIFNS,codeTerIFNS,codeOKATO,status;


    public DataKladr(String bigName,
                     String smallName,
                     long code,
                     long postCode,
                     long codeIFNS,
                     long codeTerIFNS,
                     long codeOKATO,
                     long status,
                     String statusSign) {
        this.bigName=bigName;
        this.smallName=smallName;
        this.code=code;
        this.postCode=postCode;
        this.codeIFNS=codeIFNS;
        this.codeTerIFNS=codeTerIFNS;
        this.codeOKATO=codeOKATO;
        this.status=status;
        this.statusSign=statusSign;
    }

    public String getBigName() {
        return bigName;
    }

    public String getSmallName() {
        return smallName;
    }

    public Long getCode() {
        return code;
    }

    public Long getPostCode() {
        return postCode;
    }

    public Long getCodeIFNS() {
        return codeIFNS;
    }

    public Long getCodeTerIFNS() {
        return codeTerIFNS;
    }

    public Long getCodeOKATO() {
        return codeOKATO;
    }

    public Long getStatus() {
        return status;
    }

    public String getStatusSign() {
        return statusSign;
    }
}
