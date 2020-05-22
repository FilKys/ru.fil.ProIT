package API.ReadFile;

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
import java.util.Iterator;

public class ReadCSV implements SQLInsert{

    public StringBuilder read(File fileIn, String tableDB) {
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
                            sb.append("),\n");
                        else if(i == (data.length - 1) && (line = br.readLine()) == null)
                            sb.append(");");
                        else
                            sb.append(",");
                    }
                } else
                    j++;
            }
            System.out.println(sb);
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
