/*
 * Main.java
 * Created by ruicouto on Nov 27, 2017 (9:47:37 AM).
 */
package main.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ruicouto
 */
public class User {
    /** Email of the user */
    private String email;
    /** Password of the user */
    private String password;
    /** List of user albums */
    private List<Album> albums;
    /** Database id */
    private int id;
    

    public User() {
        albums = new ArrayList<>();
    }    
    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
        
}
