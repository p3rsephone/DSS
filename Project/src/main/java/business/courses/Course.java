package business.courses;

import business.utilities.graph.Graph;
import business.exceptions.*;
import business.users.Student;
import business.utilities.Pair;

import java.util.*;

public class Course {
    private String code;
    private String name;
    private Graph shifts;
    private Integer year;
    private String weekday;
    private HashMap<String, Request> billboard;

    public Course(String code, String name, Integer year, String weekday) {
        this.code = code;
        this.name = name;
        this.year = year;
        this.weekday = weekday;
        this.shifts = new Graph();
        this.billboard = new HashMap<>();
    }

    public void addShift(Shift s) throws ShiftAlredyExistsException {
        HashMap<String, Shift> sh = this.shifts.getShifts();
        if(sh.containsKey(s.getCode())) {
            throw new ShiftAlredyExistsException();
        }
        this.shifts.addShift(s);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getWeekday() {
        return weekday;
    }

    public Integer getYear() {
        return year;
    }

    public Shift getShift(String shift) {
        return this.shifts.getShift(shift);
    }

    public void addStudentToShift(String shiftId, Integer studentNumber) {
        try {
            this.shifts.addStudent(shiftId, studentNumber);
        } catch (StudentAlreadyInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
    }

    public Integer removeStudentFromShift(String shiftId, Integer studentNumber) {
        Integer r = 0;
        try {
            r = this.shifts.removeStudent(shiftId, studentNumber);
        } catch (StudentNotInShiftException e) {
            e.printStackTrace();
        }
        return r;
    }

    public void swap(TreeSet<Request> matchingSet) {
        // TODO
    }


    public void requestExchange(Student s, String originShift, String destShift) {
       this.shifts.addRequest(s.getNumber(), originShift, destShift);
    }

    public Set<Pair<Integer,Integer>> getLargestCycle() {
        // TODO
        return new TreeSet<>();
    }

    public HashMap<String, Shift> getShifts() {
        return shifts.getShifts();
    }

    public void makeSwaps(String shiftCode) {
        // TODO
    }

    public void missing(Integer studentNumber, String shiftCode) {
        this.shifts.getShift(shiftCode).foulStudent(studentNumber);
    }
}
