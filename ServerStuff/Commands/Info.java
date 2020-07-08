package ServerStuff.Commands;

import CityStructure.CityTree;
import ClientStuff.User;

import java.time.format.DateTimeFormatter;

/**
 * реализация команды info
 */
public class Info implements Command{
    private String msg;

    @Override
    public void execute(CityTree cityTree, User user) {
        if(cityTree.isEmpty()){
            msg = "Collection is empty";
        }else {
            msg = ("Amount of cities in storage: " + cityTree.size() + "\n" +
                    "Date of initialization: " + cityTree.getAuthDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n" +
                    "Collection type: " + cityTree.getClass().getSimpleName());
        }
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
