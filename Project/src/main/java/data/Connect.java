/*
 * Connect
 * ruicouto in 10/dez/2015
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe que gere ligações à base de dados
 * @author ruicouto
 */
public class Connect {   
    
    private static final String URL = "jdbc:mysql://localhost:3306"; //"jdbc:mariadb://localhost:3306
    //private static final String TABLE = "Ups";
    private static final String USERNAME = ""; //TODO: alterar
    private static final String PASSWORD = ""; //TODO: alterar
    
    /**
     * Establishes a connection to the database
     * @return
     * @throws ClassNotFoundException 
     */
    public static Connection connect() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            //Class.forName("org.mariadb.jdbc.Driver");
            //Connection cn = DriverManager.getConnection("jdbc:mariadb://" + URL + "/" + TABLE + "?user=" + USERNAME + "&password=" + PASSWORD);
            return cn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * If open, closes the database connection
     * @param c 
     */
    public static void close(Connection c) {
        try {
            if(c!=null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
