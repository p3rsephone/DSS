package data;

import business.courses.Request;
import business.users.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentDAO extends DAO implements Map<String,Student> {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    @Override
    public int size() {
        return size("Student");
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty("Student");
    }

    /**
     * Checks if a certain Student number exists in the database
     * @param key                    Student number
     * @return                       True if the students is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Student_name FROM Ups.Student WHERE Student_number=?;";
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
     * Gets a Student from the database
     * @param key  Student number
     * @return     Student
     */
    @Override
    public Student get(Object key) {
        Student student = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Student WHERE Student_number=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (Integer)key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                student = new Student(rs.getString("Student_name"),rs.getString("Student_email"),rs.getString("Student_password"),rs.getInt("Student_number"),rs.getBoolean("Student_statute"));
                sql="SELECT S.Shift_code FROM Ups.Shift AS S\n" +
                    "JOIN Ups.StudentShift AS SS ON S.Shift_code=SS.Shift_code\n" +
                    "WHERE SS.Student_number=?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,(Integer)key);
                rs = ps.executeQuery();
                while (rs.next()) { //If there are shifts associated get their codes and add them
                    student.addShift(rs.getString("Shift_code"));
                }

                sql = "SELECT C.Course_code FROM Ups.Course AS C\n" +
                        "JOIN Ups.StudentCourse AS SC ON C.Course_code=SC.Course_code\n" +
                        "WHERE SC.Student_number=?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,(Integer)key);
                rs = ps.executeQuery();
                while (rs.next()) { //If there are enrollments associated get their codes and add them
                        student.addEnrollment(rs.getString("Course_code"));
                }

                sql="SELECT * FROM Ups.Request AS R\n" +
                        "JOIN Ups.RequestStudent AS RS ON R.Request_id=RS.Request_id\n" +
                        "WHERE RS.Student_number=?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,(Integer)key);
                rs = ps.executeQuery();
                while (rs.next()) { //If there are requests associated get their codes and add them
                    Request rq = new Request((Integer)key,rs.getString("Course_code"),rs.getString("Request_originShift"), rs.getString("Request_destShift"));
                    student.addPendingRequest(rq);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return student;
    }

    /**
     * Insert a new Student in the database
     * @param key    Student number
     * @param value  Student
     * @return
     */
    @Override
    public Student put (String key, Student value) {
        Student student = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Student\n" +
                    "VALUES (?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE Student_name=VALUES(Student_name), Student_email=VALUES(Student_email), Student_password=VALUES(Student_password), Student_statute=VALUES(Student_statute);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, value.getNumber());
            ps.setString(2, value.getName());
            ps.setString(3, value.getEmail());
            ps.setString(4,value.getPassword());
            ps.setBoolean(5,value.isStatute());
            ps.executeUpdate();

            if (value.getNshifts()>0) { //Insert the shifts
                int i;
                sql = "INSERT INTO Ups.StudentShift (Student_number, Shift_code)\n" +
                      "VALUES ";
                for (i=1;i<value.getNshifts();i++) {
                    sql += "(?,?), ";
                }
                sql +="(?,?);" ;

                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                i=1;
                for (String s : value.getShifts()) {
                    ps.setInt(i++,value.getNumber());
                    ps.setString(i++,s);
                }
                ps.executeUpdate(sql);
            }

            if (value.getNEnrollments()>0) { //Insert enrollments
                int i;
                sql = "INSERT INTO Ups.StudentCourse\n" +
                        "VALUES ";
                for (i=1;i<value.getNshifts();i++) {
                    sql += "(?,?,?), ";
                }
                sql +="(?,?,?);" ;

                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                i=1;
                String sqlWhoTeacher = "SELECT Teacher_Course_number FROM Ups.Course WHERE Course_code=?;";
                PreparedStatement psTeacher = conn.prepareStatement(sqlWhoTeacher);
                for (String s : value.getEnrollments()) {
                    psTeacher.setString(1,s);
                    Integer teacherCode = psTeacher.executeQuery().getInt("Teacher_Course_number"); //Get Course's Teacher
                    ps.setInt(i++,value.getNumber());
                    ps.setString(i++,s);
                    ps.setInt(i++,teacherCode);
                }
                ps.executeUpdate(sql);
            }
            if (value.getNrequests()>0) { //Insert requests
                int i;
                sql = "INSERT INTO Ups.RequestStudent (Request_Course_code, Student_number)\n" +
                        "VALUES ";
                String sqlRequest = "INSERT INTO Ups.Request (Request_originShift, Request_destShift, Course_code)\n" +
                                     "VALUES ";
                for (i=1;i<value.getNrequests();i++) {
                    sql += "(?,?), ";
                    sqlRequest += "(?,?,?), ";
                }
                sql +="(?,?);" ;
                sqlRequest += "(?,?,?);";
                PreparedStatement psRequest = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS);
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                i=1;
                Integer j=1;
                for (Request r : value.getRequests()) {
                    psRequest.setString(j++,r.getOriginShift());
                    psRequest.setString(j++,r.getDestShift());
                    psRequest.setString(j++,r.getCourse());

                    ps.setString(i++,r.getCourse());
                    ps.setInt(i++,r.getStudent());
                }
                psRequest.executeUpdate(sqlRequest);
                ps.executeUpdate(sql);

            }

            student = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return student;
    }

    /**
     * Removes a Student from the database
     * @param key  Student number
     * @return     Student that was deleted
     */
    @Override
    public Student remove(Object key) {
        Student student = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Student WHERE Student_number = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (Integer)key);
            ps.executeUpdate();
            if (student.getNshifts()>0) {
                sql="DELETE FROM Ups.StudentShift WHERE Student_number= ?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, (Integer)key);
                ps.executeUpdate();
            }
            if (student.getNEnrollments()>0) {
                sql="DELETE FROM Ups.StudentCourse WHERE Student_number= ?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, (Integer)key);
                ps.executeUpdate();
            }
            if (student.getNrequests()>0) {
                sql="DELETE FROM Ups.RequestStudent WHERE Student_number= ?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, (Integer)key);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return student;
    }

    /**
     * Insert several Students into the database
     * @param m  Map of all the students
     */
    @Override
    public void putAll(Map<? extends String, ? extends Student> m) {
        for(Student s : m.values()) {
            put(s.getNumber().toString(), s);
        }
    }

    /**
     * Delete every Students from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Student WHERE Student_number>0;DELETE FROM Ups.StudentShift WHERE EXISTS(SELECT Student_number FROM Ups.StudentShift);DELETE FROM Ups.StudentCourse WHERE EXISTS(SELECT Student_number FROM Ups.StudentCourse);DELETE FROM Ups.RequestStudent WHERE EXISTS(SELECT Student_number FROM Ups.RequestStudent);";
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
     * Gets all of the Students from the database
     * @return  Collection of all the students
     */
    @Override
    public Collection<Student> values() {
        Collection<Student> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Student";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                Student student = get(rs.getInt("Student_number"));
                collection.add(student);
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
    public Set<Entry<String, Student>> entrySet() {
        throw new NullPointerException("Not implemented!"); //Makes no sense in this context but has to be implemented
    }
}