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

    public void registerExchange(Student s1, String cocode1, String shcode1, Request request) {
        HashMap<Integer, String> record = new HashMap<>();
        record.put(s1.getNumber(), shcode1);
        record.put(request.getStudent(), request.getShift());
        Exchange e = new Exchange(this.nrOfExchanges++, cocode1, record);
        this.exchanges.put(e.getCode(), e);
    }
}
