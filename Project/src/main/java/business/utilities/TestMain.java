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
        Course course2 = new Course("SD", "Sistemas Distribuidos", 2, 1);
        Shift tp1 = new Shift("DSS-TP1", "DSS", 1, 1, 10, "DI109", "thu", "afternoon");
        Shift tp2 = new Shift("DSS-TP2", "DSS", 2, 1, 10, "DI109", "thu", "afternoon");
        Shift tp3 = new Shift("DSS-TP3", "DSS", 1, 1, 10, "DI109", "thu", "afternoon");
        Shift sd1 = new Shift("SD-TP1", "SD", 1, 1, 10, "DI109", "thu", "morning");
        Shift sd2 = new Shift("SD-TP2", "SD", 3, 1, 10, "DI109", "thu", "morning");
        Shift sd3 = new Shift("SD-TP3", "SD", 1, 1, 10, "DI109", "thu", "morning");
        Student a1 = new Student("joao", "joao@", "123", 1, false);
        Student a2 = new Student("maria", "maria@", "123", 2, false);
        Student a3 = new Student("jose", "jose@", "123", 3, false);
        Student a4 = new Student("marta", "marta@", "123", 4, false);
        Student a5 = new Student("david", "david@", "123", 5, false);
        engine.addCourse(course);
        engine.addCourse(course2);
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
            course2.addShift(sd1);
            course2.addShift(sd2);
            course2.addShift(sd3);
        } catch (ShiftAlredyExistsException e) {
            e.printStackTrace();
        }

        engine.enrollStudent("DSS",  1);
        engine.enrollStudent("DSS",  2);
        engine.enrollStudent("DSS",  3);
        engine.enrollStudent("DSS",  4);
        engine.enrollStudent("DSS",  5);

        engine.enrollStudent("SD",  1);
        engine.enrollStudent("SD",  2);
        engine.enrollStudent("SD",  3);
        engine.enrollStudent("SD",  4);
        engine.enrollStudent("SD",  5);

        engine.allocateStudents();

        try {
            engine.requestExchange("DSS", a1, "DSS-TP2", "DSS-TP3");
            System.out.println(engine.concatRequests(1, "DSS", "DSS-TP2"));
            engine.getCourse("DSS").getBillboard().forEach((k,v) -> v.forEach(r -> System.out.println(r.getCode())));
            a1.getRequests("DSS-TP2").forEach(r -> System.out.println(r.toString()));
            engine.requestExchange("DSS", a1, "DSS-TP2", "DSS-TP1");
            System.out.println(engine.concatRequests(1, "DSS", "DSS-TP2"));
            engine.requestExchange("DSS", a4, "DSS-TP3", "DSS-TP2");
        } catch (TooManyRequestsException | ShiftNotValidException e) {
            e.printStackTrace();
        } catch (ShiftNotValidException e) {
            e.printStackTrace();
        }

        for(Map.Entry<Integer, Student> s : engine.getStudents().entrySet()) {
            System.out.println(s.getValue().getNumber() + ": " + s.getValue().getName() + " ||| " + s.getValue().getShifts().toString());
        }

        for (Exchange e : engine.getAllExchangesOfCourse("DSS")) {
            System.out.println("Code: " + e.getCode() + " => student " + e.getOriginStudent() + " went from " + e.getOriginShift() + " to " + e.getDestShift() + ". Switched with " + e.getDestStudent());
            System.out.println("Cancelled? " + e.isCancelled());
        }

        System.out.println("CANCELLING EXCHANGE");

        try {
            engine.cancelExchange(0);
        } catch (ExchangeDoesNotExistException | StudentNotInShiftException | ExchangeAlreadyCancelledException e) {
            e.printStackTrace();
        }


        for (Map.Entry<Integer, Exchange> entry : engine.getExchanges().entrySet()) {
            Exchange e = entry.getValue();
            System.out.println("Code: " + e.getCode() + " => student " + e.getOriginStudent() + " went from " + e.getOriginShift() + " to " + e.getDestShift() + ". Switched with " + e.getDestStudent());
            System.out.println("Cancelled? " + e.isCancelled());
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

        engine.getStudentsWithoutShift("DSS").forEach(s -> System.out.println(s.getNumber()));

    }
}
