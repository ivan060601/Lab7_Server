package ServerStuff.Commands;


import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;
import DatabaseStuff.DatabaseManager;

public interface BigCommand {
    public void execute(CityTree cityTree, City city, User user);
    public String getMessage();
}
