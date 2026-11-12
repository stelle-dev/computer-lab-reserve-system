package logic;

import model.Lab;
import model.TimeSlot;
import model.Reservation;
import model.InvalidSlotException;

import java.util.ArrayList;

public class ReservationManager {

    // Storage containers for active labs and reservations
    private ArrayList<Lab> labs;
    private ArrayList<Reservation> reservations;

    // Constructor initializing memory collections
    public ReservationManager() {
        this.labs = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    // ==========================================
    // 1. LAB MANAGEMENT METHODS
    // ==========================================

    public void addLab(Lab lab) {
        if (lab != null) {
            labs.add(lab);
        }
    }

    public Lab findLabById(String labId) {
        for (Lab lab : labs) {
            if (lab.getLabId().equalsIgnoreCase(labId)) {
                return lab; // Search method returning specific matching lab
            }
        }
        return null; // Return null if lab does not exist
    }

    public ArrayList<Lab> getAllLabs() {
        return this.labs; // Standard getter returning entire list
    }

    // ==========================================
    // 2. RESERVATION MANAGEMENT (POLYMORPHISM & CUSTOM EXCEPTION)
    // ==========================================

    public void createReservation(String reservationId, Lab lab, TimeSlot slot, String reservedBy) throws InvalidSlotException {
        // validate lab existence
        if (lab == null) {
            throw new InvalidSlotException("Cannot reserve slot: Specified lab does not exist.");
        }

        // validate slot availability (prevent double-booking)
        for (Reservation r : reservations) {
            if (r.getLab().getLabId().equalsIgnoreCase(lab.getLabId())) {
                if (r.getTimeSlot().getSlotId().equalsIgnoreCase(slot.getSlotId())) {
                    throw new InvalidSlotException("Slot " + slot.getSlotId() + " is already reserved in " + lab.getLabId());
                }
            }
        }

        // Polymorphism at work: Accepts parent type TimeSlot (ClassReservationSlot or IndividualStudentSlot)
        Reservation newReservation = new Reservation(reservationId, lab, slot, reservedBy);
        reservations.add(newReservation);
    }

    public boolean cancelReservation(String reservationId) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equalsIgnoreCase(reservationId)) {
                reservations.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Reservation> getAllReservations() {
        return this.reservations; // Standard getter returning all bookings
    }

    // ==========================================
    // 3. SEARCH & FILTERING METHODS (to be added)
    // ==========================================

    // ==========================================
    // 4. SUMMARY REPORT GENERATION
    // ==========================================

    public String generateDailyReport() {
        StringBuilder report = new StringBuilder();
        report.append("==========================================\n");
        report.append("     DAILY COMPUTER LAB USAGE REPORT      \n");
        report.append("==========================================\n");

        if (labs.isEmpty()) {
            report.append("No labs registered in the system.\n");
            return report.toString();
        }

        report.append("Total Labs Registered: ").append(labs.size()).append("\n");
        report.append("Total Active Reservations: ").append(reservations.size()).append("\n");
        report.append("------------------------------------------\n");

        for (Lab lab : labs) {
            // count total bookings 
            int labBookingCount = 0;
            for (Reservation r : reservations) {
                if (r.getLab().getLabId().equalsIgnoreCase(lab.getLabId())) {
                    labBookingCount++;
                }
            }

            report.append("Lab ID: ").append(lab.getLabId())
                  .append(" | Room: ").append(lab.getRoomNumber())
                  .append(" | Capacity: ").append(lab.getCapacity()).append("\n");
            report.append(" - Active Bookings: ").append(labBookingCount).append("\n");
            report.append("------------------------------------------\n");
        }

        return report.toString();
    }
}