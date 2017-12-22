package data;

import business.courses.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoomDao implements Map<String,Room> {

    private Connection conn;

    /**
     * Returns number of entries in the database
     * @return
     * @throws NullPointerException
     */
    @Override
    public int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            String sql= "SELECT count(*) FROM Room;";
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
     * @return
     */
    @Override
    public boolean isEmpty() {

        return size() == 0;
    }

    /**
     * Checks if a certain id exists in the database
     * @param key
     * @return
     * @throws NullPointerException
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try {
            conn = Connect.connect();
            String sql = "SELECT Room_code FROM Room WHERE Room_code=?;";
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


    public boolean containsValue(Object value) {
        return false;
    }

    /**
     * Gets a room from the database
     * @param key
     * @return
     */
    @Override
    public Room get(Object key) {
        Room room = null;
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Room WHERE Room_code=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (Integer)key);
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
     * @param key
     * @param value
     * @return
     */
    @Override
    public Room put(String key, Room value) {
        Room room = null;
        try {
            conn = Connect.connect();
            String sql = "INSERT INTO Room\n" +
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

    @Override
    public Room remove(Object key) {
        Room room = this.get(key);
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Room WHERE Room_code = ?;";
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
     * @param m
     */
    @Override
    public void putAll(Map<? extends String, ? extends Room> m) {
        for(Room s : m.values()) {
            put(s.getCode(), s);
        }
    }

    /**
     * Delete every Room from the database
     * @throws NullPointerException
     */
    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            String sql = "DELETE FROM Room;";
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
        throw new NullPointerException("Not implemented!"); //Não faz sentido mas tem que ser implementado
    }

    /**
     * Gets all of the rooms from the database
     * @return
     */
    @Override
    public Collection<Room> values() {
        Collection<Room> collection = new HashSet<>();
        try {
            conn = Connect.connect();
            String sql = "SELECT * FROM Room";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Room room = new Room(rs.getString("Room_code"),rs.getInt("Room_capacity"));
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
        throw new NullPointerException("Not implemented!"); //Não faz sentido mas tem que ser implementado
    }
}
