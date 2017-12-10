package business.courses;

public class Room {
    private String code;
    private Integer capacity;

    public Room(String code, Integer capacity) {
        this.code = code;
        this.capacity = capacity;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getCode() {
        return code;
    }
}
