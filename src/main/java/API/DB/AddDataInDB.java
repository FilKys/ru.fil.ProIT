package API.DB;

import API.ReadFile.ReadCSV;
import API.ReadFile.ReadXLSX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class AddDataInDB {

    private static final Logger logger = LogManager.getLogger(AddDataInDB.class);

    private final ReadXLSX readXLSX = new ReadXLSX();
    private final ReadCSV readCSV = new ReadCSV();

    @Autowired
    private Database db = new Database();

    public String addInDBFromFile(MultipartFile file, String tableDB) throws SQLException {
        try {
            File fileIn = new File(Objects.requireNonNull(file.getOriginalFilename()));
            logger.info("File " + fileIn.getName() + " open");
            String typeFile = fileIn.getName().toLowerCase().substring(fileIn.getName().lastIndexOf('.'));
            StringBuilder sql = new StringBuilder();
            switch (typeFile) {
                case ".csv":
                    logger.info("Read " + fileIn.getName() + " - start");
                    sql = readCSV.read(fileIn, tableDB);
                    logger.info("Read " + fileIn.getName() + " - stop");
                    break;
                case ".xlsx":
                    logger.info("Read " + fileIn.getName() + " - start");
                    sql = readXLSX.read(fileIn, tableDB);
                    logger.info("Read " + fileIn.getName() + " - stop");
                    break;
            }
            if (sql != null) {
                logger.info("SQL insert - " + sql);
                return db.addData(sql);
            } else {
                logger.error("SQL insert is null!!!");
                throw new Exception("SQL insert is null!!!");
            }
        } catch (Exception e) {
            logger.error("Read file failed: " + e.getMessage());
            return "ERROR! " + e.getMessage();
        }
    }

    public String addUser(String login, String pass, String role) {
        StringBuilder sql = new StringBuilder().append("INSERT INTO public.users(login, pass, role) VALUES (")
                .append("\'").append(login).append("\'").append(",")
                .append("\'").append(pass).append("\'").append(",")
                .append("\'").append(role).append("\'").append(")");
        return db.addUser(sql);
    }
}
