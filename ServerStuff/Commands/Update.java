package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

import java.util.Objects;
import java.util.stream.Collectors;

public class Update implements BigCommand {
    private String msg;

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        if (city == null){
            msg = "Arguments are either null or filled up incorrectly";
        }
    }

    public void execute(CityTree cityTree, long id , City city, User user, DatabaseManager databaseManager) {
        if (city == null && !Objects.isNull(id)) {
            msg = "City is null";
        }else if (Objects.isNull(id)){
            msg = "ID is null";
        }else {
            CityTree cityTree_byOwner =
                    cityTree.stream()
                            .filter(city1 -> city1.getOwner().equals(user.getLogin()))
                            .collect(Collectors.toCollection(CityTree::new));
            if (cityTree_byOwner.stream().filter(city1 -> city1.getId() == id).count() > 0) {
                int prevSize = cityTree.size();
                if (databaseManager.updateCity(id, city)) {
                    cityTree.removeIf(city1 -> (city1.getId() == id));
                    if (prevSize != cityTree.size()) {
                        city.setId(id);
                        cityTree.add(city);
                        msg = "City " + id + " was updated successfully";
                    } else {
                        msg = "Needed city wasn't found";
                    }
                } else {
                    msg = "City was not updated";
                }
            }else {
                msg = "Nothing was updated";
            }
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
