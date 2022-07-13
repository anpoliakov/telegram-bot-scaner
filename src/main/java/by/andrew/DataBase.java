package by.andrew;

import by.andrew.entity.User;

import java.util.HashMap;

public class DataBase {
    private HashMap <Long, User> dataBaseUsers = new HashMap <>();

    private static DataBase INSTANCE = null;
    public DataBase() {}

    //TODO: Улчучшить instance
    public static DataBase getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataBase();
            return INSTANCE;
        }else{
            return INSTANCE;
        }
    }

    public User getUserByID(Long user_id){
        return dataBaseUsers.get(user_id);
    }

    public void addUserInDataBase(User user){
        dataBaseUsers.put(user.getUser_id(), user);
    }
}
