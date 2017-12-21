package business.courses;

import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;

import java.util.HashSet;

public class Shift {
    private String code;
    private String courseId;
    private Integer numOfStudents;
    private Integer limit;
    private HashSet<Integer> students;
    private Integer roomCode;
    
    public Shift(String code, String courseId, Integer limit, Integer roomCode) {
        this.code = code;
        this.courseId = courseId;
        this.roomCode = roomCode;
        this.numOfStudents = 0;
        this.limit = limit;
        this.students = new HashSet<>();
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
        if(this.students.contains(studentNumber)) {
            throw new StudentAlreadyInShiftException();
        } else if (this.numOfStudents + 1 > this.limit){
            throw new RoomCapacityExceededException();
        } else {
            this.students.add(studentNumber);
        }
    }

    public boolean removeStudent(Integer studentNumber) throws StudentNotInShiftException {
        if(!this.students.contains(studentNumber)) {
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
}
