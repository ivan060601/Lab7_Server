package DatabaseStuff;

import CityStructure.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseManager {
    public final String dbname = "studs";
    public final String host = "pg";
    public final String dbport = "5432";
    public final String URL = "jdbc:postgresql://" + host + ":" + dbport + "/" + dbname;
    private Properties DBinfo = new Properties();

    static Logger logger = Logger.getLogger("DBLogger");
    private Connection connection;

    public DatabaseManager(){
        try {
            String configPath = System.getenv("HOME") + System.getProperty("file.separator")+"db.cfg";
            DBinfo.load(new FileInputStream(configPath));
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, DBinfo);
        } catch (SQLException e) {
            logger.severe("Unable to create an SQL connection");
            System.exit(0);
        }catch (FileNotFoundException e){
            logger.severe("Database configuration-file was not found");
            System.exit(0);
        } catch (IOException e) {
            logger.severe("Internal error. Server will be shut down now");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean init(){
        try {
            Statement s1 = connection.createStatement();
            s1.execute("CREATE TABLE IF NOT EXISTS users(user_name varchar , password varchar );");
            s1.close();


            Statement s2 = connection.createStatement();
            s2.execute("CREATE SEQUENCE IF NOT EXISTS id_seq START 1 INCREMENT 1;");
            s2.close();

            Statement s3 = connection.createStatement();
            s3.execute("CREATE TABLE IF NOT EXISTS cities(" +
                    "id integer DEFAULT nextval('id_seq')," +
                    "name varchar unique NOT NULL," +
                    "xCoordinate float NOT NULL," +
                    "yCoordinate double precision NOT NULL," +
                    "creationDate timestamp NOT NULL," +
                    "area float not null," +
                    "population integer not null," +
                    "metersAboveSeaLevel bigint not null," +
                    "establishmentDate date not null," +
                    "carCode bigint not null," +
                    "standardOfLiving varchar," +
                    "governorsBirthday date," +
                    "owner varchar not null);");
            s3.close();

            return true;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(0);
        }
        return false;
    }

    public boolean suchUserExists(String name){
        boolean bool = false;
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE user_name = (?)");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                bool = true;
            }
            rs.close();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(0);
        }
        return bool;
    }

    public boolean passwordIsCorrect(String name, String password){
        boolean bool = false;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE user_name = (?)");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getString("password").equals(password)){
                    bool = true;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(0);
        }
        return bool;
    }

    public void createUser(String name, String password){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users(user_name, password) VALUES ((?), (?))");
            ps.setString(1, name);
            ps.setString(2, password);
            ps.execute();
            ps.close();
        }catch (SQLException e){
            logger.severe(e.getMessage());
            System.exit(0);
        }
    }

    public CityTree loadCollection(){
        CityTree cityTree = new CityTree();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM cities");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                Coordinates coordinates = new Coordinates(rs.getFloat("xcoordinate"), rs.getDouble("ycoordinate"));
                LocalDateTime creationDate = rs.getTimestamp("creationdate").toLocalDateTime();
                float area = rs.getFloat("area");
                int population = rs.getInt("population");
                long metersAboveSeaLevel = rs.getLong("metersabovesealevel");
                LocalDate establishmentDate = rs.getDate("establishmentdate").toLocalDate();
                long carCode = rs.getLong("carcode");
                StandardOfLiving standardOfLiving = toSOF(rs.getString("standardofliving"));
                Human governor = new Human(rs.getDate("governorsbirthday"));
                String owner = rs.getString("owner");
                City city = new City();
                city.setCity(id, name, coordinates, creationDate , area, population, metersAboveSeaLevel, establishmentDate, carCode, standardOfLiving, governor, owner);
                cityTree.add(city);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(0);
        }
        return cityTree;
    }

    public void deleteCitiesByUser(String name){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM cities WHERE owner = (?)");
            ps.setString(1, name);
            ps.execute();
            ps.close();
        }catch (SQLException e){
            logger.severe(e.getMessage());
            System.exit(0);
        }
    }

    public void deleteCity(Long id){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM cities WHERE id = (?)");
            ps.setLong(1, id);
            ps.execute();
            ps.close();
        }catch (SQLException e){
            logger.severe(e.getMessage());
            System.exit(0);
        }
    }

    public boolean updateCity(Long id, City city){
        boolean bool = false;
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE cities " +
                    "SET name = (?), " +
                    "xcoordinate = (?), " +
                    "ycoordinate = (?), " +
                    "creationdate = (?), " +
                    "area = (?), " +
                    "population = (?), " +
                    "metersabovesealevel = (?), " +
                    "establishmentdate = (?), " +
                    "carcode = (?), " +
                    "standardofliving = (?), " +
                    "governorsbirthday = (?)" +
                    "WHERE id = (?)");

            ps.setString(1, city.getName());
            ps.setFloat(2, city.getCoordinates().getX());
            ps.setDouble(3, city.getCoordinates().getY());
            ps.setTimestamp(4, Timestamp.valueOf(city.getCreationDate()));
            ps.setFloat(5, city.getArea());
            ps.setInt(6, city.getPopulation());
            ps.setLong(7, city.getMetersAboveSeaLevel());
            ps.setDate(8, java.sql.Date.valueOf(city.getEstablishmentDate()));
            ps.setLong(9, city.getCarCode());
            ps.setString(10, city.getStandardOfLiving().name());
            ps.setDate(11, new java.sql.Date(city.getGovernor().getBirthday().getTime()));
            ps.setLong(12, id);
            ps.execute();
            bool = true;
            ps.close();
        }catch (SQLException e){
            logger.severe(e.getMessage());
            bool = false;
        }

        return bool;
    }

    public boolean addCity(City city){
        boolean bool = false;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO cities(name, xcoordinate, ycoordinate, creationdate, area, population, metersabovesealevel, establishmentdate, carcode, standardofliving, governorsbirthday, owner) " +
                                                                    "VALUES ((?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?))");
            ps.setString(1, city.getName());
            ps.setFloat(2, city.getCoordinates().getX());
            ps.setDouble(3, city.getCoordinates().getY());
            ps.setTimestamp(4, Timestamp.valueOf(city.getCreationDate()));
            ps.setFloat(5, city.getArea());
            ps.setInt(6, city.getPopulation());
            ps.setLong(7, city.getMetersAboveSeaLevel());
            ps.setDate(8, java.sql.Date.valueOf(city.getEstablishmentDate()));
            ps.setLong(9, city.getCarCode());
            ps.setString(10, city.getStandardOfLiving().name());
            ps.setDate(11, new java.sql.Date(city.getGovernor().getBirthday().getTime()));
            ps.setString(12, city.getOwner());
            ps.execute();
            bool = true;
            ps.close();
        }catch (SQLException e){
            logger.severe(e.getMessage());
            bool = false;
        }
        return bool;
    }

    private StandardOfLiving toSOF(String s){
        switch (s){
            case "MEDIUM":
                return StandardOfLiving.MEDIUM;
            case "HIGH":
                return StandardOfLiving.HIGH;
            case "ULTRA_LOW":
                return StandardOfLiving.ULTRA_LOW;
            default:
                return null;
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(0);
        }
    }
}
