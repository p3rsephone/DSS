package data;

import business.courses.Shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ShiftDAO extends DAO implements Map<String,Shift> {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    @Override
    public int size() {
        return size("Shift");
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty("Shift");
    }

    /**
     * Checks if a certain Shift code exists in the database
     * @param key                    Shift code
     * @return                       True if the shift is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Shift_code FROM Ups.Shift WHERE Shift_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ResultSet rs = ps.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }


    public boolean containsValue(Object value) { //Makes no sense in this context but has to be implemented
        return false;
    }

    /**
     * Gets a Shift from the database
     * @param key  Shift code
     * @return     Shift
     */
    @Override
    public Shift get(Object key) {
        Shift shift = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Shift WHERE Shift_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                shift = new Shift(rs.getString("Shift_code"),rs.getString("Course_code"), rs.getInt("Shift_limit"), rs.getInt("Teacher_number"), rs.getInt("Shift_expectedClasses"), rs.getString("Room_code"), rs.getString("Shift_weekday"), rs.getString("Shift_period"));
                shift.setNumOfStudents(rs.getInt("Shift_numOfStudents"));
                sql = "SELECT * FROM Ups.StudentShift WHERE Shift_code =?";
                ps = conn.prepareStatement(sql);
                ps.setString(1,shift.getCode());
                rs = ps.executeQuery();
                while (rs.next()) {
                    shift.getStudents().put(rs.getInt("Student_number"),rs.getInt("StudentShift_absences"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return shift;
    }

    /**
     * Insert a new Shift in the database
     * @param key    Shift code
     * @param value  Shift
     * @return
     */
    @Override
    public Shift put(String key, Shift value) {
        Shift shift = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Shift\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Shift_numOfStudents = VALUES(Shift_numOfStudents), Shift_limit=VALUES(Shift_limit), Shift_expectedClasses = VALUES(Shift_expectedClasses), Shift_givenClasses = VALUES(Shift_givenClasses), Shift_weekday = VALUES(Shift_weekday), Shift_period = VALUES(Shift_period), Course_code = VALUES(Course_code), Room_code = VALUES(Room_code), Teacher_number=VALUES(Teacher_number);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getCode());
            ps.setInt(2, value.getNumOfStudents());
            ps.setInt(3, value.getLimit());
            ps.setInt(4, value.getExpectedClasses());
            ps.setInt(5, value.getGivenClasses());
            ps.setString(6, value.getWeekday());
            ps.setString(7, value.getPeriod());
            ps.setString(8, value.getCourseId());
            ps.setString(9, value.getRoomCode());
            ps.setInt(10, value.getTeacher());
            ps.executeUpdate();

            if (value.getNumOfStudents()>0) { //Insert absences
                sql = "INSERT INTO Ups.StudentShift\n" +
                      "VALUES (?, ?, ?)\n" +
                      "ON DUPLICATE KEY UPDATE StudentShift_absences = VALUES(StudentShift_absences);";
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (Integer n : value.getStudents().keySet()) {
                    ps.setInt(1, n);
                    ps.setString(2,value.getCode());
                    ps.setInt(3,value.getStudents().get(n));
                    ps.executeUpdate();
                }
            }

            shift = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return shift;
    }

    /**
     * Removes a Shift from the database
     * @param key  Shift code
     * @return     Shift that was deleted
     */
    @Override
    public Shift remove(Object key) {
        Shift shift = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Shift WHERE Shift_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ps.executeUpdate();

            sql="DELETE FROM Ups.StudentShift WHERE Shift_code= ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return shift;
    }

    /**
     * Insert several Shifts into the database
     * @param m  Map of all the shifts
     */
    @Override
    public void putAll(Map<? extends String, ? extends Shift> m) {
        for(Shift s : m.values()) {
            put(s.getCode(), s);
        }
    }

    /**
     * Delete every Shift from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Shift; DELETE FROM Ups.StudentShift WHERE EXISTS(SELECT * FROM Ups.StudentShift)";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            //runtime exception!
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = null;
        try {
            conn = Connect.connect();
            set = new HashSet<>();
            String sql = "SELECT Shift_code FROM Ups.Shift";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                set.add(rs.getString("Shift_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return set;
    }

    /**
     * Gets all of the Shifts from the database
     * @return  Collection of all the shifts
     */
    @Override
    public Collection<Shift> values() {
        Collection<Shift> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Shift";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Shift shift = get(rs.getString("Shift_code"));
                collection.add(shift);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return collection;
    }

    @Override
    public Set<Entry<String, Shift>> entrySet() {
        Set<String> keys = new HashSet<>(this.keySet());

        HashMap<String, Shift> map = new HashMap<>();
        for (String key : keys) {
            map.put(key, this.get(key));
        }
        return map.entrySet();
    }
}
