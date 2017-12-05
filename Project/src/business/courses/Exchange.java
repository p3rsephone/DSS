package business.courses;

import business.users.Student;

import java.util.Set;

public class Exchange {
    private Integer code;
    private Set<Student> students;

    public Exchange(Integer code, Set<Student> students) {
        this.code = code;
        this.students = students;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Integer getCode() {
        return code;
    }
}
