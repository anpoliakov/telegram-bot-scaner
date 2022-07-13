package by.andrew;

public class DataBase {
    private Arra

    private static DataBase instance = null;
    public DataBase() {}

    public static DataBase getInstance(){
        if(instance == null){
            instance = new DataBase();
            return instance;
        }else{
            return instance;
        }
    }
}
