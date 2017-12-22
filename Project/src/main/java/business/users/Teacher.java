package business.users;

import java.util.HashSet;
import java.util.Set;

public class Teacher extends User {
    private Set<String> shifts;
    private Boolean boss;

    public Teacher(String name, Integer number, String email, String password, Boolean isBoss) {
        super(name, number, email, password);
        this.boss = isBoss;
        this.shifts = new HashSet<>();
    }

        public void addShift(String shiftid) {
            this.shifts.add(shiftid);
    }

    public Boolean isBoss() {
        return boss;
    }
}
