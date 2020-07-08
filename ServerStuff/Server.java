package ServerStuff;

import CityStructure.City;
import CityStructure.CityTree;
import ClientStuff.Command;
import ClientStuff.Respond;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private Scanner scanner = new Scanner(System.in);
    private int port;
    private DatagramSocket socket;
    private DatagramPacket input;
    private DatagramPacket output;
    private SocketAddress socketAddress;
    private byte[] buffer = new byte[64000];
    static Logger logger = Logger.getLogger("ServerLogger");

    public Server(){
        logger.info("Enter a port number to start a server");
        int port = -1;

        while (port == -1){
            try{
                int a = Integer.valueOf(scanner.nextLine().trim());
                if (a < 0 || a > 65535){
                    logger.info("Wrong port was entered. Port should be a number from 0 to 65535");
                }else{
                    port = a;
                    logger.info("Port is now: "+port);
                    this.port = port;
                    input = new DatagramPacket(buffer, buffer.length);
                    socketAddress = new InetSocketAddress(port);
                    socket = new DatagramSocket(port);
                    logger.info("Server at port " + port + " is working now");
                }
            }catch (NumberFormatException e){
                logger.log(Level.WARNING,"Entered value is not a number");
            }catch (SocketException e){
                logger.log(Level.WARNING, "Entered port is already in use");
                port = -1;
            }
        }
    }

    public CityTree loadCollection(String jsonPath){
        Convertator c = new Convertator();
        return c.toCollection(jsonPath);
    }

    public String getLine(){
        try {
            socket.receive(input);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        byte[] data = input.getData();

        return new String(data, 0, input.getLength());
    }

    public void writeLine(String s){
        output = new DatagramPacket(s.getBytes(), s.getBytes().length, input.getAddress(), input.getPort());
        try {
            socket.send(output);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());        }
    }

    public void writeBytes(byte[] array, SocketAddress socketAddress){
        output = new DatagramPacket(array, array.length, socketAddress);
        try {
            socket.send(output);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public byte[] getBytes(){
        try {
            socket.receive(input);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return input.getData();
    }

    public DatagramPacket getInput(){
        try {
            socket.receive(input);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return input;
    }


    public City getCity(){
        return byteArrayToCity(this.getLine().getBytes());
    }

    public void writeCity(City city){
        this.writeLine(cityToByteArray(city).toString());
    }

    public Command getCommand(){
        return byteArrayToCommand(this.getBytes());
    }

    public void writeRespond(Respond respond, SocketAddress socketAddress){
        this.writeBytes(respondToByteArray(respond), socketAddress);
    }

    private byte[] cityToByteArray(City city){
        return SerializationUtils.serialize(city);
    }

    private byte[] respondToByteArray(Respond respond){
        return SerializationUtils.serialize(respond);
    }

    private City byteArrayToCity(byte[] array){
        return SerializationUtils.deserialize(array);
    }

    private byte[] commandToByteArray(Command command){
        return SerializationUtils.serialize(command);
    }

    public Command byteArrayToCommand(byte[] array){
        return SerializationUtils.deserialize(array);
    }

    private Respond byteArrayToRespond(byte[] array){
        return SerializationUtils.deserialize(array);
    }
}
