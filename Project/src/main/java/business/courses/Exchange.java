package business.courses;

import data.EngineDAO;

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
    private EngineDAO db;
    private Boolean cancelled;

    public Exchange(Integer code, String course, String originShift, String destShift, Integer originStudent, Integer destStudent) {
        this.code = code;
        this.course = course;
        this.originShift = originShift;
        this.destShift = destShift;
        this.originStudent = originStudent;
        this.destStudent = destStudent;
        this.cancelled = false;
        this.db = new EngineDAO();
    }

    public Exchange(String originShift, String destShift, Integer originStudent, Integer destStudent) {
        this.code = -1;
        this.course = "";
        this.destShift = destShift;
        this.originShift = originShift;
        this.originStudent = originStudent;
        this.destStudent = destStudent;
        this.cancelled = false;
        this.db = new EngineDAO();
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

    public Boolean isCancelled() {
        return this.cancelled;
    }

    public void setCode(Integer code) {
        db.remove(this.getCode());
        this.code = code;
        db.put(this.getCode(), this);
    }

    public void setCourse(String course) {
        this.course = course;
        db.put(this.getCode(),this);
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancelExchange() {
        this.cancelled = true;
        db.put(this.getCode(), this);
    }
}
