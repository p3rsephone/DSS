package business.users;

public class User {
    private String name;
    private Integer number;
    private String email;
    private String password;

    public User(String name, Integer number, String email, String password) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getNumber() {
        return number;
    }

}
