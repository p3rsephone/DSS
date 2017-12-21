package business;

import business.courses.*;
import business.exceptions.RoomCapacityExceededException;
import business.exceptions.UserAlredyExistsException;
import business.users.Student;
import business.users.Teacher;
import business.users.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Engine {
    private HashMap<Integer, Student> students;
    private HashMap<Integer, Teacher> teachers;
    private HashMap<String, Course> courses;
    private HashMap<Integer, Exchange> exchanges;
    private Integer nrOfExchanges;
    private HashMap<Integer, Room> rooms;

    public Engine() {
        this.nrOfExchanges = 0;
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

    public void requestExchange(String course, Student s, String originShift, String destShift) {
        this.courses.get(course).requestExchange(s, originShift, destShift);
    }

    public void registerExchange(Student s1, String courseCode, String shcode1, Request request) {
        Integer s2number = request.getStudent();
        String shcode2 = request.getOriginShift();
        Exchange e = new Exchange(this.nrOfExchanges++, courseCode, shcode1, shcode2, s1.getNumber(), s2number);
        this.exchanges.put(e.getCode(), e);
    }

    public void cancelExchange(Integer code) {
        // TODO
    }

    public void defineShiftLimit(String courseId, String shiftId, Integer limit) throws RoomCapacityExceededException {
        Course course = this.courses.get(courseId);
        Shift shift = course.getShift(shiftId);
        Room r = this.rooms.get(shift.getRoomCode());
        if(r.getCapacity() <= limit) {
            shift.setLimit(limit);
        } else {
            throw new RoomCapacityExceededException();
        }
    }

    public void enrollStudent(String courseId, String shiftId, Integer studentNumber) {
        this.courses.get(courseId).addStudentToShift(shiftId, studentNumber);
    }

    public boolean expellStudent(String courseId, String shiftId, Integer studentNumber) {
        return this.courses.get(courseId).removeStudentFromShift(shiftId, studentNumber);
    }

    public void makeSwaps(String courseCode) {
        Course c = this.courses.get(courseCode);

        Set<Pair<Integer, Integer>> cycle = c.getLargestCycle();
        while (cycle != null) {
            Iterator it = cycle.iterator();
            while (it.hasNext()) {

            }
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
}
