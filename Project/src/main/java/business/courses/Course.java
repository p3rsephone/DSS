package business.courses;

import business.exceptions.*;
import business.users.Student;
import data.CourseDAO;
import data.RequestDAO;
import data.ShiftDAO;

import java.util.*;

public class Course {

    private String code; 
    private String name;
    private Integer regTeacher;
    private Integer year;
    private HashMap<String, Shift> shifts;
    private HashMap<String, ArrayList<Request>> billboard;
    private CourseDAO db;

    public Course(String code, String name, Integer regTeacher, Integer year) {
        this.code = code;
        this.name = name;
        this.regTeacher = regTeacher;
        this.year = year;
        this.shifts = new HashMap<>();
        this.billboard = new HashMap<>();
        this.db = new CourseDAO();
    }

    public void addShift(Shift s) throws ShiftAlredyExistsException {
        if(this.shifts.containsKey(s.getCode())) {
            throw new ShiftAlredyExistsException();
        }
        this.shifts.put(s.getCode(), s);
        db.put(getCode(),this);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public Shift getShift(String shift) throws ShiftNotValidException {
        if (!this.shifts.containsKey(shift)) {
            throw new ShiftNotValidException();
        } else return this.shifts.get(shift);
    }

    public void setBillboard(HashMap<String, ArrayList<Request>> billboard) {
        this.billboard = billboard;
    }

    public void setShifts(HashMap<String, Shift> shifts) {
        this.shifts = shifts;
    }

    public void addStudentToShift(String shiftId, Integer studentNumber) throws RoomCapacityExceededException, StudentAlreadyInShiftException {
        Shift s = this.shifts.get(shiftId);
        s.addStudent(studentNumber);
    }

    public Integer removeStudentFromShift(String shiftId, Integer studentNumber) throws StudentNotInShiftException {
        Integer r = 0;
        Shift s = this.shifts.get(shiftId);
        r = s.removeStudent(studentNumber);
        return r;
    }

    public Request requestExchange(Student s, String originShift, String destShift, Integer code) {
        Request r = new Request(code, s.getNumber(), this.code, originShift, destShift);
        this.addRequest(r, destShift);
        return r;
    }

    private void addRequest(Request r, String destShift) {
        if (this.billboard.containsKey(destShift)) {
            ArrayList<Request> destRequests = this.billboard.get(destShift);
            destRequests.add(r);
        } else {
            ArrayList<Request> ar = new ArrayList<>();
            ar.add(r);
            this.billboard.put(destShift,ar);
        }
        db.put(getCode(),this);
    }

    public HashMap<String, Shift> getShifts() {
        return shifts;
    }

    public Exchange makeSwaps(Request r) throws StudentNotInShiftException, StudentAlreadyInShiftException, RoomCapacityExceededException {
        Exchange res = null;
        Request dR = null;
        String destShift = r.getDestShift();
        String originShift = r.getOriginShift();
        ArrayList<Request> requests = this.billboard.get(originShift);
        if(requests != null && !requests.isEmpty()) {
            for (Request destRequest : requests) {
                if(destRequest.getDestShift().equals(originShift) && destRequest.getOriginShift().equals((destShift))) {
                    res = this.swap(originShift, destShift, r.getStudent(), destRequest.getStudent());
                    dR=destRequest;
                    break;
                }
            }
            if (dR!=null) {
                this.findAndRemove(dR);
                this.findAndRemove(r);
            }
        }
        return res;
    }

    private void findAndRemove(Request r)  {
        ArrayList<Request> requests = this.billboard.get(r.getDestShift());
        ArrayList<Request> newR = new ArrayList<>();
        for (Request req : requests) {
            if (!(req.getOriginShift().equals(r.getOriginShift()) && req.getCourse().equals(req.getCourse()))) {
                newR.add(req);
            }
        }
        this.billboard.put(r.getDestShift(), newR);
        new RequestDAO().removeRequest(r.getCode());
        db.put(getCode(),this);
    }

    public Exchange swap(String originShift, String destShift, Integer origStudent, Integer destStudent) throws StudentNotInShiftException, RoomCapacityExceededException, StudentAlreadyInShiftException {
        Shift origin = this.shifts.get(originShift);
        Shift dest = this.shifts.get(destShift);
        origin.removeStudent(origStudent);
        dest.removeStudent(destStudent);
        origin.addStudent(destStudent);
        dest.addStudent(origStudent);
        return new Exchange(originShift, destShift, origStudent, destStudent);
    }

    public void missing(Integer studentNumber, String shiftCode) {
        this.shifts.get(shiftCode).absentStudent(studentNumber);
    }

    public Integer getRegTeacher() {
        return regTeacher;
    }

    public HashMap<String, ArrayList<Request>> getBillboard() {
        return billboard;
    }

    public HashMap<String, ArrayList<Request>> getRequests() {
        return this.billboard;
    }

    public ArrayList<Request> getRequests(String destShift) {
        return this.billboard.get(destShift);
    }

    public void cancelRequest(Request r) {
        ArrayList<Request> bill = this.billboard.get(r.getDestShift());
        bill.remove(r);
        new RequestDAO().removeRequest(r.getCode());
        db.put(getCode(),this);
    }

    public Set<Integer> markAbsent(String shiftCode, ArrayList<Integer> students) throws StudentNotInShiftException {
        return this.shifts.get(shiftCode).markAbsent(students);
    }

    public Integer getAbsences(String shiftCode, Integer student) throws StudentNotInShiftException {
        return this.shifts.get(shiftCode).getAbsences(student);
    }
}
