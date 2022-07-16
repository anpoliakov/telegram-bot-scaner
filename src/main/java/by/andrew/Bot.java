package by.andrew;

import by.andrew.entity.PreparerKeyboard;
import by.andrew.entity.User;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private PreparerKeyboard preparerKeyboard = new PreparerKeyboard();
    private StatusBot CURRENT_STATE = StatusBot.START;
    private DataBase db = DataBase.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            handle(update.getMessage());
        }

        Message message = update.getMessage();

        switch (CURRENT_STATE){
            case DEFAULT:
                System.out.println("МЕНЮ DEFAULT");

                SendMessage answer = new SendMessage();
                ReplyKeyboardMarkup keyboard = preparerKeyboard.getKeyboardByState(CURRENT_STATE);
                answer.setChatId(update.getMessage().getChatId().toString());
                answer.setText("Выберите действие:");
                answer.setReplyMarkup(keyboard);
                try {
                    execute(answer);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            break;

            case LOGIN:
                System.out.println("МЕНЮ LOGIN");
                String text = update.getMessage().getText();

                if(!text.toLowerCase().equals("добавить аккаунт")){
                    String[] loginData = update.getMessage().getText().split(" ");
                    String login = loginData[0];
                    String password = loginData[1];

                    System.out.println("Введён логин:" + login);
                    System.out.println("Введён пароль:" + password);

                    Kufar kufar = new Kufar();
                    boolean isLogin = kufar.login(login, password);

                    SendMessage answerStatusAuth = new SendMessage();
                    answerStatusAuth.setChatId(update.getMessage().getChatId().toString());

                    if(isLogin){
                        answerStatusAuth.setText(Constants.SUCCESS_AUTH);
                        CURRENT_STATE = StatusBot.DEFAULT;
                    }else{
                        answerStatusAuth.setText(Constants.ERROR_AUTH);
                    }

                    try {
                        execute(answerStatusAuth);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            break;

            case SHOW_ACC:
                System.out.println("МЕНЮ SHOW_ACC");

                SendMessage answerShowAcc = new SendMessage();
                ReplyKeyboardMarkup keyboardShowAcc = preparerKeyboard.getKeyboardByState(CURRENT_STATE);
                User userShowAcc = db.getUserByID(message.getFrom().getId());

                answerShowAcc.setText(userShowAcc.showAccounts());
                answerShowAcc.setChatId(String.valueOf(message.getChatId()));
                answerShowAcc.setReplyMarkup(keyboardShowAcc);

                try {
                    execute(answerShowAcc);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            break;
        }
    }

    @SneakyThrows
    private void handle(Message message){
        if(message.hasText()){
            //предподготовка введённого сообщения
            String command = message.getText().toLowerCase().trim();

            switch (command){
                case "/start":
                    CURRENT_STATE = StatusBot.DEFAULT;

                    Long user_id = message.getFrom().getId();
                    User user = db.getUserByID(user_id);
                    if(user == null){
                        user = new User(message);
                        db.addUserInDataBase(user);
                    }
                    System.out.println("/start от: " + db.getUserByID(user_id).toString());
                break;

                case "добавить аккаунт":
                    CURRENT_STATE = StatusBot.LOGIN;

                    SendMessage answerAddAcc = new SendMessage();
                    ReplyKeyboardMarkup keyboard = preparerKeyboard.getKeyboardByState(CURRENT_STATE);
                    answerAddAcc.setChatId(String.valueOf(message.getChatId()));
                    answerAddAcc.setText(Constants.ENTER_LOGIN_AND_PASSWORD_RU);
                    answerAddAcc.setReplyMarkup(keyboard);
                    try {
                        execute(answerAddAcc);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                break;

                case "аккаунты":
                    CURRENT_STATE = StatusBot.SHOW_ACC;
                break;

                case "отмена":
                    CURRENT_STATE = StatusBot.DEFAULT;
                break;
            }
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
