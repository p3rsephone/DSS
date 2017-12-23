package business.courses;

import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;
import business.users.Student;

import java.util.HashMap;
import java.util.HashSet;

public class Shift {
    private String code;
    private String courseId;
    private Integer teacher;
    private Integer numOfStudents;
    private Integer limit;
    private HashMap<Integer, Integer> students;  //<number, faltas>
    private final Integer expectedClasses;
    private String roomCode;
    private String weekday;
    private String period;

    public Shift(String code, String courseId, Integer limit, Integer teacher, Integer expectedClasses, String roomCode, String weekday, String period) {
        this.code = code;
        this.courseId = courseId;
        this.teacher = teacher;
        this.expectedClasses = expectedClasses;
        this.roomCode = roomCode;
        this.weekday = weekday;
        this.period = period;
        this.numOfStudents = 0;
        this.limit = limit;
        this.students = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public String getCourseId() {
        return courseId;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getNumOfStudents() {
        return numOfStudents;
    }

    public void addStudent(Integer studentNumber) throws StudentAlreadyInShiftException, RoomCapacityExceededException {
        if(this.students.containsKey(studentNumber)) {
            throw new StudentAlreadyInShiftException();
        } else if (this.numOfStudents + 1 > this.limit){
            throw new RoomCapacityExceededException();
        } else {
            this.numOfStudents++;
            this.students.put(studentNumber,0);
        }
    }

    public Integer removeStudent(Integer studentNumber) throws StudentNotInShiftException {
        if(!this.students.containsKey(studentNumber)) {
            throw new StudentNotInShiftException();
        } else {
            this.numOfStudents--;
            return this.students.remove(studentNumber);
        }
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setRoom(String roomCode) {
        this.roomCode = roomCode;
    }

    public void foulStudent(Integer studentNumber) {
        Integer fouls = this.students.get(studentNumber);
        if(fouls+1 >= 0.25*this.expectedClasses) {
            this.students.remove(studentNumber);
        } else {
            this.students.put(studentNumber, fouls+1);
        }
    }

    public Integer getTeacher() {
        return teacher;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getPeriod() {
        return period;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public HashMap<Integer, Integer> getStudents() {
        return this.students;
    }
}
