package cbp.oops;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class IsAdmin{
    public static boolean authenticate(String un, String pa){
        if ((un.compareTo("root") == 0)&& (pa.compareTo("root") == 0)) {
            return true;
        }
        return false;
    }

    public static void forAdmin() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Enter userName: ");
        String un = inp.nextLine();
        System.out.println("Enter password");
        String pa = inp.nextLine();
        adminMenu(un, pa);
    }
    public static void adminMenu(String un, String pa) {
        boolean cont = true;
        int n = 0;
        Scanner inp = new Scanner(System.in);
        if (authenticate(un,pa)) {
            System.out.println("MENU FOR HOSTEL STUFF");
            do{
                System.out.println("Enter : 1 for Inserting Student, 2 for Removing Student, 3 for Available Slots, 4 for Fee Pending list, 5 for Resolving Complaints, 6 for Displaying students in a room, 7 for exit");
                n = inp.nextInt();
                switch (n) {
                    case 1: Admin.insertStudent();
                        break;
                    case 2: Admin.removeStudent();
                        break;
                    case 3: Admin.availableSlots();
                        break;
                    case 4: Admin.feePendingList();
                        break;
                    case 5: Admin.resolveComplaints();
                        break;
                    case 6: Admin.viewRoomDetails();
                        break;
                    case 7: break;
                    default:
                        System.out.println("Wrong choice");
                }
            }while(n != 7);
        }
    }
}