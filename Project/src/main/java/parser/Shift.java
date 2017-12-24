package parser;

public class Shift {
    private String course;
    private int teacher;
    private int limit;
    private int classes;
    private String roomcode;
    private String weekday;
    private String period;

    public Shift(String course, int teacher, int limit, int classes, String roomcode, String weekday, String period) {
        this.course = course;
        this.teacher = teacher;
        this.limit = limit;
        this.classes = classes;
        this.roomcode = roomcode;
        this.weekday = weekday;
        this.period = period;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getClasses() {
        return classes;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }

    public String getRoomcode() {
        return roomcode;
    }

    public void setRoomcode(String roomcode) {
        this.roomcode = roomcode;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
