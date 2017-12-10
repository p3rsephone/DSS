package business.users;

import java.util.HashSet;

public class TeacherBoss extends Teacher{
    private HashSet<String> courses;


    public TeacherBoss(String name, Integer number, String email, String password) {
        super(name, number, email, password);
        this.courses = new HashSet<>();
    }
}
