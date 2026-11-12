package model;

public abstract class TimeSlot {

    protected String slotId;
    protected String startTime;
    protected String endTime;

    public TimeSlot(String slotId, String startTime, String endTime) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public abstract String getReservationType();
}