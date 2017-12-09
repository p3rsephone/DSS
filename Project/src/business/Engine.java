package business;

import business.courses.Course;
import business.courses.Exchange;
import business.courses.Request;
import business.exceptions.TooManyRequestsException;
import business.exceptions.UserAlredyExistsException;
import business.users.Student;
import business.users.Teacher;
import business.users.User;

import java.util.HashMap;

public class Engine {
    private HashMap<Integer, Student> students;
    private HashMap<Integer, Teacher> teachers;
    private HashMap<String, Course> courses;
    private HashMap<Integer, Exchange> exchanges;
    private HashMap<String, Request> billboard;
    private Integer nrOfExchanges;

    public Engine() {
        this.nrOfExchanges = 0;
        this.billboard = new HashMap<>();
        this.teachers = new HashMap<>();
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.exchanges = new HashMap<>();
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

    public void requestExchange(Student s, String courseCode, String shiftCode) throws TooManyRequestsException {

        //Add verification of matching request to call this.registerExchange()

        if (s.getNrequests() >= s.numberEnrollments()) {
            throw new TooManyRequestsException();
        }
        Request r = new Request(s.getNumber(), courseCode, shiftCode);
        this.billboard.put(shiftCode, r);
    }

    public void registerExchange(Student s1, String courseCode, String shcode1, Request request) {
        Integer s2number = request.getStudent();
        String shcode2 = request.getShift();
        Exchange e = new Exchange(this.nrOfExchanges++, courseCode, shcode1, shcode2, s1.getNumber(), s2number);
        this.exchanges.put(e.getCode(), e);
    }

    public void cancelExchange(Integer code) {
        Exchange e = this.exchanges.get(code);
        Integer s1number = e.getOriginStudent();
        String s1currShift = e.getDestShift();
        Integer s2number = e.getDestStudent();
        String s2currShift = e.getOriginShift();

        this.courses.get(e.getCourse()).swap(s1number, s1currShift, s2number, s2currShift);
    }

    public void defineShiftLimit(String courseId, String shiftId, Integer limit) {
        this.courses.get(courseId).setShiftLimit(shiftId, limit);
    }

    public void enrollStudent(String courseId, String shiftId, Integer studentNumber) {
        this.courses.get(courseId).addStudentToShift(shiftId, studentNumber);
    }

    public boolean expellStudent(String courseId, String shiftId, Integer studentNumber) {
        return this.courses.get(courseId).removeStudentFromShift(shiftId, studentNumber);
    }
}
