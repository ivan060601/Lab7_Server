package ClientStuff;

import CityStructure.City;
import CityStructure.CityTree;
import CityStructure.Human;
import DatabaseStuff.DatabaseManager;
import ServerStuff.Commands.*;
import ServerStuff.Server;

import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Terminal {
    private DatabaseManager databaseManager;
    private Server server;
    private CityTree cityTree;
    private Clear clear = new Clear();
    private Info info = new Info();
    private Show show = new Show();
    private History history = new History();
    private Add add = new Add();
    private PrintAscending printAscending = new PrintAscending();
    private AddIfMax addIfMax = new AddIfMax();
    private RemoveLower removeLower = new RemoveLower();
    private Save save = new Save();
    private FilterStartsWithName filterStartsWithName = new FilterStartsWithName();
    private RemoveById removeById = new RemoveById();
    private ExecuteScript executeScript;
    private RemoveAnyByGovernor removeAnyByGovernor = new RemoveAnyByGovernor();
    private Update update = new Update();
    private Respond respond = new Respond();
    private Login login = new Login();
    private Register register = new Register();
    private final ExecutorService executePool = Executors.newFixedThreadPool(10);
    private final ExecutorService sendPool = Executors.newCachedThreadPool();
    static Logger logger = Logger.getLogger("TerminalLogger");

    public Terminal(CityTree cityTree, Server server, DatabaseManager databaseManager){
        this.cityTree = cityTree;
        this.server = server;
        this.databaseManager = databaseManager;
        executeScript = new ExecuteScript(this, server);
        logger.finest("Terminal created successfully");
    }

    public void start() throws InterruptedException {
        new Thread(()->{
            while (true) {
                DatagramPacket datagramPacket = server.getInput();
                SocketAddress socketAddress = datagramPacket.getSocketAddress();
                Command command = server.byteArrayToCommand(datagramPacket.getData());
                execute(command, socketAddress);
            }
        }){{start();}}.join();
    }

    private void execute(Command command, SocketAddress socketAddress) {
        executePool.execute(() -> {
            startWithCommand(command);
            send(respond, socketAddress);
        });
    }

    private void send(Respond respond, SocketAddress socketAddress) {
        sendPool.execute(() -> server.writeRespond(respond, socketAddress));
    }

    public Respond startWithCommand(Command command){
        logger.info("Server is in process of executing the command \""+command.getCommandName()+"\"");
            switch (command.getCommandName()) {
                case "login":
                    try {
                        login.execute((String[]) command.getArgument(), databaseManager);
                        respond.setMsg(login.getMessage());
                    }catch (Exception e){
                        respond.setMsg("Impossible to login");
                    }
                    break;
                case "register":
                    try {
                        register.execute((String[]) command.getArgument(), databaseManager);
                        respond.setMsg(register.getMessage());
                    }catch (Exception e){
                        respond.setMsg("Impossible to register");
                    }
                    break;
                case "server_exit":
                    logger.info("Server is closed now");
                    System.exit(0);
                    break;
                case "history":
                    history.execute(cityTree, command.getUser());
                    respond.setMsg(history.getMessage());
                    break;
                case "clear":
                    clear.execute(cityTree, command.getUser(), databaseManager);
                    respond.setMsg(clear.getMessage());
                    break;
                case "help":
                    respond.setMsg(
                            "help : вывести справку по доступным командам\n" +
                                    "info : вывести в стандартный поток вывода информацию о коллекции\n" +
                                    "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                                    "add {element} : добавить новый элемент в коллекцию\n" +
                                    "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                                    "remove_by_id id : удалить элемент из коллекции по его id\n" +
                                    "clear : очистить коллекцию\n" +
                                    "save : сохранить коллекцию в файл\n" +
                                    "execute_script file_name : считать и исполнить скрипт из указанного файл\n" +
                                    "exit : завершить программу (без сохранения в файл)\n" +
                                    "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                    "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                                    "history : вывести последние 15 команд (без их аргументов)\n" +
                                    "remove_any_by_governor governor : удалить из коллекции один элемент, значение поля governor которого эквивалентно заданному\n" +
                                    "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                                    "print_ascending : вывести элементы коллекции в порядке возрастания"
                    );
                    break;
                case "info":
                    info.execute(cityTree, command.getUser());
                    respond.setMsg(info.getMessage());
                    break;
                case "show":
                    show.execute(cityTree, command.getUser());
                    respond.setMsg(show.getMessage());
                    break;
                case "print_ascending":
                    printAscending.execute(cityTree, command.getUser());
                    respond.setMsg(printAscending.getMessage());
                    break;
                case "add":
                    add.execute(cityTree, (City) command.getArgument(), command.getUser(), databaseManager);
                    respond.setMsg(add.getMessage());
                    break;
                case "save":
                    save.execute(cityTree, command.getUser());
                    respond.setMsg(save.getMessage());
                    break;
                case "add_if_max":
                    addIfMax.execute(cityTree, (City) command.getArgument(), command.getUser(), databaseManager);
                    respond.setMsg(addIfMax.getMessage());
                    break;
                case "remove_lower":
                    removeLower.execute(cityTree, (City) command.getArgument(), command.getUser(), databaseManager);
                    respond.setMsg(removeLower.getMessage());
                    break;
                case "filter_starts_with_name":
                    if (command.getArgument() != null) {
                        filterStartsWithName.execute(cityTree, (String) command.getArgument());
                        respond.setMsg(filterStartsWithName.getMessage());
                    }else {
                        respond.setMsg("Filter is null");
                    }
                    break;
                case "remove_by_id":
                    if (command.getArgument() == null){
                        removeById.execute(cityTree, (Long) null, command.getUser(), databaseManager);
                    }else {
                        removeById.execute(cityTree, Long.valueOf((String) command.getArgument()), command.getUser(), databaseManager);
                    }
                    respond.setMsg(removeById.getMessage());
                    break;
                case "remove_any_by_governor":
                    removeAnyByGovernor.execute(cityTree, (Human) command.getArgument(), command.getUser(), databaseManager);
                    respond.setMsg(removeAnyByGovernor.getMessage());
                    break;
                case "execute_script":
                    String msg = "";
                    LoopAnalyzer la = new LoopAnalyzer();
                    try{
                        if (la.isLooped((String) command.getArgument())){
                            logger.warning("Loop was found");
                            msg = msg + ("Loop found. Remove it or try another script");
                        }else{
                            executeScript.execute(cityTree, (String) command.getArgument(), command.getUser());
                            msg = msg + (executeScript.getMessage());
                        }
                    } catch (FileNotFoundException e) {
                        logger.warning("File not found");
                        msg = msg + ("File not found");
                    } catch (SecurityException e){
                        logger.warning("File unacceptable");
                        msg = msg + ("File unacceptable");
                    }
                    respond.setMsg(msg);
                    break;
                case "update":
                    if (command.getArgument() != null && command.getAdditional() != null) {
                        update.execute(cityTree, (Long) command.getAdditional(), (City) command.getArgument(), command.getUser(), databaseManager);
                        respond.setMsg(update.getMessage());
                    }else {
                        update.execute(cityTree, null, command.getUser());
                        respond.setMsg(update.getMessage());
                    }
                    break;
                default:
                    logger.info("Unknown command");
                    respond.setMsg("Unknown command");
                    break;

            }

            if(command.getUser()!=null) {
                history.add(command.getCommandName(), command.getUser());
            }

            logger.info("Command executed successfully");
            return respond;
    }
}
