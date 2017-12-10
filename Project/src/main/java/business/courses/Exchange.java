package business.courses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Exchange {
    private Integer code;
    private String course;
    private String originShift;
    private String destShift;
    private Integer originStudent;
    private Integer destStudent;

    public Exchange(Integer code, String course, String originShift, String destShift, Integer originStudent, Integer destStudent) {
        this.code = code;
        this.course = course;
        this.originShift = originShift;
        this.destShift = destShift;
        this.originStudent = originStudent;
        this.destStudent = destStudent;
    }


    public Integer getCode() {
        return code;
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

    public Integer getOriginStudent() {
        return originStudent;
    }

    public Integer getDestStudent() {
        return destStudent;
    }
}
