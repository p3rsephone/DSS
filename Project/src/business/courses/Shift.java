package business.courses;

import business.users.Student;

import java.util.HashMap;

public class Shift {
    private String code;
    private HashMap<Integer, Student> students;
    private Room room;

    public Shift(String code, Room room) {
        this.code = code;
        this.room=room;
        this.students = new HashMap<>(this.room.getCapacity());
    }

    public String getCode() {
        return code;
    }
}
