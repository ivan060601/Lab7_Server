package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

public class AddIfMax implements BigCommand {
    private String msg;

    public void execute(CityTree cityTree, City city, User user, DatabaseManager databaseManager) {
        if (city != null) {
            if (cityTree.isEmpty()) {
                cityTree.add(city);
                msg = "Element successfully added to the collection";
            }else{
                if (city.compareTo(cityTree.first())>0){
                    if(databaseManager.addCity(city)){
                        cityTree.add(city);
                        msg = ("City was successfully added to the collection");
                    }else {
                        msg = ("City wasn't added to the collection");
                    }
                }else {
                    msg = ("City wasn't added to the collection");
                }
            }
        }else {
            msg = "Element won't be added to the collection.";
        }
    }

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        msg = ("City wasn't added to the collection");
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
