package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DAO {

    /**
     * Returns number of entries in the database
     * @return                       Number of entries
     * @throws NullPointerException  No connection
     */
    public int size(String table) {
        int i = 0;
        Connection conn = null;
        try {
            conn = Connect.connect();
            String sql= "SELECT count(*) FROM Ups." + table;
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
    public boolean isEmpty(String table) {

        return size(table) == 0;
    }
}
