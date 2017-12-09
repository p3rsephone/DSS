package main.business;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
 private static final String URL = "localhost";
 private static final String TABLE = "MusicLibrary";
 private static final String USERNAME = "teste";
 private static final String PASSWORD = "";


//  Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");


  public static Connection connect(){
   try{
    Class.forName("org.mariadb.jdbc.Driver");
    Connection cn = DriverManager.getConnection("jdbc:mariadb://"+URL+"/"+TABLE+"?user="+USERNAME+"&password="+PASSWORD);
    return  cn;
   }
   catch(Exception e){
    e.printStackTrace();
   }
   return null;
  }

  public static void close(Connection connection){
   try{
       connection.close();

   }catch (Exception e){
       e.printStackTrace();

   }
  }

}
