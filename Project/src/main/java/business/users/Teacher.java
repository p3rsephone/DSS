package business.users;

import java.util.HashSet;
import java.util.Set;

public class Teacher extends User {
    private Set<String> shifts;
    private Boolean boss;
    private String course;

    public Teacher(String name, Integer number, String email, String password, Boolean isBoss, String course) {
        super(name, number, email, password);
        this.boss = isBoss;
        this.course = course;
        this.shifts = new HashSet<>();
    }


    public Set<String> getShifts() {
        Set<String> ret = new HashSet<>();
        ret.addAll(shifts);
        return ret;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void addShift(String shiftid) {
        this.shifts.add(shiftid);
    }

    public void setShifts(Set<String> shifts) {
        this.shifts = shifts;
    }

    public Boolean isBoss() {
        return boss;
    }
}
