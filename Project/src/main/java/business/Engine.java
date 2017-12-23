package business;

import business.courses.*;
import business.exceptions.*;
import business.users.Student;
import business.users.Teacher;
import business.users.User;

import java.util.HashMap;
import java.util.Set;

public class Engine {
    private HashMap<Integer, Student> students;
    private HashMap<Integer, Teacher> teachers;
    private HashMap<String, Course> courses;
    private HashMap<Integer, Exchange> exchanges;
    private Integer nrOfExchanges;
    private Integer phase;
    private HashMap<String, Room> rooms;

    public Engine() {
        this.nrOfExchanges = 0;
        this.phase = 1;
        this.teachers = new HashMap<>();
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.exchanges = new HashMap<>();
        this.rooms = new HashMap<>();
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
        if(s.getNrequests() >= s.getNEnrollments()) {
            throw new TooManyRequestsException();
        } else {
            Course c = this.courses.get(course);
            Request r = c.requestExchange(s, originShift, destShift);
            this.makeSwaps(r);
        }
    }

    public void registerExchange(Student s1, String courseCode, String shcode1, Request request) {
        Integer s2number = request.getStudent();
        String shcode2 = request.getOriginShift();
        Exchange e = new Exchange(this.nrOfExchanges++, courseCode, shcode1, shcode2, s1.getNumber(), s2number);
        this.exchanges.put(e.getCode(), e);
    }

    public void cancelExchange(Integer code) throws ExchangeDoesNotExistException {
        if (this.exchanges.containsKey(code)) {
            throw new ExchangeDoesNotExistException();
        } else {
            Exchange e = this.exchanges.get(code);
            Student orig = this.students.get(e.getOriginStudent());
            Student dest = this.students.get(e.getDestStudent());
            if (orig.getShifts().contains(e.getDestShift()) && dest.getShifts().contains(e.getOriginShift())) {
                try {
                    this.courses.get(e.getCourse()).swap(e.getOriginShift(), e.getDestShift(), e.getDestStudent(), e.getOriginStudent());
                } catch (StudentNotInShiftException | RoomCapacityExceededException | StudentAlreadyInShiftException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void defineShiftLimit(String courseId, String shiftId, Integer limit) throws RoomCapacityExceededException {
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

    public void enrollStudent(String courseId, String shiftId, Integer studentNumber) {
        try {
            this.courses.get(courseId).addStudentToShift(shiftId, studentNumber);
            this.students.get(studentNumber).addEnrollment(courseId);
        } catch (StudentAlreadyInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
        this.students.get(studentNumber).addShift(shiftId);
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
            String shiftCode  = r.getDestShift();
            Course c = this.courses.get(courseCode);
            Exchange res = c.makeSwaps(r);
            if (res != null) {
                Student origin = this.students.get(res.getOriginStudent());
                Student dest = this.students.get(res.getDestStudent());
                origin.removeShift(res.getOriginShift());
                dest.removeShift(res.getDestShift());
                origin.addShift(res.getDestShift());
                dest.addShift(res.getOriginShift());
                res.setCourse(courseCode);
                res.setCode(this.nrOfExchanges++);
                this.exchanges.put(this.nrOfExchanges, res);
            }

        } catch (StudentNotInShiftException | StudentAlreadyInShiftException | RoomCapacityExceededException e) {
            e.printStackTrace();
        }
    }

    public User login(Integer login, String password) {
        if (this.students.containsKey(login)) {
            Student current = this.students.get(login);
            if (current.getPassword().equals(password)) {
                return current;
            }
        } else if (this.teachers.containsKey(login)) {
            Teacher current = this.teachers.get(login);
            if (current.getPassword().equals(password)) {
                return current;
            }
        }

        return null;
    }

    public void allocateStudents() {
        // TODO
    }

    public void foulStudent(Integer studentNumber, String courseCode, String shiftCode) {
        this.courses.get(courseCode).missing(studentNumber, shiftCode);
    }

    public void changePhase(Integer phase) throws InvalidPhaseException {
        switch (phase) {
            case 1: this.phase = 1;
            case 2: this.phase = 2;
            case 3: this.phase = 3;
            default: throw new InvalidPhaseException();
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
}
