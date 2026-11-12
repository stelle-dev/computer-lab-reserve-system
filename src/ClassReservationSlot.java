package model;

public class ClassReservationSlot extends TimeSlot {

    private String courseCode;

    public ClassReservationSlot(String slotId,String startTime,String endTime,String courseCode)
    {
        super(slotId, startTime, endTime);
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public String getReservationType() {
        return "Class Reservation";
    }
}