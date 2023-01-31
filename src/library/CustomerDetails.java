package library;

public class CustomerDetails {
    private final String location;
    private final String userID;

    public CustomerDetails(String location, String userID) {
        this.location = location;
        this.userID = userID;
    }

    public String getLocation() {
        return location;
    }

    public String getUserID() {
        return userID;
    }

}
