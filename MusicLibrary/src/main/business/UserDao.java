package main.business;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserDao implements Map<String, User>{
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public User get(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User put(String key, User value) {
        Connection cn = Connect.connect();
        if(cn!=null){
            try{
                String sql = "INSERT INTO User(email,password) VALUES" + " (?, ?) ";
                PreparedStatement ps = cn.prepareStatement(sql);
                ps.setString(1, value.getEmail());
                ps.setString(2, value.getPassword());
                ps.executeUpdate();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return value;
    }

    @Override
    public User remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends User> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<User> values() {
        Collection<User> res = new ArrayList<User>();
        Connection cn = Connect.connect();
        if (cn != null){
            try {

                String sql = "SELECT * FROM User;";
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setEmail((rs.getString("email")));
                    u.setPassword((rs.getString("password")));
                    res.add(u);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  res;
    }

    @Override
    public Set<Entry<String, User>> entrySet() {
        return null;
    }


}
