package data;

import business.courses.Request;

import java.sql.*;
import java.util.*;

public class RequestDAO extends DAO implements Map<String,ArrayList<Request>> {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    @Override
    public int size() {
        return size("Request");
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty("Request");
    }

    /**
     * Checks if a there are request of a certain course in the database
     * @param key                    Course code
     * @return                       True if the request is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Request_destShift FROM Ups.Request WHERE Request_destShift=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String) key);
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
     * Gets an arraylist of Requests from the database
     * @param key  Request destination shifts
     * @return     Arraylist of requests
     */
    @Override
    public ArrayList<Request> get(Object key) {
        Request request;
        ArrayList<Request> array = new ArrayList<>();
        try {
            conn = Connect.connect();

            String sql = "SELECT Student_number FROM Ups.RequestStudent AS RS\n" +
                    "JOIN Ups.Request AS R ON R.Request_code=RS.Request_code\n" +
                    "WHERE R.Request_destShift=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            ResultSet rstudent;
            while (rs.next()) {
                request = new Request(rs.getInt("Request_code"), rs.getInt("Student_number"),rs.getString("Course_code"), rs.getString("Request_originShift"), rs.getString("Request_destShift"));
                array.add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return array;
    }

    /**
     * Insert a new request in the database
     * @param key    Request code
     * @param value  Request
     * @return
     */
    @Override
    public ArrayList<Request> put(String key, ArrayList<Request> value) {
        ArrayList<Request> aRequest = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Request\n" +
                         "VALUES (?, ?, ?, ?)\n" +
                         "ON DUPLICATE KEY UPDATE Request_originShift=VALUES(Request_originShift), Course_code=VALUES(Course_code);";
            String sqlStudent = "INSERT INTO Ups.RequestStudent (Request_code, Student_number)\n"+
                                "VALUES (?, ?)\n" +
                                "ON DUPLICATE KEY UPDATE Student_number=VALUES(Student_number);";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psStudent = conn.prepareStatement(sqlStudent, Statement.RETURN_GENERATED_KEYS);
            for (Request r : value) {
                ps.setInt(1, r.getCode());
                ps.setString(2, r.getOriginShift());
                ps.setString(3, r.getDestShift());
                ps.setString(4,r.getCourse());
                ps.executeUpdate(); //Adds all the requests

                psStudent.setInt(1,r.getCode());
                psStudent.setInt(2, r.getStudent());
                psStudent.executeUpdate(); //Associates all the requests to students
            }
            aRequest = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return aRequest;
    }

    /**
     * Removes an array of Requests from the database
     * @param key  Requests destination shift
     * @return     Requests that were deleted
     */
    @Override
    public ArrayList<Request> remove(Object key) {
        ArrayList<Request> aRequest = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "SELECT Request_code FROM Ups.Request WHERE Request_destShift = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sql = "DELETE FROM Ups.RequestStudent WHERE Request_code = ?;";
                ps =conn.prepareStatement(sql);
                ps.setInt(1,rs.getInt("Request_code"));
                ps.executeUpdate();
            }

            sql = "DELETE FROM Ups.Request WHERE Request_destShift = ?;";
            ps.setString(1, (String) key);
            ps.executeUpdate();


        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return aRequest;
    }

    /**
     * Insert several Requests into the database
     * @param m  Map of all the requests
     */
    @Override
    public void putAll(Map<? extends String, ? extends ArrayList<Request>> m) {
        for(ArrayList<Request> r : m.values()) {
            put(r.get(1).getDestShift(), r);
        }
    }

    /**
     * Delete every Request from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Request;DELETE FROM Ups.RequestStudent;";
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
     * Gets all of the requests from the database
     * @return  Collection of all the requests
     */
    @Override
    public Collection<ArrayList<Request>> values() {
        Collection<ArrayList<Request>> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT DISTINCT Request_destShift FROM Ups.Request";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                ArrayList<Request> aRequest = get(rs.getString("Request_destShift"));
                collection.add(aRequest);
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
    public Set<Entry<String, ArrayList<Request>>> entrySet() {
        throw new NullPointerException("Not implemented!"); //Makes no sense in this context but has to be implemented
    }

    public HashMap<String, ArrayList<Request>> getRequestsFromCourse(Object key) {
        HashMap<String, ArrayList<Request>> collection = new HashMap<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT Student_number, R.Request_code, R.Request_destShift, R.Request_originShift, R.Course_code  FROM Ups.RequestStudent AS RS\n" +
                                "JOIN Ups.Request AS R ON R.Request_code=RS.Request_code\n" +
                                "WHERE R.Course_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,(String) key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Request request;
                    request = new Request(rs.getInt("Request_code"), rs.getInt("Student_number"), rs.getString("Course_code"), rs.getString("Request_originShift"), rs.getString("Request_destShift"));
                    collection.putIfAbsent(rs.getString("Request_destShift"), new ArrayList<Request>());
                    collection.get(rs.getString("Request_destShift")).add(request);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return collection;
    }

    public void removeFromCourse(Object key) {
        try {
            conn = Connect.connect();
            String sql = "SELECT Request_code FROM Ups.Request WHERE Course_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sql = "DELETE FROM Ups.RequestStudent WHERE Request_code = ?;";
                ps =conn.prepareStatement(sql);
                ps.setInt(1,rs.getInt("Request_code"));
                ps.executeUpdate();
            }

            sql = "DELETE FROM Ups.Request WHERE Course_code = ?;";
            ps.setString(1, key.toString());
            ps.executeUpdate();


        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    public HashMap<String, ArrayList<Integer>> getPendingRequestsFromStudent(Object key) {
        HashMap<String, ArrayList<Integer>> collection = new HashMap<>();
        try {
            conn = Connect.connect();
            String sql="SELECT R.Request_code, R.Request_originShift FROM Ups.Request AS R\n" +
                    "JOIN Ups.RequestStudent AS RS ON R.Request_code=RS.Request_code\n" +
                    "WHERE RS.Student_number=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,(Integer)key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                    collection.putIfAbsent(rs.getString("Request_originShift"), new ArrayList<>());
                    collection.get(rs.getString("Request_originShift")).add(rs.getInt("Request_code"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return collection;
    }

    public void removeRequest(Object key) {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.RequestStudent WHERE Request_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(key.toString()));
            ps.executeUpdate();

            sql = "DELETE FROM Ups.Request WHERE Request_code = ?;";
            ps =conn.prepareStatement(sql);
            ps.setInt(1,Integer.parseInt(key.toString()));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

}
