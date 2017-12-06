package business.courses;

import business.users.Student;

import java.util.HashMap;

public class Shift {
    private String code;
    private String courseId;
    private HashMap<Integer, Student> students;
    private Room room;

    public Shift(String code, String courseId, Room room) {
        this.code = code;
        this.courseId = courseId;
        this.room=room;
        this.students = new HashMap<>(this.room.getCapacity());
    }

    public String getCode() {
        return code;
    }

    public String getCourseId() {
        return courseId;
    }
}
