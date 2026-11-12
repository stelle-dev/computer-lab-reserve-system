package model;

public class Lab {

    private String labId;
    private String roomNumber;
    private int capacity;

    public Lab(String labId, String roomNumber, int capacity) {
        this.labId = labId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public String getLabId() {
        return labId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }
}