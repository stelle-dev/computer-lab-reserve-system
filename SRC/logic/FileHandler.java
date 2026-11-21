package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import model.ClassReservationSlot;
import model.IndividualStudentSlot;
import model.InvalidSlotException;
import model.Lab;
import model.Reservation;
import model.TimeSlot;

public class FileHandler {

    private static final String LABS_FILE = "labs.txt";
    private static final String RESERVATIONS_FILE = "reservations.txt";

    // Saves all labs and reservations to text files
    public static void saveData(ReservationManager manager) throws IOException {
        try (PrintWriter labWriter = new PrintWriter(new FileWriter(LABS_FILE))) {
            for (Lab lab : manager.getAllLabs()) {
                labWriter.println(
                    lab.getLabId() + "|" +
                    lab.getRoomNumber() + "|" +
                    lab.getCapacity()
                );
            }
        }

        try (PrintWriter resWriter = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation r : manager.getAllReservations()) {
                TimeSlot slot = r.getTimeSlot();
                String type;
                String detail;

                if (slot instanceof ClassReservationSlot) {
                    type = "CLASS";
                    detail = ((ClassReservationSlot) slot).getCourseCode();
                } else {
                    type = "INDIVIDUAL";
                    detail = ((IndividualStudentSlot) slot).getStudentId();
                }

                resWriter.println(
                    r.getReservationId() + "|" +
                    r.getLab().getLabId() + "|" +
                    type + "|" +
                    slot.getSlotId() + "|" +
                    slot.getStartTime() + "|" +
                    slot.getEndTime() + "|" +
                    detail + "|" +
                    r.getReservedBy()
                );
            }
        }
    }

    // Loads labs and reservations into manager; returns true if existing data was found
    public static boolean loadData(ReservationManager manager) throws IOException {
        File labsFile = new File(LABS_FILE);
        File resFile = new File(RESERVATIONS_FILE);

        if (!labsFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(labsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String labId = parts[0];
                    String room = parts[1];
                    int capacity = Integer.parseInt(parts[2]);
                    manager.addLab(new Lab(labId, room, capacity));
                }
            }
        }

        if (resFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(resFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 8) {
                        String resId = parts[0];
                        Lab lab = manager.findLabById(parts[1]);
                        String type = parts[2];
                        String slotId = parts[3];
                        String start = parts[4];
                        String end = parts[5];
                        String detail = parts[6];
                        String reservedBy = parts[7];

                        TimeSlot slot = type.equals("CLASS")
                            ? new ClassReservationSlot(slotId, start, end, detail)
                            : new IndividualStudentSlot(slotId, start, end, detail);

                        try {
                            manager.createReservation(resId, lab, slot, reservedBy);
                        } catch (InvalidSlotException ignored) {
                            // Skip invalid or duplicate entries in file
                        }
                    }
                }
            }
        }

        return !manager.getAllLabs().isEmpty();
    }
}