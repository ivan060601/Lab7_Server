package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

import java.util.stream.Collectors;

public class RemoveLower implements BigCommand {
    private String msg;

    public void execute(CityTree cityTree, City city, User user, DatabaseManager databaseManager) {
        if (cityTree.isEmpty()){
            msg = "Collection is empty";
        }else{
            if (city != null) {
                CityTree cityTree_byOwner =
                        cityTree.stream()
                                .filter(city1 -> city1.getOwner().equals(user.getLogin()))
                                .collect(Collectors.toCollection(CityTree::new));

                int prevSize = cityTree.size();

                for (City c : cityTree_byOwner){
                    if (c.compareTo(city) < 0){
                        databaseManager.deleteCity(c.getId());
                        cityTree.remove(c);
                    }
                }

                if (prevSize == cityTree.size()){
                    msg = "Collection wasn't changed";
                }else {
                    msg = prevSize - cityTree.size() + " element(-s) were removed";
                }
            }else {
                msg = "Collection wasn't changed";
            }
        }
    }

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        msg = "Collection wasn't changed";
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
