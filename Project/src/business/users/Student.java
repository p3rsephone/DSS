package business.users;

public class Student extends User{
    private Integer number;

    public Student(String name, String email, String password, Integer number) {
        super(name, email, password);
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }
}
