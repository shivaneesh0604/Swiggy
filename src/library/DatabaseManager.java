package library;

public class DatabaseManager {

    Database database = Database.getInstanceDatabase();
    private int userID =1000;
    public UserAddition addCustomer(String userName, String passWord, Role role, String name) {
        User user = new Customer("CUSTOMER_"+userID++,ApplicationFactory.getCustomerApplication(), role, name);
        User user1 = database.addUser(user,userName,passWord);
        if(user1!=null){
            return UserAddition.USER_ADDED;
        }
        else {
            return UserAddition.USERNAME_ALREADY_EXIST;
        }
    }

    public UserAddition addRider(String userName, String passWord, Role role, String name) {
        User user = new Rider("RIDER_"+userID++,ApplicationFactory.getRiderApplication(), role, name);
        User user1 = database.addUser(user,userName,passWord);
        if(user1!=null){
            return UserAddition.USER_ADDED;
        }
        else {
            return UserAddition.USERNAME_ALREADY_EXIST;
        }
    }

    public User getUser(String username, String password) {
        return database.getUser(username,password);
    }


}
