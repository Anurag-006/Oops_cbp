package cbp.oops;

import java.sql.SQLException;
import java.sql.*;
import java.sql.Statement;
import java.util.Scanner;

class Person {
    String name, id;


    Person(String name, String id) {
        this.name = name;
        this.id = id;
    }
}

class Student extends Person {
    String course;
    int yr;
    int feePending;
    String password;

    Student(String name, int age, String id, String course, int yr, String password) {
        super(name, id);
        this.yr = yr;
        this.course = course;
        this.password = password;
        this.feePending = 0;
            String sqlQuery = String.format("insert into student values ('%s', '%s', '%s', %d, null, %d, %s)", this.id, this.name, this.course, this.yr, this.feePending, this.password);
            ReadAndRemoveRows.addRow(sqlQuery);
    }
    public static void getDetails(String studentId) {
//        System.out.println("Name: " + super.name + ", Year: " + yr + ", ID: " + super.id);
        String sqlQuery = String.format("select * from student where id = '%s'",studentId);
        PrintRows.printRows(sqlQuery);
    }
    public static void getAllStudents() {
        String sqlQuery = "select * from student";
        PrintRows.printRows(sqlQuery);
    }
    public static void payFee(String id) {
        System.out.println("Enter amount");
        Scanner inp = new Scanner(System.in);
        int fees = 0;
        String query = String.format("select feepending from student where id = '%s'",id);
        try {
            Connection con = ConnectToServer.connectToServer();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            fees = 0;
            if (rs.next()) {
                fees = Integer.parseInt(rs.getString(1));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }


        int paidAmount = inp.nextInt();
        if (paidAmount > fees) {
            System.out.println("Amount can not be more than the pending fee.");
        }
        else {
            String sqlQuery = String.format("update student set feepending = feepending - %d where id = '%s'", paidAmount, id);
            ReadAndRemoveRows.updateRow(sqlQuery);
            System.out.println("Fee paid.");
        }
    }
    public static void changePassword(String id) {
        System.out.println("Enter new Password");
        Scanner inp = new Scanner(System.in);
        String pass = inp.nextLine();
        String sqlQuery = String.format("update student set password = '%s' where id = '%s'", pass, id);
        ReadAndRemoveRows.updateRow(sqlQuery);
        System.out.println("Password updated.");
    }
    public static void issueComplaint(String id) {
        System.out.println("Enter Complaint");
        Scanner inp = new Scanner(System.in);
        String com = inp.nextLine();
        System.out.println("Enter department");
        String jobTitle = inp.nextLine();
        String sqlQuery = String.format("insert into complaint values ('%s', FALSE, '%s')",com, jobTitle);
        ReadAndRemoveRows.updateRow(sqlQuery);
        System.out.println("Complaint issued.");
    }
}


class Admin extends Person {
    String name;

    Admin(String name, String id) {
        super(name, id);
    }

    public static void insertStudent() {
        System.out.println("Enter student id: ");
        Scanner inp = new Scanner(System.in);
        String sid = inp.nextLine();
        System.out.println("Enter Student name: ");
        String sname = inp.nextLine();
        System.out.println("Enter course: ");
        String scourse = inp.nextLine();
        System.out.println("Enter year: ");
        int year = inp.nextInt();
        inp.nextLine();

        System.out.println("Enter room preference: (1 : Single Room, 2 : Two Sharing, 3: Three Sharing, 4: Four Sharing)\n");
        int rp = inp.nextInt();
        inp.nextLine();

        String query = String.format("select rno from room where rsize = %d and vacancies > 0", rp);
        String rno = null;
        try {
            Connection con = ConnectToServer.connectToServer();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                rno = rs.getString(1);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if (rno != null) {
            int roomFee = 0;
            switch (rp) {
                case 1:
                    roomFee = 10000;
                    break;
                case 2:
                    roomFee = 9000;
                    break;
                case 3:
                    roomFee = 8000;
                    break;
                case 4:
                    roomFee = 7000;
                    break;
                default:
                    System.out.println(rp + " sharing not available.");
            }

            String insertQuery = String.format("insert into student values ('%s', '%s', '%s', %d, %d, '%s', %d)", sid, sname, scourse, year, roomFee, sid, Integer.parseInt(rno));
            String updateQuery = String.format("update room set vacancies = vacancies - 1 where rno = %d", Integer.parseInt(rno));

            try (Connection con = ConnectToServer.connectToServer()) {
                con.setAutoCommit(false);

                try (Statement stmt1 = con.createStatement();
                     Statement stmt2 = con.createStatement()) {

                    stmt1.executeUpdate(insertQuery);

                    stmt2.executeUpdate(updateQuery);

                    con.commit();
                    System.out.println("Student added and room allocated.");
                } catch (SQLException e) {
                    con.rollback();
                    e.printStackTrace();
                    System.out.println("Error adding student or updating room. Transaction rolled back.");
                }

                query = "select * from student";
                System.out.println("Student Added.");
                PrintRows.printRows(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database connection error.");
            }

        } else {
            System.out.println("No rooms available as per your preference.");
        }
    }
    public static void removeStudent() {
        System.out.println("Enter student id: ");
        Scanner inp = new Scanner(System.in);
        String sid = inp.nextLine();
        String query = String.format("update room set vacancies = vacancies + 1 where rno = (select room from student where id = '%s')", sid);
        ReadAndRemoveRows.updateRow(query);
        query = String.format("delete from student where id = '%s'",sid);
        ReadAndRemoveRows.removeRow(query);
        query = "select * from student";
        System.out.println("Student Removed.");
        PrintRows.printRows(query);
    }

    public static void availableSlots() {
        String query = "select rno, vacancies from room where vacancies != 0";
        PrintRows.printRows(query);
    }
    public static void feePendingList() {
        String query = "select id, name, course, feepending from student where feepending != 0";
        PrintRows.printRows(query);
    }
    public static void resolveComplaints() {

        Scanner inp = new Scanner(System.in);
        System.out.println("Enter Department name: ");
        String dept = inp.nextLine();

        String query = String.format("update complaint set cstatus = true where cname = (select cname from complaint where department = '%s' and cstatus = false limit 1)", dept);
        ReadAndRemoveRows.updateRow(query);
    }
    public static void viewRoomDetails() {
        System.out.println("Enter room number: ");
        Scanner inp = new Scanner(System.in);
        int rno = inp.nextInt();
        String query = String.format("select * from student where room = %d",rno);
        PrintRows.printRows(query);
    }
}

class Room {
    int roomNumber;
    int capacity;
    int roomFee;

    int studentCount = 0;

    Room(int roomNumber, int capacity, int roomFee) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomFee = roomFee;
    }
}

public class HostelManagementSystem {

    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);

        System.out.println("MENU FOR HOSTEL STUFF");
        int n = 0;
        boolean cont = true;
        do {
            System.out.println("Enter 1 for Student, 2 for Admin, 3 for exit");
            int user = inp.nextInt();
            switch (user) {
                case 1:
                    IsStudent.forStudent();
                    break;
                case 2:
                    IsAdmin.forAdmin();
                    break;
                case 3: cont = false;
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        }while(cont);
    }
}