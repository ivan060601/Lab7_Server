package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

public class RemoveById implements BigCommand {
    private String msg;

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        msg = "Invalid argument";
    }

    public void execute(CityTree cityTree, Long idToRemove, User user, DatabaseManager databaseManager) {
        if (idToRemove != null) {
            try {
                int prevSize = cityTree.size();

                if (!cityTree.isEmpty()) {
                    if(cityTree.stream()
                            .filter(city -> city.getOwner().equals(user.getLogin()))
                            .filter(city -> city.getId() == idToRemove)
                            .count() != 0) {
                        databaseManager.deleteCity(idToRemove);
                        cityTree.removeIf(city -> (city.getId() == idToRemove));
                    }

                    if (cityTree.size() != prevSize) {
                        msg = ("City with id " + idToRemove + " was successfully removed");
                    } else {
                        msg = ("Nothing was removed");
                    }
                } else {
                    msg = ("Collection is empty");
                }
            } catch (NumberFormatException e) {
                msg = ("ID should be a number from 0 to " + Long.MAX_VALUE);
            }
        }else {
            msg = "ID is null";
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
