package business.utilities.parser;

import java.util.List;

public class Student {
    private String name;
    private String email;
    private String password;
    private String statute;
    private List<String> courses;

    public Student(String name, String email, String password, String statute, List<String> courses) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.statute = statute;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatute() {
        return statute;
    }

    public void setStatute(String statute) {
        this.statute = statute;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
