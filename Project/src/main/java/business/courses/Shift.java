package business.courses;

import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;

import java.util.HashMap;

public class Shift {
    private String code;
    private String courseId;
    private Integer teacher;
    private Integer numOfStudents;
    private Integer limit;
    private HashMap<Integer, Integer> students;  //<number, faltas>
    private final Integer expectedClasses;
    private Integer roomCode;
    
    public Shift(String code, String courseId, Integer limit, Integer teacher, Integer expectedClasses, Integer roomCode) {
        this.code = code;
        this.courseId = courseId;
        this.teacher = teacher;
        this.expectedClasses = expectedClasses;
        this.roomCode = roomCode;
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
            this.students.put(studentNumber,0);
        }
    }

    public Integer removeStudent(Integer studentNumber) throws StudentNotInShiftException {
        if(!this.students.containsKey(studentNumber)) {
            throw new StudentNotInShiftException();
        } else {
            return this.students.remove(studentNumber);
        }
    }

    public Integer getRoomCode() {
        return roomCode;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setRoom(Integer roomCode) {
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
}
