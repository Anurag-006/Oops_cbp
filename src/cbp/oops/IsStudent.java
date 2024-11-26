package cbp.oops;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class IsStudent {
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        boolean cont = true;
        int n = 0;
        System.out.println("Enter userName: ");
        String un = inp.nextLine();
        System.out.println("Enter password");
        String pa = inp.nextLine();

        String query = String.format("select password from student where id = '%s'",un);

        try(Connection con = ConnectToServer.connectToServer()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()){
                if (pa.compareTo(rs.getString(1)) == 0) {
                    do {
                        System.out.println("Enter ");
                        n = inp.nextInt();
                    } while (cont);
                }
            }
            else {
                System.out.println("Wrong Password");
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Wrong user name");
        }
    }
}
