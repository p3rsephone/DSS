package business.utilities;

import business.exceptions.InvalidWeekDayException;

import java.util.HashMap;

public class Schedule {

    private HashMap<String, Boolean> morning;
    private HashMap<String, Boolean> afternoon;
    private HashMap<String, Pair<String, String>> shifts;


    public Schedule() {
        this.morning = new HashMap<>();
        this.afternoon = new HashMap<>();
        this.shifts = new HashMap<>();
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
    }

    public Boolean usePeriod(String shift, String weekday, String period) throws InvalidWeekDayException {
        switch (period) {
            case "morning":
                if (this.morning.get(weekday)) {
                    return false;
                } else {
                    this.morning.put(weekday, true);
                    this.shifts.put(shift, new Pair<>(weekday, period));
                    return true;
                }
            case "afternoon":
                if (this.afternoon.get(weekday)) {
                    return false;
                } else {
                    this.afternoon.put(weekday, true);
                    this.shifts.put(shift, new Pair<>(weekday, period));
                    return true;
                }
            case "full":
                if (this.afternoon.get(weekday) || this.morning.get(weekday)) {
                    return false;
                } else {
                    this.afternoon.put(weekday, true);
                    this.morning.put(weekday, true);
                    this.shifts.put(shift, new Pair<>(weekday, period));
                    return true;
                }
            default:
                throw new InvalidWeekDayException();
        }
    }

    public Boolean freePeriod(String shift) {
        if (!this.shifts.containsKey(shift)) {
            return false;
        }
        Pair<String, String> sc = this.shifts.get(shift);
        String weekday = sc.t;
        String period = sc.u;
        if (period.equals("morning")) {
            this.morning.put(weekday, false);
            return true;
        } else if (period.equals("afternoon")) {
            this.morning.put(weekday, false);
            return true;
        } else if (period.equals("full")) {
            this.morning.put(weekday, false);
            this.afternoon.put(weekday, false);
            return true;
        } else return false;
    }

    public Boolean isOccuppied(String weekday, String period) throws InvalidWeekDayException {
        if (period.equals("morning")) {
            return this.morning.get(weekday);
        } else if (period.equals("afternoon")) {
            return this.afternoon.get(weekday);
        } else if (period.equals("full")) {
            return (this.afternoon.get(weekday) && this.morning.get(weekday));
        } else throw new InvalidWeekDayException();
    }
}
