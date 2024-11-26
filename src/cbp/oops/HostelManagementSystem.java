package cbp.oops;

import java.sql.SQLException;
import java.sql.SQLWarning;
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
        int paidAmount = inp.nextInt();
        String sqlQuery = String.format("update student set feepending = feepending - %d where id = '%s'",paidAmount, id);
        ReadAndRemoveRows.updateRow(sqlQuery);
    }
    public void changePassword(String pass) {

        this.password = pass;
        String query = String.format("update student set password = '%s' where id = '%s'", this.password, this.id);
    }
    public void complaint(String com, String jobTitle) {
        String query = String.format("insert in complaint values ('%s', '%s', FALSE)",com, jobTitle);
    }
}

//final class UG extends Student {
//    String degree = "UG";
//    int feePending;
//    UG(String name, int age, String id, String course, int yr) {
//        super(name, age, id, course, yr);
//        String sqlQuery = String.format("insert into student values ('%s', '%s', %d, '%s', %d, '%s',null, 0)",this.id, this.name, this.age, this.course, this.yr, this.degree);
//        ReadAndRemoveRows.addRow(sqlQuery);
//    }
//    public static void getAllStudents() {
//        String sqlQuery = "select * from student where degree like '%UG%'";
//        PrintRows.printRows(sqlQuery);
//    }
//}

//final class PG extends Student {
//    String degree = "PG";
//    int feePending;
//    PG(String name, int age, String id, String course, int yr) {
//        super(name, age, id, course, yr);
//        String sqlQuery = String.format("insert into student values ('%s', '%s', %d, '%s', %d, '%s',null, 0)",this.id, this.name, this.age, this.course, this.yr, this.degree);
//        ReadAndRemoveRows.addRow(sqlQuery);
//    }
//    public static void getAllStudents() {
//        String sqlQuery = "select * from student where degree like '%PG%'";
//        PrintRows.printRows(sqlQuery);
//    }
//}

//class Staff extends Person {
//    String jobTitle;
//    String name;
//
//    Staff(String name, String id, String jobTitle) {
//        super(name, id);
//        this.jobTitle = jobTitle;
//    }
//
//    public void getJobDetails() {
//        System.out.println("Job Title: " + jobTitle);
//    }
//
//}
//
//class Cleaning extends Staff {
//    Cleaning(String name, String id, String jobTitle) {
//        super(name, id, jobTitle);
//    }
//
//}
//
//class MessManager extends Staff {
//    MessManager(String name, String id, String jobTitle) {
//        super(name, id, jobTitle);
//    }
//}
//
//final class Manager extends Staff {
//    Manager(String name, String id, String jobTitle) {
//        super(name, id, jobTitle);
//    }
//
//}

//class Fee {
//    double amount;
//    String dueDate;
//
//    Fee(double amount, String dueDate) {
//        this.amount = amount;
//        this.dueDate = dueDate;
//    }
//
//    public void updateFee(double amount) {
//        this.amount = amount;
//    }
//
//    public void updateFee(double amount, String dueDate) {
//        this.amount = amount;
//        this.dueDate = dueDate;
//    }
//
//    public void getFeeDetails() {
//        System.out.println("Amount: " + amount + ", Due Date: " + dueDate);
//    }
//}

//class HostelFee extends Fee {
//    HostelFee(double amount, String dueDate) {
//        super(amount, dueDate);
//    }
//
//}
//
//class MessFee extends Fee {
//    MessFee(double amount, String dueDate) {
//        super(amount, dueDate);
//    }
//
//}
//
//class OtherFee extends Fee {
//    OtherFee(double amount, String dueDate) {
//        super(amount, dueDate);
//    }
//
//}

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

    public void addStudent(Student student) {
        if (this.capacity > this.studentCount) {
            student.feePending = this.roomFee;
            String sqlQuery = String.format("update student set room = '%s' where id = '%s'",this.roomNumber, student.id);
            ReadAndRemoveRows.updateRow(sqlQuery);
            sqlQuery = String.format("update student set feepending = %d where id = '%s'",student.feePending, student.id);
            ReadAndRemoveRows.updateRow(sqlQuery);
            this.studentCount++;
        }
        else {
            System.out.println("The room is already full.");
        }
    }

    public void getRoomDetails() {
        System.out.println("Room Number: " + roomNumber + ", Capacity: " + capacity);
    }

}

class Single extends Room {
    Single(int roomNumber) {
        super(roomNumber, 1, 10000);
    }

    public void getRoomDetails() {
        System.out.println("Single Sharing, Room Number: " + roomNumber + ", Capacity: " + capacity);
    }
}

class Double extends Room {
    Double(int roomNumber) {
        super(roomNumber, 2, 8000);
    }

    public void getRoomDetails() {
        System.out.println("Double Sharing, Room Number: " + roomNumber + ", Capacity: " + capacity);
    }
}

class Triple extends Room {
    Triple(int roomNumber) {
        super(roomNumber, 3, 6000);
    }

    public void getRoomDetails() {
        System.out.println("Triple Sharing, Room Number: " + roomNumber + ", Capacity: " + capacity);
    }
}

class Four extends Room {
    Four(int roomNumber) {
        super(roomNumber, 4, 5000);
    }

    public void getRoomDetails() {
        System.out.println("Four Sharing, Room Number: " + roomNumber + ", Capacity: " + capacity);
    }
}

//class Complaint {
//    String description;
//
//    Complaint(String description) {
//        this.description = description;
//    }
//
//    public void getComplaintDetails() {
//        System.out.println("Complaint: " + description);
//    }
//}

//class Maintenance extends Complaint {
//    Maintenance(String description, String date) {
//        super(description, date);
//    }
//
//}
//
//class Food extends Complaint {
//    Food(String description) {
//        super(description, );
//    }
//
//}
//
//class OtherComplaints extends Complaint {
//    OtherComplaints(String description, String date) {
//        super(description, date);
//    }
//
//}

class AdminClasses {

}

public class HostelManagementSystem {

    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);

//        System.out.println("MENU FOR HOSTEL STUFF");
//        int n = 0;
//        do{
//            System.out.println("Enter 1 for Student, 2 for Hostel Manager");
//            int user = inp.nextInt();
//            switch (user) {
//                case 1:
//            }
//            System.out.println("Enter : 1 for Inserting Student, 2 for Removing Student, 3 for Updating Fee, 4 for Updating Room Details, 5 for Available Slots, 6 for Fee Pending list, 7 for Complaints, 8 updating Complaints");
//            n = inp.nextInt();
//
//        }while(n != 5);
        Student ugStudent = new Student("Karthik", 1, "S03", "CSE", 2, "S03");
//        Student pgStudent = new Student("Vishal", 2, "S04", "CSD", 1);

//        Student.getDetails("S01");
//////
//        pgStudent.getDetails();

        Student.getAllStudents();

//        Cleaning cleaningStaff = new Cleaning("Murugan", 30, "ST01", "Cleaner");
//        MessManager messManager = new MessManager("Nagaraju", 35, "ST02", "Mess Manager");
//
//        cleaningStaff.getJobDetails();
//
//        messManager.getJobDetails();
//
        Single singleRoom = new Single(101);
        singleRoom.addStudent(ugStudent);

        Student.getAllStudents();
//        singleRoom.addStudent(pgStudent);

//        UG.getAllStudents();

        ugStudent.payFee(3000);

        Student.getAllStudents();
//        UG.getAllStudents();
//        PG.getAllStudents();
//        Double doubleRoom = new Double(201);
//
//        singleRoom.getRoomDetails();
//        doubleRoom.getRoomDetails();
//
//        HostelFee hostelFee = new HostelFee(5000, "20-09-2024");
//        MessFee messFee = new MessFee(2000, "20-09-2024");
//
//        hostelFee.updateFee(2000, "02-10-2024");
//        hostelFee.getFeeDetails();
//        messFee.updateFee(1000, "02-10-2024");
//        messFee.getFeeDetails();
//
//        Maintenance maintenanceComplaint = new Maintenance("Leaky faucet", "19-09-2024");
//        Food foodComplaint = new Food("Inadequate food quality", "19-09-2024");
//
//        maintenanceComplaint.getComplaintDetails();
//        foodComplaint.getComplaintDetails();
//        foodComplaint.updateComplaint("Bad taste", "05-10-2024");
    }
}