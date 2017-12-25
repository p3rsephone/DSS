package data;

import business.users.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class TeacherDao implements Map<String, Teacher> {
    Connection conn;

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
            String sql= "SELECT count(*) FROM Ups.Teacher";
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
        return size()==0;
    }

    /**
     * Checks if a certain Teacher number exists in the database
     * @param key                    Teacher number
     * @return                       True if the teacher is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Teacher_name FROM Ups.Teacher WHERE Teacher_number =?;";
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

    @Override
    public boolean containsValue(Object value) {//Makes no sense in this context but has to be implemented
        return false;
    }

    /**
     * Gets a Teacher from the database
     * @param key  Teacher number
     * @return     Teacher
     */
    @Override
    public Teacher get(Object key) {
        Teacher teacher = null;
        try {
            conn = Connect.connect();
            String sql= "SELECT * FROM Ups.Teacher WHERE Teacher_number=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (Integer)key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                teacher = new Teacher(rs.getString("Teacher_name"),rs.getInt("Teacher_number"),rs.getString("Teacher_email"),rs.getString("Teacher_password"),rs.getBoolean("Teacher_isBoss"),"");

                sql = "SELECT Course_code FROM Ups.Course WHERE Teacher_number = ?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,(Integer) key);
                rs = ps.executeQuery();
                if (rs.next()) { //If it has one, adds owned course
                    teacher.setCourse(rs.getString("Course_code"));
                }

                sql = "SELECT Shift_code FROM Ups.Shift WHERE Teacher_number = ?;" ;
                ps = conn.prepareStatement(sql);
                ps.setInt(1,(Integer) key);
                rs = ps.executeQuery();
                while (rs.next()) { // Adds all the shifts
                    teacher.addShift(rs.getString("Shift_code"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return teacher;
    }

    /**
     * Insert a new Teacher in the database
     * @param key    Teacher number
     * @param value  Teacher
     * @return
     */
    @Override
    public Teacher put(String key, Teacher value) {
        Teacher teacher = null;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Ups.Teacher\n" +
                    "VALUES (?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Teacher_name=VALUES(Teacher_name),  Teacher_number = VALUES(Teacher_number), Teacher_email=VALUES(Teacher_email), Teacher_password=VALUES(Teacher_password), Teacher_isBoss=VALUES(Teacher_isBoss)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getName());
            ps.setInt(2, value.getNumber());
            ps.setString(3, value.getEmail());
            ps.setString(4,value.getPassword());
            ps.setBoolean(5, value.isBoss());
            ps.executeUpdate();
            //TODO: Method to update more shit
            teacher = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return teacher;
    }

    /**
     * Removes a Teacher from the database
     * @param key  Teacher number
     * @return     Teacher that was deleted
     */
    @Override
    public Teacher remove(Object key) {
        Teacher teacher = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Teacher WHERE Teacher_number = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return teacher;
    }

    /**
     * Insert several Teachers into the database
     * @param m  Map of all the teachers
     */
    @Override
    public void putAll(Map<? extends String, ? extends Teacher> m) {
        for(Teacher s : m.values()) {
            put(s.getNumber().toString(), s); //a primeira parte é inútil.
        }
    }

    /**
     * Delete every Teacher from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Teacher WHERE Teacher_number>0;";
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
        } catch (Exception e) {
            //runtime exeption!
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
     * Gets all of the Teachers from the database
     * @return  Collection of all the teachers
     */
    @Override
    public Collection<Teacher> values() {
        Collection<Teacher> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Teacher";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Teacher teacher = get(rs.getInt("Teacher_number"));
                collection.add(teacher);
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
    public Set<Entry<String, Teacher>> entrySet() {
        throw new NullPointerException("Not implemented!"); //Makes no sense in this context but has to be implemented
    }
}
