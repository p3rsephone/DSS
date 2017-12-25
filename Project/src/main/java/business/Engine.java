package business;

import business.courses.*;
import business.exceptions.*;
import business.users.DC;
import business.users.Student;
import business.users.Teacher;
import business.users.User;

import java.lang.reflect.Array;
import java.util.*;

public class Engine {
    private HashMap<Integer, Student> students;
    private HashMap<Integer, Teacher> teachers;
    private HashMap<String, Course> courses;
    private HashMap<Integer, Exchange> exchanges;
    private Integer nrOfExchanges;
    private Integer phase;
    private HashMap<String, Room> rooms;
    private DC dc;

    public Engine() {
        this.nrOfExchanges = 0;
        this.phase = 1;
        this.teachers = new HashMap<>();
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.exchanges = new HashMap<>();
        this.rooms = new HashMap<>();
        this.dc = new DC();
    }

    public void addUser(User u) throws UserAlredyExistsException {
        if(u instanceof Student ) {
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
    }

    public void addCourse(Course c) {
        this.courses.put(c.getCode(), c);
    }

    public void addRoom(Room r) {
        this.rooms.put(r.getCode(), r);
    }

    public void requestExchange(String course, Student s, String originShift, String destShift) throws TooManyRequestsException {
        if (s.getNrequests() >= s.getNEnrollments() + 1) {
            throw new TooManyRequestsException();
        } else {
            Course c = this.courses.get(course);
            Request r = c.requestExchange(s, originShift, destShift);
            s.addPendingRequest(r);
            this.makeSwaps(r);
        }
    }

    public void cancelExchange(Integer code) throws ExchangeDoesNotExistException, StudentNotInShiftException, ExchangeAlreadyCancelledException {
        if (!this.exchanges.containsKey(code)) {
            throw new ExchangeDoesNotExistException();
        } else {
            //this.exchanges.forEach((k,v)-> System.out.println("a chave e " + k+ ""));
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
                } catch (RoomCapacityExceededException | StudentAlreadyInShiftException e1) {
                    e1.printStackTrace();
                }
            } else throw new StudentNotInShiftException();
        }
    }

    public void defineShiftLimit(String courseId, String shiftId, Integer limit) throws RoomCapacityExceededException, StudentsDoNotFitInShiftException {
        Course course = this.courses.get(courseId);
        Shift shift = null;
        try {
            shift = course.getShift(shiftId);
        } catch (ShiftNotValidException e) {
            e.printStackTrace();
        }
        Room r = this.rooms.get(shift.getRoomCode());
        if(r.getCapacity() <= limit) {
            shift.setLimit(limit);
        } else {
            throw new RoomCapacityExceededException();
        }
    }

    public void enrollStudent(String courseId, Integer studentNumber) {
        this.students.get(studentNumber).addEnrollment(courseId);
    }

    public Integer expellStudent(String courseId, String shiftId, Integer studentNumber) {
        Integer fouls = -1;
        try {
            fouls = this.courses.get(courseId).removeStudentFromShift(shiftId, studentNumber);
        } catch (StudentNotInShiftException e) {
            e.printStackTrace();
        }

        return fouls;
    }

    public void makeSwaps(Request r) {
        try {
            String courseCode = r.getCourse();
            Course c = this.courses.get(courseCode);
            Exchange res = c.makeSwaps(r);
            if (res != null) {
                Student origin = this.students.get(res.getOriginStudent());
                Student dest = this.students.get(res.getDestStudent());
                this.updateShifts(res, origin, dest, c.getCode());
                this.exchanges.put(this.nrOfExchanges++, res);
            }

        } catch (StudentNotInShiftException | StudentAlreadyInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
    }

    public void updateShifts(Exchange e, Student origin, Student dest, String courseCode) {
        origin.removeShift(e.getOriginShift());
        dest.removeShift(e.getDestShift());
        origin.addShift(e.getDestShift());
        dest.addShift(e.getOriginShift());
        e.setCourse(courseCode);
        e.setCode(this.nrOfExchanges);
        origin.removePendingRequest(e.getOriginShift(), e.getCourse());
        dest.findAndRemove(e.getCourse(), e.getDestShift());
    }

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
    }
    public String getShift(String courseID, Set<String> shiftsID ){
        Course course = courses.get(courseID);
        HashMap shifts = course.getShifts();
        for(String shift : shiftsID){
            if(shifts.containsKey(shift))
                return shift;
        }
        return "";
    }

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
    }

    public Boolean allocateStudent(String courseCode, Student s) throws InvalidWeekDayException, RoomCapacityExceededException, StudentAlreadyInShiftException {
        Course c = this.courses.get(courseCode);
        Set<Map.Entry<String, Shift>> es = c.getShifts().entrySet();
        Boolean res = false;
        for (Map.Entry<String, Shift> sh : es) {
            Shift shift = sh.getValue();
            if (!shift.isFull() && s.addShift(sh.getValue())) {
                sh.getValue().addStudent(s.getNumber());
                res = true;
                break;
            }
        }
        return res;
    }

    public void foulStudent(Integer studentNumber, String courseCode, String shiftCode) {
        this.courses.get(courseCode).missing(studentNumber, shiftCode);
    }


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
    }

    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    public Course getCourse(String c) {
        return this.courses.get(c);
    }

    public HashMap<Integer, Exchange> getExchanges() {
        return exchanges;
    }

    public HashMap<String, Course> getCourses() {
        return courses;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public Set<String> getShiftsOfCourse(String code){
        return this.getCourse(code).getShifts().keySet();
    }

    public Integer getPhase() {
        return phase;
    }

    public Set<Exchange> getAllExchangesOfCourse(String courseCode) {
        Set<Exchange> res = new HashSet<>();
        Set<Map.Entry<Integer, Exchange>> exchanges = this.exchanges.entrySet();
        for (Map.Entry<Integer, Exchange> e : exchanges) {
            Exchange ex = e.getValue();
            if(ex.getCourse().equals(courseCode)) res.add(ex);
        }
        return res;
    }

    public void cancelRequest(String student, Request r) {
        Course c = this.courses.get(r.getCourse());
        c.cancelRequest(r);
        Student s = this.students.get(student);
        s.cancelRequest(r);
    }

    public void markAbsent(String courseCode, String shiftCode, ArrayList<Integer> students) {
        try {
            this.courses.get(courseCode).markAbsent(shiftCode, students);
        } catch (StudentNotInShiftException e) {
            e.printStackTrace();
        }
    }

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
    }

    public Set<Student> getStudentOfShift(String courseCode, String shiftCode) {
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
    }
}
