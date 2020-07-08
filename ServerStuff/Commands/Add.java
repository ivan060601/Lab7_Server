package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

import java.util.logging.Logger;

/**
 * реализация команды add
 */
public class Add implements BigCommand {
    private String msg;
    static Logger logger = Logger.getLogger("AddLogger");

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        logger.info("DatabaseManager is missing");
        msg = "Nothing was added to the collection";
    }

    public void execute(CityTree cityTree, City city, User user, DatabaseManager dm) {
        if (city == null){
            logger.warning("Original city is null");
            msg = "Original city is null";
        }else {
            city.setOwner(user.getLogin());
            try {
                int prevSize = cityTree.size();

                if (!cityTree.contains(city)) {
                    if (dm.addCity(city)) {
                        cityTree.add(city);
                    }
                }
                if (prevSize != cityTree.size()) {
                    logger.info("Element was successfully added to the collection");
                    msg = ("Element was successfully added to the collection.");
                } else {
                    logger.info("Collection already contains such element");
                    msg = ("Collection already contains such element");
                }
            } catch (NumberFormatException e) {
                logger.warning("Some number-fields have incorrect values");
                msg = ("Some number-fields have incorrect values. Try changing them");
            }
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }


}