package business.users;

import java.util.Set;

public class Teacher extends User {
    private Set<String> shifts;

    public Teacher(String name, Integer number, String email, String password) {
        super(name, number, email, password);
    }
}