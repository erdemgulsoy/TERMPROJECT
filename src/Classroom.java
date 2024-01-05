public class Classroom {
    private String roomID ;
    private int capacity;

    public Classroom(String roomID, int capacity) {
        this.roomID = roomID;
        this.capacity = capacity;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
