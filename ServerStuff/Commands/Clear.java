package ServerStuff.Commands;

import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

import java.util.logging.Logger;

/**
 * реализация команды clear
 */
public class Clear implements Command {
    private Logger logger = Logger.getLogger("ClearLogger");
    private String msg;

    @Override
    public void execute(CityTree cityTree, User user) {
        logger.warning("DatabaseManager Missing");
    }

    public void execute(CityTree cityTree, User user, DatabaseManager databaseManager){
        if (cityTree.stream().filter(city -> city.getOwner().equals(user.getLogin())).count() != 0){
            int prevSize = cityTree.size();
            databaseManager.deleteCitiesByUser(user.getLogin());
            cityTree.removeIf(city -> city.getOwner().equals(user.getLogin()));
            if (prevSize != cityTree.size()){
                msg = (prevSize - cityTree.size()) + " element(-s) were removed";
                logger.info((prevSize - cityTree.size()) + " element(-s) were removed");
            }else{
                msg = "Nothing was removed";
                logger.info(msg);
            }
        }else{
            msg = "No cities to delete";
            logger.info(msg + " for user " + user.getLogin());
        }
    }

    @Override
    public String getMessage(){
        return this.msg;
    }
}
