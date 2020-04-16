package API;


import API.Data.DataDocs;
import API.Data.DataKladr;
import API.Data.DataOIV;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO Сделать нормальную обработку ошибок

public class Database {
    static private final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/catalog";
    static private final String USER = "postgres";
    static private final String PASS = "123";

    public void addInDB(File fileIn, String tableDB) throws SQLException {
        String typeFile = fileIn.getName().toLowerCase().substring(fileIn.getName().lastIndexOf('.'));
        StringBuilder sql = new StringBuilder();
        try {
            switch (typeFile) {
                case ".csv":
                    sql = readCSV(fileIn, tableDB);
                    System.out.println("Считывание закончено!");
                    break;
                case ".xlsx":
                    sql = readXLSX(fileIn, tableDB);
                    System.out.println("Считывание закончено!");
                    break;
            }
            if (sql != null) {
                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    Statement statement = connection.createStatement();
                    statement.execute(sql.toString());
                    System.out.println("Запись в БД закончена!");
                } catch (SQLException e) {
                    System.out.println("Connection Failed");
                    e.printStackTrace();
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder readXLSX(File fileIn, String tableDB) {
        tableDB = "docs";
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(fileIn))) {
            StringBuilder sb = new StringBuilder();
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
            sb.append(getInsertSQL(tableDB));
            for (int i = 1; i <= myExcelSheet.getLastRowNum(); i++) {
                sb.append("(");
                XSSFRow row = myExcelSheet.getRow(i);
                for (Iterator<Cell> it = row.iterator(); it.hasNext(); ) {
                    Cell cell = it.next();
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            sb.append(cell.getNumericCellValue());
                            break;
                        case STRING:
                            String value = cell.getStringCellValue().replace("\n", " ")
                                    .replace("  ", " ");
                            try {
                                sb.append(Integer.parseInt(value));
                            } catch (Exception e) {
                                sb.append("\'" + value + "\'");
                            }
                            break;
                    }

                    if (!it.hasNext() && i != myExcelSheet.getLastRowNum())
                        sb.append("),\n");
                    else
                        sb.append(",");
                }
            }
            sb.append(";");
            System.out.println(sb);
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private StringBuilder readCSV(File fileIn, String tableDB) {
        try {
            StringBuilder sb = new StringBuilder().append(getInsertSQL(tableDB));
            BufferedReader br = new BufferedReader(new FileReader(fileIn, Charset.forName("windows-1251")));
            String line = br.readLine();
            int j = 0;
            while ((line = br.readLine()) != null) {
                if (j != 0) {
                    sb.append("(");
                    String[] data = line.split(";");
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].replaceAll("\"", "");
                        if (data[i].length() != 0) {
                            if (i < 2 || i == (data.length - 1))
                                sb.append("\'" + data[i] + "\'");
                            else if (i == 2 && data[i].toLowerCase().contains("e")) {
                                if (data[i].contains(","))
                                    data[i] = data[i].replace(',', '.');
                                sb.append(new BigDecimal(data[i].toUpperCase()).longValue());
                            } else sb.append(data[i]);
                        } else {
                            sb.append("null");
                        }
                        if (i == (data.length - 1) && (line = br.readLine()) != null)
                            sb.append("),");
                        else
                            sb.append(",");
                    }
                } else
                    j++;
            }

            sb.append(");\n");
            System.out.println(sb);
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DataKladr> getKladr(int col, String type) {
        if (col == 0) {
            StringBuilder sql = new StringBuilder().append(getSelectSQL("kladr")).append(";");
            return getDataKladr(sql);
        } else {
            StringBuilder sql = new StringBuilder().append(" ORDER BY ")
                    .append(getNameColumn("kladr", col))
                    .append(type);
            return getDataKladr(sql);
        }
    }

    private List<DataKladr> getDataKladr(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql.toString());
            List<DataKladr> dataKladrList = new ArrayList<>();
            while (rs.next()) {
                dataKladrList.add(new DataKladr(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getLong(5),
                        rs.getLong(6),
                        rs.getLong(7),
                        rs.getLong(8),
                        rs.getString(9)));
            }
            System.out.println("Считывание каталога kladr закончено");
            return dataKladrList;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
    }

    public List<DataDocs> getDocs(int col, String type) {
        if (col == 0) {
            StringBuilder sql = new StringBuilder().append(getSelectSQL("docs")).append(";");
            return getDataDocs(sql);
        } else {
            StringBuilder sql = new StringBuilder().append(" ORDER BY ")
                    .append(getNameColumn("docs", col))
                    .append(type);
            return getDataDocs(sql);
        }
    }

    private List<DataDocs> getDataDocs(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql.toString());
            List<DataDocs> dataDocsList = new ArrayList<>();
            while (rs.next()) {
                dataDocsList.add(new DataDocs(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)));
            }
            System.out.println("Считывание каталога DOCS закончено");
            return dataDocsList;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
    }

    public List<DataOIV> getOiv(int col, String type) {
        if (col == 0) {
            StringBuilder sql = new StringBuilder().append(getSelectSQL("oiv")).append(";");
            return getDataOIV(sql);
        } else {
            StringBuilder sql = new StringBuilder().append(" ORDER BY ")
                    .append(getNameColumn("oiv", col))
                    .append(type);
            return getDataOIV(sql);
        }
    }

    private List<DataOIV> getDataOIV(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql.toString());
            List<DataOIV> dataOIVList = new ArrayList<>();
            while (rs.next()) {
                dataOIVList.add(new DataOIV(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)));
            }
            System.out.println("Считывание каталога OIV закончено");
            return dataOIVList;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
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
                        "status_sign " +
                        "FROM public.kladr";
            case "oiv":
                return "SELECT id, name, head_org, oiv, status_sign FROM public.oiv";
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
                        "web_services " +
                        "FROM public.docs";
        }
        return null;
    }

    private String getInsertSQL(String tableDB) {
        switch (tableDB) {
            case "kladr":
                return "INSERT INTO public.kladr" +
                        "(big_name, sm_name, code, postcode, code_ifns, code_ter_ifns, code_okato, status, status_sign)" +
                        " VALUES ";
            case "oiv":
                return "INSERT INTO public.oiv " +
                        "(id, name, head_org, oiv, status_sign) " +
                        "VALUES ";
            case "docs":
                return "INSERT INTO public.docs" +
                        "(id, name, oiv, xsd_schemas, web_services, status_open, note, elec_doc, real_doc, status_sign) " +
                        "VALUES ";
        }
        return null;
    }

    private String getNameColumn(String tableDB, int id) {
        switch (tableDB) {
            case "kladr":
                switch (id) {
                    case 1:
                        return "big_name";
                    case 2:
                        return "sm_name";
                    case 3:
                        return "code";
                    case 4:
                        return "postcode";
                    case 5:
                        return "code_ifns";
                    case 6:
                        return "code_ter_ifns";
                    case 7:
                        return "code_okato";
                    case 8:
                        return "status";
                    case 9:
                        return "status_sign";
                }
                return "error";
            case "oiv":
                switch (id) {
                    case 1:
                        return "id";
                    case 2:
                        return "name";
                    case 3:
                        return "head_org";
                    case 4:
                        return "oiv";
                    case 5:
                        return "status_sign";
                }
                return "error";
            case "docs":
                switch (id) {
                    case 1:
                        return "id";
                    case 2:
                        return "name";
                    case 3:
                        return "oiv";
                    case 4:
                        return "xsd_schemas";
                    case 5:
                        return "web_services";
                    case 6:
                        return "status_open";
                    case 7:
                        return "note";
                    case 8:
                        return "elec_doc";
                    case 9:
                        return "real_doc";
                    case 10:
                        return "status_sign";
                }
                return "error";
        }
        return null;
    }

}
