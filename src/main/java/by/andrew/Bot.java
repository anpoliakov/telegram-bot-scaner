package by.andrew;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class Bot extends TelegramLongPollingBot {
    private boolean isActiveInputLogin = false;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && isActiveInputLogin == false){
            handleMessage(update.getMessage());
        }

        if(update.hasMessage() && isActiveInputLogin == true){
            handleEnterLogin(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleMessage(Message message){
        if(message.hasText() && message.hasEntities()){
            String command = getCommand(message);

            switch (command){
                    case "/add_account":
                        isActiveInputLogin = true;
                        execute(
                            SendMessage.builder().
                                chatId(message.getChatId().toString()).
                                text(Constants.ENTER_LOGIN_AND_PASSWORD_RU).
                                build());
                    break;
            }
        }
    }

    private void handleEnterLogin(Message message){
        if(message.hasText()){
            System.out.println("В обработчик login & password:" + message.getText());
            isActiveInputLogin = false;

            String[] data = message.getText().split(" ");
            String login = data[0];
            String password = data[1];

            System.out.println("Введён логин:" + login);
            System.out.println("Введён пароль:" + password);

            Kufar kufar = new Kufar();
            kufar.login(login, password);
        }
    }

    //Подготавлявает команду переданную в чат
    private String getCommand(Message message){
        String command = null;
        Optional<MessageEntity> commands = message.getEntities().
                stream().filter(e -> "bot_command".equals(e.getType())).findFirst();

        if (commands.isPresent()) {
            command = message.getText().substring(commands.get().getOffset(), commands.get().getLength());
        }

        //TODO: случай когда команда не найдена и возвращаем null
        return command;
    }

    @Override
    public String getBotUsername() {
        return "scaner_icoer_bot";
    }

    @Override
    public String getBotToken() {
        return "5561545276:AAHDkqzcX4RF1cEm1SK74DNtSUy4kbeH4rw";
    }
}
