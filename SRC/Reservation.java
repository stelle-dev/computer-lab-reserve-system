package model;

public class Reservation {

    private String reservationId;
    private Lab lab;
    private TimeSlot timeSlot;
    private String reservedBy;

    public Reservation(String reservationId,
                       Lab lab,
                       TimeSlot timeSlot,
                       String reservedBy) {

        this.reservationId = reservationId;
        this.lab = lab;
        this.timeSlot = timeSlot;
        this.reservedBy = reservedBy;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Lab getLab() {
        return lab;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public String getReservedBy() {
        return reservedBy;
    }
}