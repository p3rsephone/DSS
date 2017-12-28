package business.courses;

import business.exceptions.RoomCapacityExceededException;
import business.exceptions.StudentAlreadyInShiftException;
import business.exceptions.StudentNotInShiftException;
import business.exceptions.StudentsDoNotFitInShiftException;
import data.ShiftDAO;
import data.StudentDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
    private Integer givenClasses;
    private ShiftDAO db;

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
        this.givenClasses = 0;
        this.db = new ShiftDAO();
    }

    public String getCode() {
        return code;
    }

    public String getCourseId() {
        return courseId;
    }

    public Integer getTeacher() {
        return teacher;
    }

    public Integer getNumOfStudents() {
        return numOfStudents;
    }

    public void setNumOfStudents(Integer numOfStudents) {
        this.numOfStudents = numOfStudents;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer newLimit) throws StudentsDoNotFitInShiftException {
        if(newLimit < this.students.size()) {
            throw new StudentsDoNotFitInShiftException();
        }
        this.limit = newLimit;
    }

    public HashMap<Integer, Integer> getStudents() {
        return this.students;
    }

    public Integer getExpectedClasses() {
        return expectedClasses;
    }

    public String getRoomCode() {
        return roomCode;
    }


    public String getWeekday() {
        return weekday;
    }


    public String getPeriod() {
        return period;
    }


    public void addStudent(Integer studentNumber) throws StudentAlreadyInShiftException, RoomCapacityExceededException {
        if(this.students.containsKey(studentNumber)) {
            throw new StudentAlreadyInShiftException();
        } else if (this.numOfStudents + 1 > this.limit){
            throw new RoomCapacityExceededException();
        } else {
            this.numOfStudents++;
            this.students.put(studentNumber,0);
            db.put(getCode(), this);
        }
    }

    public Integer removeStudent(Integer studentNumber) throws StudentNotInShiftException {
        if(!this.students.containsKey(studentNumber)) {
            throw new StudentNotInShiftException();
        } else {
            this.numOfStudents--;
            int ret = this.students.remove(studentNumber);
            new StudentDAO().removeStudentFromShift(studentNumber, this.getCode());
            db.put(getCode(), this);
            return ret;
        }
    }


    public Boolean absentStudent(Integer studentNumber) {
        Boolean res = false;
        Integer absences = this.students.get(studentNumber);
        if(absences+1 >= 0.25*this.expectedClasses) {
            this.students.remove(studentNumber);
            new StudentDAO().removeStudentFromShift(studentNumber, getCode());
            res = true;
        } else {
            this.students.put(studentNumber, absences+1);
        }
        db.put(getCode(),this);
        return res;
    }

    public boolean isFull() {
        return this.numOfStudents >= this.limit;
    }

    public Integer getGivenClasses() {
        return givenClasses;
    }


    public Set<Integer> markAbsent(ArrayList<Integer> missingStudents) throws StudentNotInShiftException {
        Set<Integer> res = new HashSet<>();
        for (Integer s : missingStudents) {
            if (!this.students.containsKey(s)) {
                throw new StudentNotInShiftException();
            } else {
                Boolean expelled = this.absentStudent(s);
                if (expelled) {
                    res.add(s);
                }
            }
        }
        return res;
    }

    public Integer getAbsences(Integer student) throws StudentNotInShiftException {
        if (!this.students.containsKey(student)) {
            throw new StudentNotInShiftException();
        } else {
            return this.students.get(student);
        }
    }
}
