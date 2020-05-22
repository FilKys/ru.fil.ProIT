package API.DB;

import API.ReadFile.ReadCSV;
import API.ReadFile.ReadXLSX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class AddDataInDB {

    private ReadXLSX readXLSX = new ReadXLSX();
    private ReadCSV readCSV = new ReadCSV();

    @Autowired
    private Database db = new Database();

    public String addInDBFromFile(MultipartFile file, String tableDB) throws SQLException {
        try {
            //        File fileIn = new File(Objects.requireNonNull(file.getOriginalFilename()));
//            File fileIn = new File("C:\\Users\\milli\\IdeaProjects\\ru.fil.ProIT\\oiv.xlsx");
            File fileIn = new File("C:\\Users\\milli\\IdeaProjects\\ru.fil.ProIT\\kladr.csv");
            String typeFile = fileIn.getName().toLowerCase().substring(fileIn.getName().lastIndexOf('.'));
            StringBuilder sql = new StringBuilder();
            switch (typeFile) {
                case ".csv":
                    sql = readCSV.read(fileIn, tableDB);
                    System.out.println("Считывание закончено!");
                    break;
                case ".xlsx":
                    sql = readXLSX.read(fileIn, tableDB);
                    System.out.println("Считывание закончено!");
                    break;
            }
            if (sql != null) {
                /*
                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    Statement statement = connection.createStatement();
                    statement.execute(sql.toString());
                    System.out.println("Запись в БД закончена!");
                } catch (SQLException e) {
                    System.out.println("Connection Failed");
                    e.printStackTrace();
                }*/
            } else {
                throw new Exception("SQL insert is null!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Load file = false";
        }
        return "Load file - completed";
    }

    public String addUser(String login, String pass, String role) {
        StringBuilder sql = new StringBuilder().append("INSERT INTO public.users(login, pass, role) VALUES (")
                .append("\'").append(login).append("\'").append(",")
                .append("\'").append(pass).append("\'").append(",")
                .append("\'").append(role).append("\'").append(")");
        return db.addUser(sql);
    }
}
