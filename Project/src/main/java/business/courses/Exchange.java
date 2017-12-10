package business.courses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Exchange {
    private Integer code;
    private String course;
    private HashMap<Integer, String> exchanges;

    public Exchange(Integer code, String course, HashMap<Integer, String> exchanges) {
        this.code = code;
        this.course = course;
        this.exchanges = exchanges;
    }


    public Integer getCode() {
        return code;
    }

    public String getCourse() {
        return course;
    }

    public Set<Map.Entry<Integer, String>> getExchanges() {
        return exchanges.entrySet();
    }
}
