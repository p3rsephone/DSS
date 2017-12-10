package business.courses;

import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlredyInShiftException;
import business.exceptions.StudentNotInShiftException;
import business.users.Student;

import java.util.HashSet;

public class Shift {
    private String code;
    private String courseId;
    private Integer numOfStudents;
    private Integer limit;
    private HashSet<Integer> students;
    private Room room;

    public Shift(String code, String courseId, Integer limit, Room room) {
        this.code = code;
        this.courseId = courseId;
        this.numOfStudents = 0;
        this.limit = limit;
        this.room=room;
        this.students = new HashSet<>(this.room.getCapacity());
    }

    public String getCode() {
        return code;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setLimit(Integer limit) throws RoomCapacityExceededException {
        if(this.room.getCapacity() <= limit) {
            this.limit = limit;
        } else {
            throw new RoomCapacityExceededException();
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getNumOfStudents() {
        return numOfStudents;
    }

    public void addStudent(Integer studentNumber) throws StudentAlredyInShiftException, RoomCapacityExceededException {
        if(this.students.contains(studentNumber)) {
            throw new StudentAlredyInShiftException();
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
}
