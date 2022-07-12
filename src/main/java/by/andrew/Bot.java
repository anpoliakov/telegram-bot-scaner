package by.andrew;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class Bot extends TelegramLongPollingBot {
    private boolean enteringLoginPassword = false;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleMessage(Message message){
        if(message.hasText() && message.hasEntities()){
            enteringLoginPassword = false;

            //Получаю первую команду прописанную боту
            Optional<MessageEntity> commands = message.getEntities().stream()
                    .filter(e -> "bot_command".equals(e.getType())).findFirst();

            if(commands.isPresent()){
                String command = message.getText().
                        substring(commands.get().getOffset(), commands.get().getLength());

                switch (command){
                    case "/add_account":
                        enteringLoginPassword = true;
                        execute(
                            SendMessage.builder().
                                chatId(message.getChatId().toString()).
                                text(Constants.ENTER_LOGIN_AND_PASSWORD_RU).
                                build());
                    break;
                }
            }
        }

        //Если пользователь вводит логин и пароль
        if(message.hasText() && enteringLoginPassword == true && !message.hasEntities()){
            String[] data = message.getText().split(" ");
            System.out.println(data[0]);
            System.out.println(data[1]);
            enteringLoginPassword = false;
        }
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
