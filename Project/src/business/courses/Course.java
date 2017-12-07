package business.courses;

import business.exceptions.ShiftAlredyExistsException;

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
}
