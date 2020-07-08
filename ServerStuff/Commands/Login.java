package ServerStuff.Commands;

import DatabaseStuff.DatabaseManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Login{
    private String msg;
    private MessageDigest digest;
    static Logger logger = Logger.getLogger("Login");

    public Login(){
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.severe("No such algorithm");
            System.exit(0);
        }
    }

    public void execute(String[] params, DatabaseManager databaseManager){
        if ((params != null)&&(params.length == 2)) {
            String login = params[0];
            String password = bytesToHex(digest.digest(("4k%0UU[" + params[1] + "]iF89hJd").getBytes()));
            if (databaseManager.suchUserExists(login)) {
                if (databaseManager.passwordIsCorrect(login, password)) {
                    logger.info(login + " logged in successfully");
                    msg = "Logged in successfully";
                } else {
                    logger.info("Password is incorrect");
                    msg = "Password is incorrect";
                }
            } else {
                logger.info("Such user doesn't exist");
                msg = "Such user doesn't exist";
            }
        }else {
            logger.info("Login/password is null");
            msg = "Login/password is null";
        }
    }

    public String getMessage() {
        return msg;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
