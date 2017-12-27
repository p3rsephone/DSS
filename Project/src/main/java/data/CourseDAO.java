package data;

import business.courses.Course;
import business.courses.Request;
import business.courses.Shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class CourseDAO extends DAO implements Map<String,Course> {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    @Override
    public int size() {
        return size("ParseCourse");
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty("ParseCourse");
    }

    /**
     * Checks if a certain ParseCourse code exists in the database
     * @param key                    ParseCourse code
     * @return                       True if the course is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Course_code FROM Ups.ParseCourse WHERE Course_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) { //Makes no sense in this context but has to be implemented
        return false;
    }

    /**
     * Gets a ParseCourse from the database
     * @param key  ParseCourse code
     * @return     ParseCourse
     */
    @Override
    public Course get(Object key) {
        Course course = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.ParseCourse WHERE Course_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                course = new Course(rs.getString("Course_code"),rs.getString("Course_name"), rs.getInt("Teacher_number"), rs.getInt("Course_year"));

                sql = "SELECT * FROM Ups.ParseShift WHERE Course_code=?;";
                ps = conn.prepareStatement(sql);
                ps.setString(1, key.toString());
                rs = ps.executeQuery();
                Shift shift;
                HashMap <String, Shift> shifts = null;
                while (rs.next()) {
                    shift = new ShiftDAO().get(rs.getString("Shift_code"));
                    shifts.put(rs.getString("Shift_code"),shift);
                }
                course.setShifts(shifts);

                HashMap <String, ArrayList<Request>> requests = new RequestDAO().getRequestsFromCourse(key.toString());
                course.setBillboard(requests);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return course;
    }

    /**
     * Insert a new course in the database
     * @param key    ParseCourse code
     * @param value  ParseCourse
     * @return
     */
    @Override
    public Course put(String key, Course value) {
        Course course = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.ParseCourse\n" +
                    "VALUES (?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Course_name=VALUES(Course_name), Course_year=VALUES(Course_year), Course_name=VALUES(Course_name), Teacher_number=VALUES(Teacher_number);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getCode());
            ps.setString(2, value.getName());
            ps.setInt(3, value.getYear());
            ps.setInt(4, value.getRegTeacher());
            ps.executeUpdate();

            //Insert shifts
            for (Shift s : value.getShifts().values()) {
               new ShiftDAO().put(s.getCode(),s);
            }

            //Insert requests from billboard
            for (ArrayList<Request> a : value.getBillboard().values()) {
                new RequestDAO().put(a.get(0).getDestShift(), a);
            }
            course = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return course;
    }

    /**
     * Removes a course from the database
     * @param key  ParseCourse code
     * @return     ParseCourse that was deleted
     */
    @Override
    public Course remove(Object key) {
        Course course = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.ParseCourse WHERE Course_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ps.executeUpdate();
            sql = "SELECT Shift_code FROM Ups.ParseShift WHERE Course_code = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                new ShiftDAO().remove(rs.getString("Shift_code"));
            }
            new RequestDAO().removeFromCourse(key);

        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return course;
    }

    /**
     * Insert several Courses into the database
     * @param m  Map of all the courses
     */
    @Override
    public void putAll(Map<? extends String, ? extends Course> m) {
        for(Course s : m.values()) {
            put(s.getCode(), s);
        }
    }

    /**
     * Delete every ParseCourse from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.ParseCourse;DELETE FROM Ups.StudentCourse WHERE EXISTS(SELECT Course_code FROM Ups.StudentCourse);";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
            new RequestDAO().clear();
            new ShiftDAO().clear();

        } catch (Exception e) {
            //runtime exception!
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    @Override
    public Set<String> keySet() {
        throw new NullPointerException("Not implemented!"); //Makes no sense in this context but has to be implemented
    }

    /**
     * Gets all of the courses from the database
     * @return  Collection of all the courses
     */
    @Override
    public Collection<Course> values() {
        Collection<Course> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.ParseCourse";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            Course course;
            while (rs.next()) {
                course = get(rs.getString("Course_code"));
                collection.add(course);
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
    public Set<Entry<String, Course>> entrySet() {
        throw new NullPointerException("Not implemented!"); //Makes no sense in this context but has to be implemented
    }
}
