package API.DB;

import API.Data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetDataFromDB {

    static private final int DELTA_PAGINATOR = 50;
    private static Logger logger = LogManager.getLogger(GetDataFromDB.class);

    @Autowired
    private Database db = new Database();

    public List<DataKladr> getKladr(int pagin, int col, String type, String likeText){
        StringBuilder sql = new StringBuilder()
                .append(getSelectSQL("kladr"))
                .append(getSQLSort(col,"kladr",type,likeText))
                .append(paginationSQL(pagin))
                .append(";");
        logger.info("Create SQL for getKladr - "+sql);
        return db.getDataKladr(sql);
    }

    public List<DataDocs> getDocs(int pagin, int col, String type, String likeText){
        StringBuilder sql = new StringBuilder()
                .append(getSelectSQL("docs"))
                .append(getSQLSort(col,"docs",type,likeText))
                .append(paginationSQL(pagin))
                .append(";");
        logger.info("Create SQL for getDocs - "+sql);
        return db.getDataDocs(sql);
    }

    public List<DataOIV> getOiv(int pagin, int col, String type, String likeText){
        StringBuilder sql = new StringBuilder()
                .append(getSelectSQL("oiv"))
                .append(getSQLSort(col,"oiv",type,likeText))
                .append(paginationSQL(pagin))
                .append(";");
        logger.info("Create SQL for getOiv - "+sql);
        return db.getDataOIV(sql);
    }

    public Integer getCountPages(String page) {
        StringBuilder sql = new StringBuilder().append("SELECT count(*) FROM ");
        switch (page) {
            case "kladr":
                sql.append(" public.kladr");
                break;
            case "docs":
                sql.append(" public.docs");
                break;
            case "oiv":
                sql.append(" public.oiv");
                break;
        }
        if (sql.toString().contains("public")) {
            logger.info("Create SQL for getCountPages - "+sql);
            return (int) Math.ceil((double) db.getCountRows(sql.toString())/ DELTA_PAGINATOR);
        } else
            logger.error("WARNING!! No correct create SQL for getCountPages - "+sql);
            return null;
    }

    public List<DataCatalogs> getMenu() {
        logger.info("Create SQL for getMenu - SELECT id, name, link FROM public.catalogs;");
        return db.getDataMenu("SELECT id, name, link " +
                "FROM public.catalogs;");
    }

    private String getSelectSQL(String tableDB) {
        switch (tableDB) {
            case "kladr":
                return "SELECT big_name, " +
                        "sm_name, " +
                        "code, " +
                        "postcode, " +
                        "code_ifns, " +
                        "code_ter_ifns, " +
                        "code_okato, " +
                        "status, " +
                        "status_sign, " +
                        "time_change "+
                        "FROM public.kladr";
            case "oiv":
                return "SELECT id, name, head_org, oiv, status_sign, time_change FROM public.oiv";
            case "docs":
                return "SELECT id, " +
                        "name, " +
                        "oiv, " +
                        "xsd_schemas, " +
                        "status_open, " +
                        "note, " +
                        "elec_doc, " +
                        "real_doc, " +
                        "status_sign, " +
                        "web_services, " +
                        "time_change "+
                        "FROM public.docs";
        }
        return null;
    }

    private StringBuilder getSQLSort(int col, String table, String type, String likeText){
        StringBuilder sql = new StringBuilder();
        switch (type){
            case "DESC":
            case "ASC":
                sql.append(" ORDER BY ")
                        .append(getNameColumn(table, col))
                        .append(type);
                break;
            case "START":
                sql.append(" where ")
                        .append(getNameColumn(table,col))
                        .append(" like '")
                        .append(likeText)
                        .append("%'");
                break;
            case "CONTAINS":
                sql.append(" where ")
                        .append(getNameColumn(table,col))
                        .append(" like '%")
                        .append(likeText)
                        .append("%'");
                break;
            case "FINISH":
                sql.append(" where ")
                        .append(getNameColumn(table,col))
                        .append(" like '%")
                        .append(likeText)
                        .append("'");
                break;
            case "EQUALS":
                sql.append(" where ")
                        .append(getNameColumn(table,col))
                        .append(" like '")
                        .append(likeText)
                        .append("'");
                break;
        }
        return sql;
    }

    private String paginationSQL(int pagin) {
        StringBuilder sb = new StringBuilder().append(" LIMIT ");
        sb.append(DELTA_PAGINATOR);
        int start = 0;
        for (int i = 0; i < pagin-1; i++) {
            start += DELTA_PAGINATOR;
        }
        sb.append(" OFFSET ");
        sb.append(start);
        return sb.toString();
    }

    private String getNameColumn(String tableDB, int id) {
        switch (tableDB) {
            case "kladr":
                switch (id) {
                    case 1:
                        return "big_name ";
                    case 2:
                        return "sm_name ";
                    case 3:
                        return "code ";
                    case 4:
                        return "postcode ";
                    case 5:
                        return "code_ifns ";
                    case 6:
                        return "code_ter_ifns ";
                    case 7:
                        return "code_okato ";
                    case 8:
                        return "status ";
                    case 9:
                        return "status_sign ";
                    case 10:
                        return "time_change ";
                }
                return "error";
            case "oiv":
                switch (id) {
                    case 1:
                        return "id ";
                    case 2:
                        return "name ";
                    case 3:
                        return "head_org ";
                    case 4:
                        return "oiv ";
                    case 5:
                        return "status_sign ";
                    case 6:
                        return "time_change ";
                }
                return "error";
            case "docs":
                switch (id) {
                    case 1:
                        return "id ";
                    case 2:
                        return "name ";
                    case 3:
                        return "oiv ";
                    case 4:
                        return "xsd_schemas ";
                    case 5:
                        return "web_services ";
                    case 6:
                        return "status_open ";
                    case 7:
                        return "note ";
                    case 8:
                        return "elec_doc ";
                    case 9:
                        return "real_doc ";
                    case 10:
                        return "status_sign ";
                    case 11:
                        return "time_change ";
                }
                return "error";
        }
        return null;
    }

    public List<User> getUsers() {
        String sql = "SELECT id, login, pass, role FROM public.users";
        logger.info("Create SQL for getUsers - "+sql);
        return db.getUsers(sql);
    }
}
