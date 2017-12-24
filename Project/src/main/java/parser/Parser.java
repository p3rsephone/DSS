package parser;
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
        Type type = new TypeToken<Map<String, parser.Course>>(){}.getType();
        Map<String, parser.Course> data = gson.fromJson(jsonFile, type);

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
                        c.addShift(new business.courses.Shift(key,value.getCourse(),value.getLimit(),value.getTeacher(),value.getClasses(),value.getRoomcode(),value.getWeekday(),value.getPeriod()));
                        engine.addCourse(c);
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
                        engine.addUser(new business.users.Teacher(value.getName(),value.getNumber(),value.getEmail(),value.getPassword(),boss));
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
                    for(String course : value.getCourses()){
                        engine.enrollStudent(course,key);
                    }
                }
        );
    }
    /*
    public static void main(String[] args) {
        HashMap<String, Course> courses = new HashMap<>();
        Parser ola = new Parser(courses);
        try {
            ola.parseMscProfiles("/perfies.json");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        courses.forEach((k,v)->System.out.println(v.getName() + " " + v.getWeekday()));
    }
    */
}
