package business;

import business.courses.Course;
import business.courses.Exchange;
import business.users.User;

import java.util.HashMap;

public class Engine {
    private HashMap<Integer, User> users;
    private HashMap<String, Course> courses;
    private HashMap<Integer, Exchange> exchanges;

    public Engine() {
        this.users = new HashMap<>();
        this.courses = new HashMap<>();
        this.exchanges = new HashMap<>();
    }
}
