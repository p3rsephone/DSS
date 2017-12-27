package business.utilities.parser;

public class ParseTeacher {
    private String name;
    private int number;
    private String email;
    private String password;
    private String boss;
    private String owned;

    public ParseTeacher(String name, int number, String email, String password, String boss, String owned) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.boss = boss;
        this.owned = owned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getCourse() {
        return owned;
    }

    public void setCourse(String owned) {
        this.owned= owned;
    }
}
