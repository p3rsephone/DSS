package business.utilities;

import business.Engine;
import business.courses.Course;
import business.courses.Exchange;
import business.courses.Request;
import business.courses.Shift;
import business.exceptions.*;
import business.users.Student;

import java.util.ArrayList;
import java.util.Map;

public class TestMain {

    public static void main(String[] args) {
        Engine engine = new Engine();
        Course course = new Course("DSS", "Desenvolvimento de Sistemas de Software", 1, 1);
        Shift tp1 = new Shift("TP1", "DSS", 10, 1, 10, "DI109", "thu", "afternoon");
        Shift tp2 = new Shift("TP2", "DSS", 10, 1, 10, "DI109", "thu", "afternoon");
        Shift tp3 = new Shift("TP3", "DSS", 10, 1, 10, "DI109", "thu", "afternoon");
        Student a1 = new Student("joao", "joao@", "123", 1, false);
        Student a2 = new Student("maria", "maria@", "123", 2, false);
        Student a3 = new Student("jose", "jose@", "123", 3, false);
        Student a4 = new Student("marta", "marta@", "123", 4, false);
        Student a5 = new Student("david", "david@", "123", 5, false);
        engine.addCourse(course);

        try {
            engine.addUser(a1);
            engine.addUser(a5);
            engine.addUser(a2);
            engine.addUser(a3);
            engine.addUser(a4);
        } catch (UserAlredyExistsException e) {
            e.printStackTrace();
        }

        try {
            course.addShift(tp1);
            course.addShift(tp2);
            course.addShift(tp3);
        } catch (ShiftAlredyExistsException e) {
            e.printStackTrace();
        }

        engine.enrollStudent("DSS", "TP1", 1);
        engine.enrollStudent("DSS", "TP2", 2);
        engine.enrollStudent("DSS", "TP2", 3);
        engine.enrollStudent("DSS", "TP3", 4);
        engine.enrollStudent("DSS", "TP3", 5);


        try {
            engine.requestExchange("DSS", a1, "TP1", "TP2");
            engine.requestExchange("DSS", a2, "TP2", "TP1");
            engine.requestExchange("DSS", a3, "TP2", "TP3");
            engine.requestExchange("DSS", a4, "TP3", "TP1");
            engine.requestExchange("DSS", a5, "TP3", "TP2");
        } catch (TooManyRequestsException e) {
            e.printStackTrace();
        }

        for(Map.Entry<String, ArrayList<Request>> r : engine.getCourse("DSS").getBillboard().entrySet() ) {
            for (Request e : r.getValue()) {
                System.out.println(e.getStudent() + "is on" +  e.getOriginShift()+ " wants to go to " + e.getDestShift());
            }
        }

        for(Map.Entry<String, ArrayList<Request>> r : engine.getCourse("DSS").getBillboard().entrySet() ) {
            for (Request e : r.getValue()) {
                System.out.println(e.getStudent() + "is on" +  e.getOriginShift() + " wants to go to " + e.getDestShift());
            }
        }


        for(Map.Entry<Integer, Exchange> e : engine.getExchanges().entrySet()) {
            System.out.println(e.getValue().getOriginStudent() + "->" + e.getValue().getDestShift());
        }

        for(Map.Entry<Integer, Student> s : engine.getStudents().entrySet()) {
            System.out.println(s.getValue().getNumber() + ": " + s.getValue().getName() + " ||| " + s.getValue().getShifts().toString());
        }

        for(Map.Entry<String, Shift> s : engine.getCourse("DSS").getShifts().entrySet()) {
            System.out.println(s.getValue().getCode() + " <><><>  ");
            for(Map.Entry<Integer, Integer> t : s.getValue().getStudents().entrySet()) {
                System.out.println(t.getKey());
            }
        }

        for(Map.Entry<Integer, Student> s : engine.getStudents().entrySet()) {
            for (Request r : s.getValue().getRequests()) {
                System.out.println(s.getKey() + " wants " + r.getDestShift());
            }
        }
    }
}
