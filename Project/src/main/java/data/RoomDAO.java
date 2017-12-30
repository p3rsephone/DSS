package data;

import business.courses.Room;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class RoomDAO extends DAO implements Map<String,Room>  {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    @Override
    public int size() {
        return size("Room");
    }

    /**
     * Checks if database is empty
     * @return  True if the database is empty, false if it is not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty("Room");
    }

    /**
     * Checks if a certain Room code exists in the database
     * @param key                    Room code
     * @return                       True if the room is in the database
     * @throws NullPointerException  There is no connection
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Room_code FROM Ups.Room WHERE Room_code=?;";
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
     * Gets a Room from the database
     * @param key  Room code
     * @return     Room
     */
    @Override
    public Room get(Object key) {
        Room room = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Room WHERE Room_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                room = new Room(rs.getString("Room_code"),rs.getInt("Room_capacity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return room;
    }

    /**
     * Insert a new room in the database
     * @param key    Room code
     * @param value  Room
     * @return
     */
    @Override
    public Room put(String key, Room value) {
        Room room = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Ups.Room\n" +
                    "VALUES (?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE Room_capacity=VALUES(Room_capacity);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, value.getCode());
            ps.setInt(2, value.getCapacity());
            ps.executeUpdate();

            room = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return room;
    }

    /**
     * Removes a room from the database
     * @param key  Room code
     * @return     Room that was deleted
     */
    @Override
    public Room remove(Object key) {
        Room room = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Room WHERE Room_code = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String)key);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return room;
    }

    /**
     * Insert several Rooms into the database
     * @param m  Map of all the rooms
     */
    @Override
    public void putAll(Map<? extends String, ? extends Room> m) {
        for(Room s : m.values()) {
            put(s.getCode(), s);
        }
    }

    /**
     * Delete every Room from the database
     * @throws NullPointerException  No connection
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Ups.Room;";
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
            String sql = "SELECT Room_code FROM Ups.Room";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                set.add(rs.getString("Room_code"));
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
     * Gets all of the rooms from the database
     * @return  Collection of all the rooms
     */
    @Override
    public Collection<Room> values() {
        Collection<Room> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Ups.Room";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Room room = get(rs.getString("Room_code"));
                collection.add(room);
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
    public Set<Entry<String, Room>> entrySet() {
        Set<String> keys = new HashSet<>(this.keySet());

        HashMap<String, Room> map = new HashMap<>();
        for (String key : keys) {
            map.put(key, this.get(key));
        }
        return map.entrySet();
    }

    public HashMap<String, Room> list() {
        HashMap<String, Room> map = new HashMap<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT Room_code FROM Ups.Room";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room r = get(rs.getString("Course_code"));
                map.put(r.getCode(), r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return map;
    }
}
