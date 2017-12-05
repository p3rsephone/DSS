package business.courses;

import java.util.HashSet;

public class Course {
    private String code;
    private HashSet<Shift> shifts;

    public Course(String code, HashSet<Shift> shifts) {
        this.code = code;
        this.shifts = shifts;
    }

    public void addShift(Shift s) {
        this.shifts.add(s);
    }

    public String getCode() {
        return code;
    }
}
