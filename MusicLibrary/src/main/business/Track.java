/*
 * Main.java
 * Created by ruicouto on Nov 27, 2017 (9:47:37 AM).
 */
package main.business;

/**
 *
 * @author ruicouto
 */
public class Track {
    /** Title of the track */
    private String title;
    /** Length of the track */
    private float length;
    /** Database id */
    private int id;

    public Track() {
    }

    public Track(String title, float length) {
        this.title = title;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
