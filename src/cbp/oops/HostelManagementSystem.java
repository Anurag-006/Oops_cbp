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

        System.out.println("Enter room preference: ");
        int rp = inp.nextInt();

        String query = String.format("select rno from room where rsize = %d and vacancies > 0", rp);
        String rno = "";
        try {
            Connection con = ConnectToServer.connectToServer();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rno = "";
            if (rs.next()) {
                rno = rs.getString(1);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        if (rno != "") {
            query = String.format("insert into student values ('%s', '%s', '%s', %d, %d, '%s', %d)", sid, sname, scourse, year, 135000, sid, Integer.parseInt(rno));
            ReadAndRemoveRows.addRow(query);

            query = String.format("update room set vacancies = vacancies - 1 where rno = %d", Integer.parseInt(rno));
            ReadAndRemoveRows.updateRow(query);

            query = "select * from student";
            System.out.println("Student Added.");
            PrintRows.printRows(query);
        } else {
            System.out.println("Unable to find the room.");
        }
    }
    public static void removeStudent() {
        System.out.println("Enter student id: ");
        Scanner inp = new Scanner(System.in);
        String sid = inp.nextLine();
        String query = String.format("update room set vacancies = vacancies + 1 where rno = (select rno from student where id = '%s')", sid);
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
        String query = "update complaint set cstatus = true where cstatus = false";
        ReadAndRemoveRows.updateRow(query);
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