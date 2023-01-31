package library;


public abstract class User {
    private final Role role;
    private final String name;
    private final String userID;

    public User(String userID, Role role, String name) {
        this.userID = userID;
        this.role = role;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public Role getRole() {
        return role;
    }

}
