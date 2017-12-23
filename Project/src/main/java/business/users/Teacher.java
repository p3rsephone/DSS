package business.users;

import java.util.HashSet;
import java.util.Set;

public class Teacher extends User {
    private Set<String> shifts;
    private Boolean boss;
    private String ownedCourse;

    public Teacher(String name, Integer number, String email, String password, Boolean isBoss) {
        super(name, number, email, password);
        this.boss = isBoss;
        this.ownedCourse = null;
        this.shifts = new HashSet<>();
    }


    public Set<String> getShifts() {
        Set<String> ret = new HashSet<>();
        ret.addAll(shifts);
        return ret;
    }

    public String getOwnedCourse() {
        return ownedCourse;
    }

    public void addShift(String shiftid) {
        this.shifts.add(shiftid);
    }

    public void setOwnedCourse(String ownedCourse) {
        this.ownedCourse = ownedCourse;
    }

    public Boolean isBoss() {
        return boss;
    }
}
