package library;

class CustomerDetails {
    private final Location location;
    private final String userID;

    public CustomerDetails(Location location, String userID) {
        this.location = location;
        this.userID = userID;
    }

    public Location getLocation() {
        return location;
    }

    public String getUserID() {
        return userID;
    }

}
