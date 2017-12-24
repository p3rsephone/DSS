package data;

import business.courses.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CourseDao implements Map<String,Course> {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    @Override
    public int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            String sql= "SELECT count(*) FROM Ups.Course;";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()) {
                i = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
        finally {
            Connect.close(conn);
        }
        return i;
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {

        return size() == 0;
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
            ps.setInt(1, Integer.parseInt(key.toString()));
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
            ps.setInt(1, (Integer)key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                course = new Course(rs.getString("Course_code"),rs.getInt("Course_capacity"));
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
                    "VALUES (?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Course_capacity=VALUES(Course_capacity);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getCode());
            ps.setInt(2, value.getCapacity());
            ps.executeUpdate();

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
            ps.setString(1, (String)key);
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
            String sql = "DELETE FROM Ups.Course;";
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
            while (rs.next()) {
                Course course = get(rs.getString("Course_code"));
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
