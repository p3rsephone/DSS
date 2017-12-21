package business.courses;

public class Request {
    private Integer student;
    private String course;
    private String originShift;
    private String destShift;

    public Request(Integer student, String course, String originShift, String destShift) {
        this.student = student;
        this.course = course;
        this.originShift = originShift;
        this.destShift = destShift;
    }

    public Integer getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }


    public String getOriginShift() {
        return originShift;
    }

    public String getDestShift() {
        return destShift;
    }
}
