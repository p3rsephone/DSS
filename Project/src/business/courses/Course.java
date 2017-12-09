package business.courses;

import business.exceptions.RoomCapacityExceededException;
import business.exceptions.ShiftAlredyExistsException;
import business.exceptions.StudentAlredyInShiftException;
import business.exceptions.StudentNotInShiftException;

import java.util.HashMap;

public class Course {
    private String code;
    private String name;
    private HashMap<String, Shift> shifts;
    private Year year;
    private String weekday;

    public Course(String code, String name, Year year, String weekday) {
        this.code = code;
        this.name = name;
        this.year = year;
        this.weekday = weekday;
        this.shifts = new HashMap<>();
    }

    public void addShift(Shift s) throws ShiftAlredyExistsException {
        if(this.shifts.containsKey(s.getCode())) {
            throw new ShiftAlredyExistsException();
        }
        this.shifts.put(s.getCode(), s);
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

    public Year getYear() {
        return year;
    }

    public Shift getShift(String shift) {
        return this.shifts.get(shift);
    }

    public void setShiftLimit(String shiftId, Integer limit) {
        try {
            this.shifts.get(shiftId).setLimit(limit);
        } catch (RoomCapacityExceededException e) {
            e.printStackTrace();
        }
    }

    public void addStudentToShift(String shiftId, Integer studentNumber) {
        try {
            this.shifts.get(shiftId).addStudent(studentNumber);
        } catch (StudentAlredyInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
    }

    public boolean removeStudentFromShift(String shiftId, Integer studentNumber) {
        boolean r = false;
        try {
            r = this.shifts.get(shiftId).removeStudent(studentNumber);
        } catch (StudentNotInShiftException e) {
            e.printStackTrace();
        }
        return r;
    }

    public void swap(Integer s1number, String s1currShift, Integer s2number, String s2currShift) {
        Shift shift1 = this.shifts.get(s1currShift);
        Shift shift2 = this.shifts.get(s2currShift);
        try {
            shift1.removeStudent(s1number);
            shift1.addStudent(s2number);
            shift2.removeStudent(s2number);
            shift2.addStudent(s1number);
        } catch (StudentNotInShiftException | StudentAlredyInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
    }
}
