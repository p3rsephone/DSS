package business.users;

import business.courses.Request;
import business.exceptions.InvalidWeekDayException;
import business.utilities.Schedule;

import java.util.HashSet;
import java.util.Set;

public class Student extends User{
    private Set<String> shifts;
    private Set<String> enrollments;
    private Set<Request> pendingRequests;
    private Boolean statute;
    private Schedule schedule;

    public Student(String name, String email, String password, Integer number, Boolean statute) {
        super(name, number, email, password);
        this.schedule = new Schedule();
        this.shifts = new HashSet<>();
        this.enrollments = new HashSet<>();
        this.pendingRequests = new HashSet<>();
        this.statute = statute;
    }

    public void addShift(String codShift) {
        this.shifts.add(codShift);
    }

    public void addEnrollment(String codCourse) {
        this.enrollments.add(codCourse);
    }

    public void addPendingRequests(Request rq) {
        this.pendingRequests.add(rq);
    }

    public Set<String> getShifts() {
        Set<String> ret = new HashSet<>();
        ret.addAll(shifts);
        return ret;
    }

    public Set<String> getEnrollments() {
        Set<String> ret = new HashSet<>();
        ret.addAll(enrollments);
        return ret;
    }

    public Set<Request> getRequests() {
        Set<Request> ret = new HashSet<>();
        ret.addAll(pendingRequests);
        return ret;
    }

    public int getNshifts() {
        return this.shifts.size();
    }


    public int getNrequests() {
        return this.pendingRequests.size();
    }

    public Boolean isStatute() {
        return this.statute;
    }

    public Integer getNEnrollments() {
        return this.enrollments.size();
    }

    public Boolean isOcuppied(String weekday, String period) {
        Boolean res = true;
        try {
            res = this.schedule.isOcuppied(weekday, period);
        } catch (InvalidWeekDayException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void removeShift(String destShift) {
        this.shifts.remove(destShift);
    }
}
