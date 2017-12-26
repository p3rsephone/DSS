package business.utilities.parser;
import business.Engine;
import business.courses.Course;
import business.exceptions.ShiftAlredyExistsException;
import business.exceptions.UserAlredyExistsException;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;



public class Parser {
    private Engine engine;
    private Gson gson ;

    public Parser(Engine engine) {
        this.engine = engine;
        this.gson = new Gson();
    }

    /*
    public void parseMscProfiles(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, Map<String, MscUc>>>(){}.getType();
        Map<String,Map<String,MscUc>> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)->
            value.forEach((k,v)-> {
                String id = k.replace("UC", key);
                engine.
            })
        );
    }
    */

    public void parseCourse(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, business.utilities.parser.Course>>(){}.getType();
        Map<String, business.utilities.parser.Course> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)->
            engine.addCourse(new Course(value.getCodigo(),value.getNome(),value.getTeacherReg(),value.getYear()))
        );
    }

    public void parseShift(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, Shift>>(){}.getType();
        Map<String,Shift> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)->{
            Course c = engine.getCourse(value.getCourse());
                    try {

                        engine.registerShift(c,
                                new business.courses.Shift(key,value.getCourse(),value.getLimit(),value.getTeacher()
                                        ,value.getClasses(),value.getRoomcode(),value.getWeekday(),value.getPeriod()));
                    } catch (ShiftAlredyExistsException e) {
                        e.printStackTrace();
                    }
        }
        );
    }
    public void parseRoom(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, Room>>(){}.getType();
        Map<String,Room> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)->{
            engine.addRoom(new business.courses.Room(key,value.getCapacidade()));
        }
        );
    }
    public void parseTeacher(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String,Teacher>>(){}.getType();
        Map<String,Teacher> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)->{
            boolean boss = value.getBoss().equals("y");
                    try {
                        engine.addUser(new business.users.Teacher(value.getName(),value.getNumber(),value.getEmail(),value.getPassword(),boss,value.getCourse()));
                    } catch (UserAlredyExistsException e) {
                        e.printStackTrace();
                    }

                }
        );
    }

    public void parseStudent(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<Integer, Student>>(){}.getType();
        Map<Integer,Student> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)-> {
            try {
                engine.addUser(new business.users.Student(value.getName(),value.getEmail(),value.getPassword(),key,Boolean.valueOf(value.getStatute())));
            } catch (UserAlredyExistsException e) {
                e.printStackTrace();
            }
            value.getCourses().forEach((v)->engine.enrollStudent(v,key));
                }
        );
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        Parser ola = new Parser(engine);
        try {
                ola.parseRoom("/home/resende/DSS/Project/src/main/java/business.utilities.parser/rooms.json");
                ola.parseCourse("/home/resende/DSS/Project/src/main/java/business.utilities.parser/courses1.json");
                ola.parseCourse("/home/resende/DSS/Project/src/main/java/business.utilities.parser/courses2.json");
                ola.parseShift("/home/resende/DSS/Project/src/main/java/business.utilities.parser/shifts1.json");
                ola.parseShift("/home/resende/DSS/Project/src/main/java/business.utilities.parser/shifts2.json");
                ola.parseStudent("/home/resende/DSS/Project/src/main/java/business.utilities.parser/student.json");
        } catch (FileNotFoundException e) {
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
            System.out.println("File not found");
        }
        engine.allocateStudents();
        HashMap<String, Course> courses = engine.getCourses();
        HashMap<String, business.courses.Room> salas = engine.getRooms();
        System.out.println("/n");
        System.out.println("COURSES/n");
        courses.forEach((k,v)->System.out.println(v.getCode() + " " + v.getName()));
        System.out.println("/n");
        System.out.println("SALAS/n");
        salas.forEach((k,v)->System.out.println(v.getCode() + " " + v.getCapacity()));
        System.out.println("/n");
        System.out.println("shifts/n");
        courses.forEach((k,v)->{
            System.out.println(v.getCode() + " " + ":");
            v.getShifts().forEach((key,value)-> System.out.println("/t "+ key +" "+value.getCode() + value.getCourseId()) );
        });
        System.out.println("/n");
        System.out.println("Students/n");
        engine.getStudents().forEach((key,value)->{
            System.out.println(key );
            value.getShifts().forEach(System.out::println);
        } );
    }

}
