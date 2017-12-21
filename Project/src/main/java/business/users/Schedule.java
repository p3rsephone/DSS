package business.users;

import business.exceptions.InvalidWeekDayException;

import java.util.HashMap;

public class Schedule {

    private HashMap<String, Boolean> morning;
    private HashMap<String, Boolean> afternoon;
    private Integer ocuppiedPeriods;


    public Schedule() {
        this.morning = new HashMap<>();
        this.afternoon = new HashMap<>();
        this.morning.put("mon", false);
        this.morning.put("tue", false);
        this.morning.put("wed", false);
        this.morning.put("thu", false);
        this.morning.put("fri", false);
        this.afternoon.put("mon", false);
        this.afternoon.put("tue", false);
        this.afternoon.put("wed", false);
        this.afternoon.put("thu", false);
        this.afternoon.put("fri", false);
        this.ocuppiedPeriods = 0;
    }

    public Boolean usePeriod(String weekday, String period) throws InvalidWeekDayException {
        if(this.ocuppiedPeriods == 7) {
            return false;
        }
        if (weekday.equals("morning")) {
            if(this.morning.get(period)) {
                return false;
            } else {
                this.morning.put(period, true);
                return true;
            }
        } else if (weekday.equals("afternoon")) {
            if(this.morning.get(period)) {
                return false;
            } else {
                this.morning.put(period, true);
                return true;
            }
        } else throw new InvalidWeekDayException();
    }

    public Boolean isOcuppied(String weekday, String period) throws InvalidWeekDayException {
        if (weekday.equals("morning")) {
            return this.morning.get(period);
        } else if (weekday.equals("afternoon")) {
            return this.afternoon.get(period);
        } else throw new InvalidWeekDayException();
    }

    public Boolean isFull(){
        return (this.ocuppiedPeriods == 7);
    }
}
