package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;

/**
 * реализация команды show
 */
public class Show implements Command{
    private String msg;

    @Override
    public void execute(CityTree cityTree, User user) {
        msg = "";
        if (cityTree.isEmpty()){
            msg = ("Collection is empty");
        }else{
            cityTree.stream().forEach(city -> msg = msg+city.toString()+"\n----------//----------\n");
            msg = msg.substring(0, msg.length()-1);
        }
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
