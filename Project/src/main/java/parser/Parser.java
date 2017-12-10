package parser;
import business.courses.Course;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static business.courses.Year.*;


public class Parser {
    private HashMap<String, Course> courses;
    private Gson gson ;

    public Parser(HashMap<String,Course> courses) {
        this.courses = courses;
        this.gson = new Gson();
    }

    public void parseMscProfiles(String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, Map<String, MscUc>>>(){}.getType();
        Map<String,Map<String,MscUc>> data = gson.fromJson(jsonFile, type);

        data.forEach((key,value)->
            value.forEach((k,v)-> {
                String id = k.replace("UC", key);
                courses.put(id,new Course(id, v.getNome(), FOURTH, v.getDiasem()));
            })
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
