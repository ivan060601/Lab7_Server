import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.Terminal;
import DatabaseStuff.DatabaseManager;
import ServerStuff.Server;


import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Main {
    private static Server server;
    private static CityTree cityTree;
    static Logger logger = Logger.getLogger("MainLogger");
    private static DatabaseManager databaseManager = new DatabaseManager();

    public static void main(String[] args) {
        server = new Server();
        databaseManager.init();
        cityTree = databaseManager.loadCollection();
        cityTree.setAuthDateTime(LocalDateTime.now());
        logger.info(cityTree.size() + " element(-s) were loaded from the database");
        Terminal t = new Terminal(cityTree, server, databaseManager);
        try {
            t.start();
        }catch (NullPointerException | InterruptedException e){
            logger.severe(e.getMessage());
            System.exit(0);
        }
    }
}

