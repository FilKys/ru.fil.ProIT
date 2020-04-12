package API;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class Database {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/catalog";
    static final String USER = "postgres";
    static final String PASS = "123";

    public void addInDB(File fileIn, String tableDB) throws SQLException {
        String typeFile = fileIn.getName().toLowerCase().substring(fileIn.getName().lastIndexOf('.'));
        StringBuilder sql = new StringBuilder();
        switch (typeFile) {
            case ".csv":
                sql = readCSV(fileIn, tableDB);
                System.out.println("Считывание закончено!");
                break;
            case ".xls":
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
        }
    }

    private StringBuilder readXLSX(File fileIn, String tableDB) {
        tableDB = "docs";
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(fileIn))) {
            StringBuilder sb = new StringBuilder();
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
            for (int i = 1; i <= myExcelSheet.getLastRowNum(); i++) {
                sb.append(getInsertSQL(tableDB));
                XSSFRow row = myExcelSheet.getRow(i);
                for (Iterator<Cell> it = row.iterator(); it.hasNext(); ) {
                    Cell cell = it.next();
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            sb.append(cell.getNumericCellValue());
                            break;
                        case STRING:
                            String value = cell.getStringCellValue().replace("\n"," ")
                                    .replace("  "," ");
                            try {
                                sb.append(Integer.parseInt(value));
                            } catch (Exception e) {
                                sb.append("\'" + value + "\'");
                            }
                            break;
                    }

                    if (!it.hasNext())
                        sb.append(");\n");
                    else
                        sb.append(",");
                }
            }
            System.out.println(sb);
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private StringBuilder readCSV(File fileIn, String tableDB) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(fileIn, Charset.forName("windows-1251")));
            String line = br.readLine();
//            int j = 0;
            while ((line = br.readLine()) != null) {
//                j++;
//                System.out.println("Строка " + j);
                String[] data = line.split(";");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replaceAll("\"", "");
                    if (i == 0)
                        sb.append(getInsertSQL(tableDB));
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
                    if (i == (data.length - 1))
                        sb.append(");\n");
                    else
                        sb.append(",");
                }
            }
            System.out.println(sb);
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getInsertSQL(String tableDB) {
        switch (tableDB) {
            case "kladr":
                return "INSERT INTO public.kladr" +
                        "(big_name, sm_name, code, postcode, code_ifns, code_ter_ifns, code_okato, status, status_sign)" +
                        " VALUES (";
            case "oiv":
                return "INSERT INTO public.oiv " +
                        "(id, name, head_org, oiv, status_sign) " +
                        "VALUES (";
            case "docs":
                return "INSERT INTO public.docs" +
                        "(id, name, oiv, xsd_schemas, web_services, status_open, note, elec_doc, real_doc, status_sign) " +
                        "VALUES (";
        }
        return null;
    }
}
