package by.andrew.entity;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

public class User {
    private ArrayList <Account> accounts;

    //user_id и chat_id обысно совподают, можно использовать одно из
    private Long user_id;
    private Long chat_id;
    private String first_name;
    private String last_name;

    public User(Message message) {
        this.user_id = message.getFrom().getId();
        this.chat_id = message.getChatId();
        this.first_name = message.getFrom().getFirstName();
        this.last_name = message.getFrom().getLastName();
        accounts = new ArrayList<>();
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Account account) {
        accounts.add(account);
    }

    public String showAccounts(){
        String text = "Аккаунты Kufar не добавлены";

        //если аакаунты присутствуют - добавляем их вместе default надписи
        if(!accounts.isEmpty()){
            StringBuilder allAccounts = new StringBuilder();
            for(Account acc : accounts){
                allAccounts.append(acc.toString());
            }
            text = allAccounts.toString();
        }

        return text;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", chat_id=" + chat_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
