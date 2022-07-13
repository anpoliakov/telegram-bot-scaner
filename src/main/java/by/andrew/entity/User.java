package by.andrew.entity;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

public class User {
    private ArrayList <Account> accounts;

    private Long user_id;
    private Long chat_id;
    private String first_name;
    private String last_name;

    public User(Message message) {
        this.user_id = message.getFrom().getId();
        this.chat_id = message.getChatId();
        this.first_name = message.getFrom().getFirstName();
        this.last_name = message.getFrom().getLastName();
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
}
