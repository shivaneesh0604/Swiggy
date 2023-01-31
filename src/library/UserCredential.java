package library;

public class UserCredential {
    private final String userName;
    private final String passWord;
    private final String userID;

    public UserCredential(String userName, String passWord, String userID) {
        this.userName = userName;
        this.passWord = passWord;
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getUserID() {
        return userID;
    }

}
