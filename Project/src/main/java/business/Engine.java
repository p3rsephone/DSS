package business;

import business.courses.*;
import business.exceptions.*;
import business.users.DC;
import business.users.Student;
import business.users.Teacher;
import business.users.User;
import data.*;

import java.util.*;

public class Engine {
    private StudentDAO students;
    private TeacherDAO teachers;
    private CourseDAO courses;
    private ShiftDAO shifts;
    private EngineDAO exchanges;
    private Integer nrOfExchanges;
    private Integer phase;
    private RoomDAO rooms;
    private DC dc;
    private int nrOfRequests;

    public Engine() {
        this.exchanges = new EngineDAO();
        if (!exchanges.exists()) exchanges.create();
        this.nrOfExchanges = exchanges.getNExchanges();
        this.nrOfRequests = exchanges.getNRequests();
        this.phase = exchanges.getNPhase();
        this.teachers = new TeacherDAO();
        this.shifts = new ShiftDAO();
        this.students = new StudentDAO();
        this.courses = new CourseDAO();
        this.rooms = new RoomDAO();
        this.dc = new DC();
    }

    public Integer getNrOfExchanges() {
        return nrOfExchanges;
    }

    public int getNrOfRequests() {
        return nrOfRequests;
    }

    public void addUser(User u) throws UserAlredyExistsException {
        if(u instanceof Student) {
            Student s = (Student) u;
            if(this.students.containsKey(s.getNumber())) {
                throw new UserAlredyExistsException();
            }
            this.students.put(u.getNumber(), s);
        } else {
            Teacher t = (Teacher) u;
            if(this.teachers.containsKey(t.getNumber())) {
                throw new UserAlredyExistsException();
            }
            this.teachers.put(t.getNumber(), t);
        }
    } //CHECK

    public void addCourse(Course c) {
        this.courses.put(c.getCode(), c);
    } //CHECK

    public void addRoom(Room r) {
        this.rooms.put(r.getCode(), r);
    } //CHECK

    public void requestExchange(String course, Student s, String originShift, String destShift) throws TooManyRequestsException, ShiftNotValidException, InvalidWeekDayException {
         if (s.getAllNRequests() >= s.getNEnrollments() + 1) {
            throw new TooManyRequestsException();
        } else {
            Course c = this.courses.get(course);
            Request r = c.requestExchange(s, originShift, destShift, this.nrOfRequests++);
            exchanges.putEngine(this); //updates nrORequests
            s.addPendingRequest(r);
            this.makeSwaps(r);
            if (s.isStatute() && s.getRequests(originShift).size() != 0) {
                s.removeShift(originShift);
                Shift shift = c.getShift(destShift);
                s.addShift(shift);
                ArrayList<Integer> reqs = s.getRequests(originShift);
                reqs.remove(r.getCode());
            }
        }
    } //CHECK

    public void cancelExchange(Integer code) throws ExchangeDoesNotExistException, StudentNotInShiftException, ExchangeAlreadyCancelledException {
        if (!this.exchanges.containsKey(code)) {
            throw new ExchangeDoesNotExistException();
        } else {
            Exchange e = this.exchanges.get(code);
            Student orig = this.students.get(e.getOriginStudent());
            Student dest = this.students.get(e.getDestStudent());
            if (e.isCancelled()) {
                throw new ExchangeAlreadyCancelledException();
            } else if (orig.getShifts().contains(e.getDestShift()) && dest.getShifts().contains(e.getOriginShift())) {
                try {
                    Course c = this.courses.get(e.getCourse());
                    Exchange n = c.swap(e.getDestShift(), e.getOriginShift(), e.getOriginStudent(), e.getDestStudent());
                    e.cancelExchange();
                    this.updateShifts(n, orig, dest, c.getCode());
                } catch (RoomCapacityExceededException | StudentAlreadyInShiftException | RequestInvalidException | InvalidWeekDayException | ShiftNotValidException e1) {
                    e1.printStackTrace();
                }
            } else throw new StudentNotInShiftException();
        }
    } //CHECK

    public void defineShiftLimit(String courseId, String shiftId, Integer limit) throws RoomCapacityExceededException, StudentsDoNotFitInShiftException {
        Course course = this.courses.get(courseId);
        Shift shift = null;
        try {
            shift = course.getShift(shiftId);
        } catch (ShiftNotValidException e) {
            e.printStackTrace();
        }
        Room r = this.rooms.get(shift.getRoomCode());
        if(r.getCapacity() >= limit) {
            shift.setLimit(limit);
            shifts.put(shift.getCode(), shift);
        } else {
            throw new RoomCapacityExceededException();
        }
    } //CHECK

    public void enrollStudent(String courseId, Integer studentNumber) {
        this.students.get(studentNumber).addEnrollment(courseId);
    } //CHECK

    public Integer expellStudent(String courseId, String shiftId, Integer studentNumber) {
        Integer absences = -1;
        try {
            absences = this.courses.get(courseId).removeStudentFromShift(shiftId, studentNumber);
            Student s = this.students.get(studentNumber);
            s.removeShift(shiftId);
        } catch (StudentNotInShiftException e) {
            e.printStackTrace();
        }

        return absences;
    } //CHECK

    private void makeSwaps(Request r) throws ShiftNotValidException {
        try {
            String courseCode = r.getCourse();
            Course c = this.courses.get(courseCode);
            Exchange res = c.makeSwaps(r);
            if (res != null) {
                Student origin = this.students.get(res.getOriginStudent());
                Student dest = this.students.get(res.getDestStudent());
                this.updateShifts(res, origin, dest, c.getCode());
                res.setCode(this.nrOfExchanges);
                res.setCourse(courseCode);
                this.exchanges.put(this.nrOfExchanges++, res);
                exchanges.putEngine(this); //update nrOfExchanges
            }

        } catch (StudentNotInShiftException | StudentAlreadyInShiftException | RoomCapacityExceededException | RequestInvalidException | InvalidWeekDayException e) {
            e.printStackTrace();
        }
    } //CHECK

    private void updateShifts(Exchange e, Student origin, Student dest, String courseCode) throws RequestInvalidException, ShiftNotValidException, InvalidWeekDayException {
        Course c = this.courses.get(courseCode);
        Shift o = c.getShift(e.getOriginShift());
        Shift d = c.getShift(e.getDestShift());
        origin.removeShift(e.getOriginShift());
        dest.removeShift(e.getDestShift());
        origin.addShift(d);
        dest.addShift(o);
        origin.removePendingRequest(e.getOriginShift());
        dest.removePendingRequest(e.getDestShift());
    } //CHECK

    public User login(Integer login, String password) {
        if (this.students.containsKey(login)) {
            Student current = this.students.get(login);
            if (current.getPassword().equals(password)) {
                return current;
            }
        }
        if (this.teachers.containsKey(login)) {
            Teacher current = this.teachers.get(login);
            if (current.getPassword().equals(password)) {
                return current;
            }
        }
        if(dc.getNumber().equals(login) && dc.getPassword().equals(password))
                    return dc;

        return null;
    }

    public void registerShift(Course c, Shift s) throws ShiftAlredyExistsException {
        c.addShift(s);
        Teacher t = this.teachers.get(s.getTeacher());
        t.addShift(s.getCode());
    }

    public Integer validateRequest(Integer stNumber, String course, String originShift, String destShift) {
        Student s = this.students.get(stNumber);
        Course c = this.courses.get(course);
        if (s.getNrequests() >= s.getNEnrollments() + 1) {
            return 0;
        } else if (originShift.equals(destShift)) {
            return -1;
        } else if (!c.getShifts().containsKey(originShift) || !c.getShifts().containsKey(destShift)) {
            return -2;
        }

        return 1;
    } //TODO: DELETE, CHECK

    public String getShift(String courseID, Set<String> shiftsID ){
        Course course = courses.get(courseID);
        HashMap shifts = course.getShifts();
        for(String shift : shiftsID){
            if(shifts.containsKey(shift))
                return shift;
        }
        return "";
    } //CHECK

    public void allocateStudents() {
        for (Map.Entry<Integer, Student> s : this.students.entrySet()) {
            Student current = s.getValue();
            for (String courseCode : current.getEnrollments()) {
                try {
                    this.allocateStudent(courseCode, current);
                } catch (InvalidWeekDayException | RoomCapacityExceededException | StudentAlreadyInShiftException e) {
                    e.printStackTrace();
                }
            }
        }
    } //CHECK

    public void allocateStudent(String courseCode, Student s) throws InvalidWeekDayException, RoomCapacityExceededException, StudentAlreadyInShiftException {
        Course c = this.courses.get(courseCode);
        Set<Map.Entry<String, Shift>> es = c.getShifts().entrySet();
        for (Map.Entry<String, Shift> sh : es) {
            Shift shift = sh.getValue();
            if (!shift.isFull() && s.addShift(sh.getValue())) {
                sh.getValue().addStudent(s.getNumber());
                break;
            }
        }
    }

    public void absentStudent(Integer studentNumber, String courseCode, String shiftCode) {
        this.courses.get(courseCode).missing(studentNumber, shiftCode);
    } //TODO:DELETE,  CHECK


    public void changePhase(Integer phase) throws InvalidPhaseException {
        switch (phase) {
            case 1:
                this.phase = 1;
                break;
            case 2:
                this.phase = 2;
                break;
            case 3:
                this.phase = 3;
                break;
            default:
                throw new InvalidPhaseException();
        }
        exchanges.putEngine(this);
    } //CHECK

    public HashMap<Integer, Student> getStudents() {
        return students.list();
    } //CHECK

    public Course getCourse(String c) {
        return this.courses.get(c);
    }//CHECK

    public HashMap<Integer, Exchange> getExchanges() {
        return exchanges.list();
    } //CHECK

    public HashMap<String, Course> getCourses() {
        return courses.list();
    }//CHECK

    public HashMap<String, Room> getRooms() {
        return rooms.list();
    } //CHECK

    public Set<String> getShiftsOfCourse(String code){
        return this.getCourse(code).getShifts().keySet();
    } //CHECK

    public Integer getPhase() {
        return phase;
    } //CHECK

    public Set<Exchange> getAllExchangesOfCourse(String courseCode) {
        Set<Exchange> res = exchanges.getAllExchangesCourse(courseCode);
        return res;
    } //CHECK

    public void cancelRequest(Integer student, Request r) {
        Course c = this.courses.get(r.getCourse());
        c.cancelRequest(r);
        Student s = this.students.get(student);
        s.cancelRequest(r);
    } //CHECK

    public void markAbsent(String courseCode, String shiftCode, ArrayList<Integer> students) {
        try {
            Course c = this.courses.get(courseCode);
            Set<Integer> expellStudents = c.markAbsent(shiftCode, students);

            for (Integer stu : expellStudents) {
                Student s = this.students.get(stu);
                s.removeShift(shiftCode);
            }
        } catch (StudentNotInShiftException e) {
            e.printStackTrace();
        }
    } //CHECK

    public Integer getAbsences(String courseCode, String shiftCode, Integer student) throws StudentNotInShiftException {
        return  this.courses.get(courseCode).getAbsences(shiftCode,student);
    } //CHECK

    public Set<Student> getStudentsOfCourse(String courseCode) {
        Set<Integer> students = new HashSet<>();
        Set<Student> res = new HashSet<>();
        Course c = this.courses.get(courseCode);
        Set<Map.Entry<String, Shift>> shifts = c.getShifts().entrySet();
        for (Map.Entry<String, Shift> me : shifts) {
            Shift s = me.getValue();
            Set<Integer> stud = s.getStudents().keySet();
            students.addAll(stud);
        }
        for (Integer s : students) {
            res.add(this.students.get(s));
        }
        return res;
    } //TODO: DELETE, CHECK

    public Set<Student> getStudentOfShift(String courseCode,String shiftCode) {
        Set<Student> res = new HashSet<>();
        Course c = this.courses.get(courseCode);
        try {
                Shift shift = c.getShift(shiftCode);
                Set<Integer> stud = shift.getStudents().keySet();
                for (Integer s : stud) {
                    res.add(this.students.get(s));
            }
        } catch (ShiftNotValidException e) {
            e.printStackTrace();
        }
        return res;
    } //CHECK

    public String concatRequests(Integer student, String courseCode, String originShift) {
        String res = "";
        Integer counter = 0;
        Student s = this.students.get(student);
        ArrayList<Integer> codes = s.getRequests(originShift);
        Course c = this.courses.get(courseCode);
        if (s != null && codes != null && c !=null) {
            Set<Map.Entry<String, ArrayList<Request>>> req = c.getRequests().entrySet();
            for (Integer code : codes) {
                for (Map.Entry<String, ArrayList<Request>> entry : req) {
                    ArrayList<Request> requests = entry.getValue();
                    for (Request r : requests) {
                        if (r.getCode().equals(code) ) {
                            if (counter > 0) {
                                res = res.concat(", " + r.getDestShift());
                            } else {
                                res = res.concat(r.getDestShift());
                            }
                            counter++;
                        }
                    }
                }
            }
        }
        return res;
    } //CHECK

    public Set<String> getRequests(Integer student, String courseCode, String originShift) {
        Set<String> res = new HashSet<>();
        Integer counter = 0;
        Student s = this.students.get(student);
        ArrayList<Integer> codes = s.getRequests(originShift);
        Course c = this.courses.get(courseCode);
        if (s != null && codes != null && c !=null) {
            Set<Map.Entry<String, ArrayList<Request>>> req = c.getRequests().entrySet();
            for (Integer code : codes) {
                for (Map.Entry<String, ArrayList<Request>> entry : req) {
                    ArrayList<Request> requests = entry.getValue();
                    for (Request r : requests) {
                        if (r.getCode().equals(code) ) {
                           res.add(r.getDestShift());
                        }
                    }
                }
            }
        }
        return res;
    }

    public Set<Student> getEnrolledStudents(String courseCode) {
        Set<Student> res = new HashSet<>();
        Set<Map.Entry<Integer,Student>> entries = this.students.entrySet();
        for (Map.Entry<Integer, Student> entry : entries) {
            Student s = entry.getValue();
            if (s.getEnrollments().contains(courseCode)) {
                res.add(s);
            }
        }
        return res;
    } //TODO:DELETE, CHECK

    public Set<Student> getStudentsWithoutShift(String courseCode) {
        Course c = this.courses.get(courseCode);
        Set<Map.Entry<Integer,Student>> entries = this.students.entrySet();
        HashMap<String, Shift> shifts = c.getShifts();
        Set<Student> res = new HashSet<>();
        Boolean isEnrolled;
        for (Map.Entry<Integer, Student> entry : entries) {
            isEnrolled = false;
            Student s = entry.getValue();
            for (String code : s.getShifts()) {
                if (isEnrolled)  {
                    break;
                } else if (shifts.containsKey(code)) {
                    isEnrolled = true;
                }
            }
            if (!isEnrolled) {
                res.add(s);
            }
        }

        return res;
    } //CHECK

    public void addStudentToShift(String courseCode, String shiftCode, Integer studentNumber) throws StudentAlreadyInShiftException, RoomCapacityExceededException, ShiftNotValidException, InvalidWeekDayException {
        Course c = this.courses.get(courseCode);
        c.addStudentToShift(shiftCode, studentNumber);
        Student s = this.students.get(studentNumber);
        Shift shift = c.getShift(shiftCode);
        s.addShift(shift);
    }

    public Request getRequest(Integer studentNumber, String courseCode, String originShift, String destShift) throws ShiftNotValidException {
        Student s = this.students.get(studentNumber);
        Course c = this.courses.get(courseCode);
        ArrayList<Request> requests = c.getRequests(destShift);
        ArrayList<Integer> codes = s.getRequests(originShift);
        if (codes != null) {
            for (Request r : requests) {
                Integer stu = r.getStudent();
                String ori = r.getOriginShift();
                String dest = r.getDestShift();
                if (stu.equals(studentNumber) && ori.equals(originShift) && dest.equals(destShift)) {
                    return r;
                }
            }
        }
        return null;
    } //CHECK

    public void reset () {
        this.phase = 1;
        this.nrOfExchanges = 0;
        this.nrOfRequests = 0;
        exchanges.reset();
    }

}

