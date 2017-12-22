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
    
    private static final String URL = "localhost";
    private static final String TABLE = "turma";
    private static final String USERNAME = "default"; //TODO: alterar
    private static final String PASSWORD = "password"; //TODO: alterar
    
    /**
     * Establishes a connection to the database
     * @return
     * @throws ClassNotFoundException 
     */
    public static Connection connect() throws ClassNotFoundException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mariadb://" + URL + "/" + TABLE + "?user=" + USERNAME + "&password=" + PASSWORD);
            //Client MUST close connection
            //"jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword"
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
