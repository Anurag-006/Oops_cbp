package cbp.oops;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class IsStudent {
    public static boolean authenticate(String un, String pa){
        String query = String.format("select password from student where id = '%s'",un);

        try(Connection con = ConnectToServer.connectToServer()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()){
                if (pa.compareTo(rs.getString(1)) == 0) {
                    return true;
                }
            }
            else {
                System.out.println("Wrong Password");
                return false;
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Wrong user name");
        }
        return false;
    }

    public static void forStudent() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Enter userName: ");
        String un = inp.nextLine();
        System.out.println("Enter password");
        String pa = inp.nextLine();
        studentMenu(un, pa);
    }
    public static void studentMenu(String un, String pa) {
        boolean cont = true;
        int n = 0;
        Scanner inp = new Scanner(System.in);
        if (authenticate(un,pa)) {
            System.out.println("MENU FOR STUDENT");
            do{
                System.out.println("Enter : 1 for issuing complaint, 2 for paying fee, 3 for changing password, 4 for exit");
                n = inp.nextInt();
                switch (n) {
                    case 1: Student.issueComplaint(un);
                            break;
                    case 2: Student.payFee(un);
                            break;
                    case 3: Student.changePassword(un);
                            break;
                    case 4: break;
                    default:
                        System.out.println("Wrong choice");
                }
            }while(n != 4);
        }
    }
}
