/*
 * Main.java
 * Created by ruicouto on Nov 27, 2017 (9:47:37 AM).
 */
package main.business;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author ruicouto
 */
public class MusicLibrary {
    /** Library users */
    private Map<String, User> users;
    /** Library albums */
    private Map<String, Album> albums;

    public MusicLibrary() {
        users = new HashMap<>();
        albums = new HashMap<>();
    }
    
    public void addAlbum(String name, Album album) {
        albums.put(name, album);
    }
    
    public void addUser(String email, User user) {
        users.put(email, user);
    }

    public Map<String, Album> getAlbums() {
        return albums;
    }

    public Map<String, User> getUsers() {
        return users;
    }
    
    public User getUser(String email) {
        return users.get(email);
    }
    
    public Album getAlbum(String name) {
        return albums.get(name);
    }
    
    
}
