package business.users;

import business.courses.Request;
import business.courses.Shift;
import business.exceptions.InvalidWeekDayException;
import business.exceptions.RequestInvalidException;
import business.utilities.Schedule;

import java.lang.reflect.Array;
import java.util.*;

public class Student extends User{
    private Set<String> shifts;
    private Set<String> enrollments;
    private HashMap<String, ArrayList<Integer>> pendingRequests;
    private Boolean statute;
    private Schedule schedule;

    public Student(String name, String email, String password, Integer number, Boolean statute) {
        super(name, number, email, password);
        this.schedule = new Schedule();
        this.shifts = new HashSet<>();
        this.enrollments = new HashSet<>();
        this.pendingRequests = new HashMap<>();
        this.statute = statute;
    }

    public Boolean addShift(Shift s) throws InvalidWeekDayException {
        if (!this.schedule.isOccuppied(s.getWeekday(), s.getPeriod())) {
            this.schedule.usePeriod(s.getCode(), s.getWeekday(), s.getPeriod());
            this.shifts.add(s.getCode());
            // StudentDAO dbStudent = new StudentDAO();
            // dbStudent.putStudentShift(studentNumber); UPDATE
            return true;
        } else return false;

    }

    public void setShifts(Set<String> shifts) {
        this.shifts = shifts;
    }

    public void addEnrollment(String codCourse) {
        this.enrollments.add(codCourse);
    }

    public void setEnrollments(Set<String> enrollments) {
        this.enrollments = enrollments;
    }

    public void addPendingRequest(Request rq) {
        if(!this.pendingRequests.containsKey(rq.getOriginShift())) {
            ArrayList<Integer> reqs = new ArrayList<>();
            reqs.add(rq.getCode());
            this.pendingRequests.put(rq.getOriginShift(), reqs);
        } else {
            ArrayList<Integer> reqs = this.pendingRequests.get(rq.getOriginShift());
            reqs.add(rq.getCode());
        }
    }

    public void setPendingRequests(HashMap<String, ArrayList<Integer>> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public void removePendingRequest(String originShift) throws RequestInvalidException {
        if( this.pendingRequests.containsKey(originShift)) {
            ArrayList<Integer> req = this.pendingRequests.get(originShift);
            req.clear();
        }
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

    public ArrayList<Integer> getRequests(String originShift) {
        return this.pendingRequests.get(originShift);
    }

    public HashMap<String, ArrayList<Integer>> getPendingRequests() {
        HashMap<String, ArrayList<Integer>> ret = new HashMap<>();
        ret.putAll(this.pendingRequests);
        return ret;
    }

    public int getNshifts() {
        return this.shifts.size();
    }


    public int getNrequests() {
        return this.pendingRequests.size();
    }

    public int getAllNRequests() {
        int sum=0;
        for (ArrayList a : pendingRequests.values()) {
            sum += a.size();
        }
        return sum;
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


    public ArrayList<Integer> requestCourse(String originShift){
       ArrayList<Integer> res = this.pendingRequests.get(originShift);
       return res;

    }

    public void cancelRequest(Request r) {
        this.pendingRequests.remove(r);
    }

    @Override
    public String toString() {
        return  super.toString()+
               "statute=" + statute ;

    }
}
