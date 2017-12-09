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
public class Album {
    /** Album name */
    private String name;
    /** Artist name */
    private String artist;
    /** List of album tracks */
    private List<Track> tracks;
    /** Database id */
    private int id;

    public Album() {
        tracks = new ArrayList<>();
    }

    public Album(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<Track> getTracks() {
        return tracks;
    }
    
    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
