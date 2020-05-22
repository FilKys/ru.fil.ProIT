package API.DB;


import API.Data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

@Service
@PropertySource("classpath:db.properties")
class Database {

    private static Logger logger = LogManager.getLogger(Database.class);

    @Value("${db.url}")
    private String DB_URL;
    @Value("${db.user}")
    private String USER;
    @Value("${db.pass}")
    private String PASS;

    protected List<DataKladr> getDataKladr(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql.toString());
            List<DataKladr> dataKladrList = new ArrayList<>();
            logger.info("Read catalog KLADR - start");
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
                        rs.getString(9),
                        rs.getTimestamp(10)));
            }
            logger.info("Read catalog KLADR - finish");
            return dataKladrList;
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    protected List<DataDocs> getDataDocs(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql.toString());
            List<DataDocs> dataDocsList = new ArrayList<>();
            logger.info("Read catalog DOCS - start");
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
                        rs.getString(10),
                        rs.getTimestamp(11)));
            }
            logger.info("Read catalog DOCS - finish");
            return dataDocsList;
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    protected List<DataOIV> getDataOIV(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql.toString());
            List<DataOIV> dataOIVList = new ArrayList<>();
            logger.info("Read catalog OIV - start");
            while (rs.next()) {
                dataOIVList.add(new DataOIV(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getTimestamp(6)));
            }
            logger.info("Read catalog OIV - finish");
            return dataOIVList;
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    protected int getCountRows(String sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int pagin = 0;
            logger.info("Read data count rows - start");
            while (rs.next()) {
                pagin = rs.getInt(1);
            }
            logger.info("Read data count rows - stop");
            return pagin;
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
//            e.printStackTrace();
            return 0;
        }
    }

    protected List<DataCatalogs> getDataMenu(String sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            List<DataCatalogs> dataCatalogsList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement
                    .executeQuery(sql);
            logger.info("Read data menu - start");
            while (rs.next()) {
                dataCatalogsList.add(new DataCatalogs(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("link")
                ));
            }
            logger.info("Read data menu - stop");
            return dataCatalogsList;
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    public List<User> getUsers(String sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            List<User> userList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            logger.info("Read users - start");
            while (rs.next()) {
                userList.add(new User(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("pass"),
                        rs.getString("role")
                ));
            }
            logger.info("Read users - stop");
            return userList;
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    public String addUser(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            Statement statement = connection.createStatement();
            int rs = statement.executeUpdate(sql.toString());
            logger.info("Update in User - completed");
            return "Completed";
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
            return "ERROR! "+e.getMessage();
        }
    }

    public String addData(StringBuilder sql) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            logger.info("Connection to db - completed");
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            logger.info("Insert - completed");
            return "Insert - completed";
        } catch (SQLException e) {
            logger.error("Connection Failed: "+e.getMessage());
            return "ERROR! "+e.getMessage();
        }
    }
}
