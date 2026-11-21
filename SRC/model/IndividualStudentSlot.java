package model;

public class IndividualStudentSlot extends TimeSlot {

    private String studentId;

    public IndividualStudentSlot(String slotId,String startTime,String endTime,String studentId) 
    {
        super(slotId, startTime, endTime);
        this.studentId = studentId;
    }
    public String getStudentId() {
        return studentId;
    }

    @Override
    public String getReservationType() {
        return "Individual Reservation";
    }
}