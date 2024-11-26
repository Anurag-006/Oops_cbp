package cbp.oops;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectToServer {
    public static Connection connectToServer(){
        String url = "jdbc:postgresql://localhost:5432/Hostel";
        String username = "postgres";
        String pass = "2580";
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, pass);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return con;
    }
}