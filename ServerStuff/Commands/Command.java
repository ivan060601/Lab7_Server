package ServerStuff.Commands;

import CityStructure.CityTree;
import ClientStuff.User;

public interface Command{
    public void execute(CityTree cityTree, User user);
    public String getMessage();
}