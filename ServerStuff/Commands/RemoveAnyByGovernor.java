package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import CityStructure.Human;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class RemoveAnyByGovernor implements BigCommand {
    private String msg;

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        msg = "Invalid argument";
    }

    public void execute(CityTree cityTree, Human human, User user, DatabaseManager databaseManager){
        if (human == null){
            msg = "Human is null";
        }else {
            int prevSize = cityTree.size();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            CityTree cityTree_byOwner =
            cityTree.stream()
                    .filter(city1 -> city1.getOwner().equals(user.getLogin()))
                    .collect(Collectors.toCollection(CityTree::new));

            for (City c : cityTree_byOwner){
                if (c.getGovernor() != null){
                    if (df.format(c.getGovernor().getBirthday()). equals(df.format(human.getBirthday()))) {
                        databaseManager.deleteCity(c.getId());
                        cityTree.remove(c);
                        msg = "City " + c.getId() + " was successfully removed";
                        break;
                    }
                }else{
                    msg = "Nothing was removed";
                }
            }
            if (prevSize == cityTree.size()){
                msg = "Nothing was removed";
            }
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
