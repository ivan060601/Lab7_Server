package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.User;

public class FilterStartsWithName implements BigCommand {
    private String msg;
    @Override
    public void execute(CityTree cityTree, City city, User user) {
        msg = "Invalid argument";
    }

    public void execute(CityTree cityTree, String subName){
        boolean b = false;
        if (!cityTree.isEmpty()){
            msg = "";
            for (City c: cityTree){
                if(c.getName().startsWith(subName)){
                    b = true;
                    msg = msg + (c.toString()) + "\n----//----\n";
                }
            }

            if (!b){
                msg = ("Nothing was found");
            }else {
                msg = msg.substring(0, msg.length()-1);
            }
        }else {
            msg = ("Collection is empty");
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
