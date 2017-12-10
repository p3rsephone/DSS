package business.courses;

public class Request {
    private Integer student;
    private String course;
    private String shift;

    public Request(Integer student, String course, String shift) {
        this.student = student;
        this.course = course;
        this.shift = shift;
    }

    public Integer getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    public String getShift() {
        return shift;
    }
}
