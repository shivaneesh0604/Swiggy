package library;


public abstract class User {
    private final String userID;
    private final String passWord;
    private final Role role;
    private final String name;

    public User(String userID, String passWord, Role role, String name) {
        this.userID = userID;
        this.passWord = passWord;
        this.role = role;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassWord() {
        return passWord;
    }

    public Role getRole() {
        return role;
    }

}
