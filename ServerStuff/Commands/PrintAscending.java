package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;

/**
 * реализация команды print_ascending
 */
public class PrintAscending implements Command {
    private String msg;

    @Override
    public void execute(CityTree cityTree, User user) {
        msg = "";
        if (!cityTree.isEmpty()) {
            for (City c: cityTree){
                msg = msg + c.toString() + "\n----//----\n";
            }
            msg = msg.substring(0, msg.length()-2);
        }else {
            msg = ("Collection is empty");
        }
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
