package parser;
import business.courses.Course;
import business.courses.Year;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import business.courses.Year.*;


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
                courses.put(id,new Course(id, v.getNome(), Year.FOURTH, v.getDiasem()));
            })
        );
    }

    public void parseMscOptional(String file)throws FileNotFoundException{
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, MscOptional>>(){}.getType();
        Map<String,MscOptional> data = gson.fromJson(jsonFile, type);

        data.forEach((k,v)->
                        courses.put(k,new Course(k, v.getNome(), Year.FOURTH, v.getDiasem()))
        );

        //TODO per esta disponivel fix
    }

    public void parseUcs(String file)throws FileNotFoundException{
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        JsonObject jsonFile = gson.fromJson(jsonReader, JsonObject.class);
        Type type = new TypeToken<Map<String, UC>>(){}.getType();
        Map<String,UC> data = gson.fromJson(jsonFile, type);

        data.forEach((k,v)->{
                    String codigo = v.getCodigo();
                    Pattern p = Pattern.compile("H5(.*?)N");
                    Matcher m = p.matcher(codigo);
                    m.find();
                    String text = m.group(1);
                    courses.put(codigo,new Course(codigo, v.getNome(), Year.FIRST.fromInteger(Integer.parseInt(text)), ""));
                }

        );
    }

    public static void main(String[] args) {
        HashMap<String, Course> courses = new HashMap<>();
        Parser ola = new Parser(courses);
        try {
                ola.parseMscProfiles("perfies.json");
                ola.parseMscOptional("opcionais.json");
                ola.parseUcs("ucs.json");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        courses.forEach((k,v)->System.out.println(v.getName() + " "+ v.getYear() +" " + v.getWeekday()));
    }
}
