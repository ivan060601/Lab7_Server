package ServerStuff.Commands;

import CityStructure.CityTree;
import ClientStuff.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * реализация команды history
 */
public class History implements Command {
    private HashMap<String, String[]> histories = new HashMap<>();
    private boolean empty = true;
    private String msg;

    @Override
    public void execute(CityTree cityTree, User user) {
        if (empty == true){
            msg = ("History is empty");
        }else{
            if (histories.containsKey(user.getLogin())) {
                String[] comArray = histories.get(user.getLogin());
                int k = 1;
                msg = "";
                for (int i = comArray.length - 2; i >= 0; i--) {
                    if (comArray[i] != null) {
                        msg = msg + (k + ". " + comArray[i]) + "\n";
                        k++;
                    }
                }

                if (comArray[comArray.length - 1] != null) {
                    msg = msg + (k + ". " + comArray[comArray.length - 1] + "\n");
                }
                msg = msg.substring(0, msg.length() - 1);
            }else {
                msg = "History is empty";
            }
        }
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    public void add(String s, User user){
        this.empty = false;
        if (histories.containsKey(user.getLogin())){
            String[] comArray = histories.get(user.getLogin());
            int i = 14;
            while (i > 0){
                comArray[i] = comArray[i-1];
                i--;
            }
            comArray[0] = s;
            histories.put(user.getLogin(), comArray);
        }else{
            String[] temp = new String[15];
            temp[0] = s;
            histories.put(user.getLogin(), temp);
        }

    }


}