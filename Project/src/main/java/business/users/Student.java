package business.users;

import business.courses.Request;
import business.courses.Shift;
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

    public Boolean addShift(Shift s) throws InvalidWeekDayException {
        if (!this.schedule.isOccuppied(s.getWeekday(), s.getPeriod())) {
            this.schedule.usePeriod(s.getCode(), s.getWeekday(), s.getPeriod());
            this.shifts.add(s.getCode());
            return true;
        } else return false;

    }

    public Boolean addShift(String shift) {
        this.shifts.add(shift);
        return true;
    }

    public void addEnrollment(String codCourse) {
        this.enrollments.add(codCourse);
    }

    public void addPendingRequest(Request rq) {
        this.pendingRequests.add(rq);
    }

    public void removePendingRequest(Request rq) {
        Set<Request> res = new HashSet<>();
        for (Request r : this.pendingRequests) {
            if (r.getOriginShift().equals(rq.getOriginShift()) && r.getCourse().equals(rq.getCourse())) {
                res.add(r);
            }
        }
        this.pendingRequests.removeAll(res);
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

    public void removeShift(String destShift) {
        this.shifts.remove(destShift);
        this.schedule.freePeriod(destShift);
    }

    public void findAndRemove(String course, String originShift, String destShift) {
        Set<Request> res = new HashSet<>();
        for (Request r : this.pendingRequests) {
            if (r.getOriginShift().equals(originShift) && r.getCourse().equals(course)) {
                res.add(r);
            }
        }
        this.pendingRequests.removeAll(res);
    }

    public String requestCourse(String courseID){
       String ret = "";

       for (Request r : pendingRequests){
          if(r.getCourse().equals(courseID))
              ret = ret.concat(r.getDestShift() + ", ");
       }

       return ret;

    }
}
