package logic;

import model.ClassReservationSlot;
import model.InvalidSlotException;
import model.Lab;
import model.TimeSlot;

public class LogicTest {

    public static void main(String[] args) {

        ReservationManager manager = new ReservationManager();

        // ADDU Computer Labs (sample data)
        Lab lab1 = new Lab("LAB-F604", "F604", 40);
        Lab lab2 = new Lab("LAB-F605", "F605", 40);

        manager.addLab(lab1);
        manager.addLab(lab2);

        System.out.println("====================================");
        System.out.println("  ATENEO COMPUTER LAB RESERVATION");
        System.out.println("====================================");

        System.out.println("\nREGISTERED LABS");
        for (Lab lab : manager.getAllLabs()) {
            System.out.println(
                lab.getLabId() +
                " | Room: " + lab.getRoomNumber() +
                " | Capacity: " + lab.getCapacity()
            );
        }

        try {

            TimeSlot slot = new ClassReservationSlot(
                    "TS001",
                    "7:30 AM",
                    "9:00 AM",
                    "IT1233 - Computer Programming 2"
            );

            manager.createReservation(
                    "RES001",
                    lab1,
                    slot,
                    "Ernest Miguel Escuadra"
            );

            System.out.println("\nReservation created successfully!");

        } catch (InvalidSlotException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n===== DAILY REPORT =====");
        System.out.println(manager.generateDailyReport());

        if (manager.cancelReservation("RES001")) {
            System.out.println("Reservation cancelled successfully.");
        }

        System.out.println("Reservations Remaining: "
                + manager.getAllReservations().size());
    }
}   