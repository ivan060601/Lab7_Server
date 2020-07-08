package ClientStuff;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
    final static long serialVersionUID = 1L;
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}