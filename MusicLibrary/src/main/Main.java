/*
 * Main.java
 * Created by ruicouto on Nov 27, 2017 (9:47:37 AM).
 */
package main;

import main.business.Connect;
import main.business.MusicLibrary;
import main.business.User;
import main.business.UserDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ruicouto
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connect connect = new Connect();
        Connection cn =   connect.connect();
        User user = new User("ola@gmail.com","password");
        connect.close(cn);
        UserDao users = new UserDao();
       users.put(user.getEmail(),user);
        MusicLibrary musicLibrary = new MusicLibrary();
        //TODO: Create the views and show the music library
        //para fazer select values do UserDao
        ArrayList<User> lista = (ArrayList<User>) users.values();

        for(User u : lista){
            System.out.println(u.getEmail() + " " + u.getPassword());
        }
    }
    
}
