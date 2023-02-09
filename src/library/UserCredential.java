package library;

class UserCredential {
    private final String userName;
    private final String passWord;

    UserCredential(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

}
