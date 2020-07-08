package ServerStuff.Commands;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.Command;
import ClientStuff.Terminal;
import ClientStuff.User;
import ServerStuff.Checkers.CheckParameter;
import ServerStuff.Checkers.NullPointerChecker;
import ServerStuff.Checkers.WrongFieldChecker;
import ServerStuff.Server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExecuteScript implements BigCommand {
    private Server server;
    private Terminal fileTerminal;
    private String msg = "Script executed";
    private Gson gson = new Gson();
    private NullPointerChecker np = new NullPointerChecker();
    private WrongFieldChecker wf = new WrongFieldChecker();

    public ExecuteScript(Terminal terminal, Server server){
        this.server = server;
        this.fileTerminal = terminal;
    }

    @Override
    public void execute(CityTree cityTree, City city, User user) {
        server.writeLine("Invalid argument");
    }

    public void execute(CityTree cityTree, String fileName, User user){
        String path = System.getenv("HOME");
        fileName = path + System.getProperty("file.separator") + fileName;

        try{
                File script = new File(fileName);

                if (!script.exists()) {
                   msg = ("File does not exist");
                    throw new FileNotFoundException();
                }
                if (!script.isFile()) {
                    msg = ("This is not a file");
                    throw new FileNotFoundException();
                }
                if (!script.canRead()) {
                    msg = ("File is unreachable");
                    throw new FileNotFoundException();
                }
                if (!script.canWrite()){
                    msg = ("File is unreachable");
                    throw new FileNotFoundException();
                }

                Scanner scanner = new Scanner(script);
                while (scanner.hasNext()) {
                    String newLine = scanner.nextLine().trim();
                    String[] arr = newLine.split(" ", 2);

                    if (arr.length == 1) {
                        fileTerminal.startWithCommand(new Command(arr[0],null, user));
                    } else {
                        switch (arr[0]){
                            case "add":
                            case "add_if_max":
                            case "remove_lower":
                                fileTerminal.startWithCommand(new Command(arr[0], makeCityFromJSON(arr[1]), user));
                                break;
                            default:
                                fileTerminal.startWithCommand(new Command(arr[0], arr[1], user));
                                break;
                        }
                    }
                }

        }catch (FileNotFoundException e){
            msg = (msg + (". Script was not executed"));
            return;
        }catch (OutOfMemoryError e) {
            msg = ("File is too big");
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public City makeCityFromJSON(String s){
        try {
            City c = gson.fromJson(s, City.class);
            np.checkEverything(c, CheckParameter.WHITHOUT_ASKING);
            wf.checkEverything(c, CheckParameter.WHITHOUT_ASKING);
            return c;
        }catch (JsonSyntaxException e){
            msg = ("JSON Syntax error. ");
        }catch (NumberFormatException e){
            msg = ("Some number-fields have incorrect values. ");
        }
        return null;
    }
}
