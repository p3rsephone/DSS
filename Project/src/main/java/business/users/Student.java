package business.users;

import business.courses.Request;

import java.util.HashSet;
import java.util.Set;

public class Student extends User{
    private Set<String> shifts;
    private Set<Request> pendingRequests;
    private Boolean statute;
    public Student(String name, String email, String password, Integer number, Boolean statute) {
        super(name, number, email, password);
        this.shifts = new HashSet<>();
        this.pendingRequests = new HashSet<>();
        this.statute = statute;
    }

    public void addShift(String codShift) {
        this.shifts.add(codShift);
    }

    public int numberEnrollments() {
        return this.shifts.size();
    }


    public int getNrequests() {
        return this.pendingRequests.size();
    }

    public Boolean isStatute() {
        return this.statute;
    }
}
