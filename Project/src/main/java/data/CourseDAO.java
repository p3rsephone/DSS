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
        return size("Course");
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty("Course");
    }

    /**
     * Checks if a certain Course code exists in the database
     * @param key                    Course code
     * @return                       True if the course is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Course_code FROM Ups.Course WHERE Course_code=?;";
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


    public boolean containsValue(Object value) { //Makes no sense in this context but has to be implemented
        return false;
    }

    /**
     * Gets a Course from the database
     * @param key  Course code
     * @return     Course
     */
    @Override
    public Course get(Object key) {
        Course course = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Course WHERE Course_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                course = new Course(rs.getString("Course_code"),rs.getString("Course_name"), rs.getInt("Teacher_number"), rs.getInt("Course_year"));

                sql = "SELECT * FROM Ups.Shift WHERE Course_code=?;";
                ps = conn.prepareStatement(sql);
                ps.setString(1, key.toString());
                rs = ps.executeQuery();
                Shift shift;
                while (rs.next()) {
                    shift = new Shift(rs.getString("Shift_code"),rs.getString("Course_code"), rs.getInt("Shift_limit"), rs.getInt("Teacher_number"), rs.getInt("Shift_expectedClasses"), rs.getString("Room_code"), rs.getString("Shift_weekday"), rs.getString("Shift_period"));

                    sql = "SELECT * FROM Ups.StudentShift WHERE Shift_code =?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1,shift.getCode());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        shift.getStudents().put(rs.getInt("Student_number"),rs.getInt("StudentShift_absences"));
                    }
                    course.addShift(shift);
                }

                sql = "SELECT * FROM Ups.Request WHERE Course_code = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1,course.getCode());
                rs = ps.executeQuery();
                Request request;
                while (rs.next()) {
                    request = new Request(rs.getInt("Student_number"),rs.getString("Course_code"), rs.getString("Request_originShift"), rs.getString("Request_destShift"));
                    course.getBillboard().get(rs.getString("Request_destShift")).add(request);
                }
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
     * @param key    Course code
     * @param value  Course
     * @return
     */
    @Override
    public Course put(String key, Course value) {
        Course course = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Course\n" +
                    "VALUES (?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Course_name=VALUES(Course_name), Course_year=VALUES(Course_year), Course_name=VALUES(Course_name), Teacher_number=VALUES(Teacher_number);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getCode());
            ps.setString(2, value.getName());
            ps.setInt(3, value.getYear());
            ps.setInt(4, value.getRegTeacher());
            ps.executeUpdate();

            sql = sql = "INSERT INTO Ups.Shift\n" +  //Insert or update Shifts
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Shift_limit=VALUES(Shift_limit), Shift_expectedClasses = VALUES(Shift_expectedClasses), Shift_weekday = VALUES(Shift_weekday), Shift_period = VALUES(Shift_period), Course_code = VALUES(Course_code), Room_code = VALUES(Room_code), Teacher_number=VALUES(Teacher_number);";
            ps = conn.prepareStatement(sql);
            for (Shift s : value.getShifts().values()) {
                ps.setString(1, s.getCode());
                ps.setInt(2, s.getNumOfStudents());
                ps.setInt(3, s.getLimit());
                ps.setInt(4, s.getExpectedClasses());
                ps.setString(5, s.getWeekday());
                ps.setString(6, s.getPeriod());
                ps.setString(7, s.getCourseId());
                ps.setString(8, s.getRoomCode());
                ps.setInt(9, s.getTeacher());
                ps.executeUpdate();
            }

            //Insert requests from billboard
            sql = "INSERT INTO Ups.Request (Request_originShift, Request_destShift, Course_code)\n" +
                  "VALUES (?, ?, ?);";
            ps = conn.prepareStatement(sql);
            String dest;
            for (ArrayList<Request> a : value.getBillboard().values()) {
                dest = a.get(0).getDestShift();
                for (Request r : a) {
                    ps.setString(1,r.getOriginShift());
                    ps.setString(2,dest);
                    ps.setString(3,r.getCourse());
                    ps.executeUpdate();
                }
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
     * @param key  Course code
     * @return     Course that was deleted
     */
    @Override
    public Course remove(Object key) {
        Course course = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Course WHERE Course_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ps.executeUpdate();
            sql = "DELETE FROM Ups.Shift WHERE Course_code = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ps.executeUpdate();
            sql = "DELETE FROM Ups.Request WHERE Course_code = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, key.toString());
            ps.executeUpdate();
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
     * Delete every Course from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Course;DELETE FROM Ups.Request WHERE EXISTS(SELECT Course_code FROM Ups.Request);DELETE FROM Ups.Shift WHERE EXISTS(SELECT Course_code FROM Ups.Shift);";
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
            String sql = "SELECT * FROM Ups.Course";
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
