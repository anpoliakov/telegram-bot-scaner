package by.andrew;

import by.andrew.entity.Account;
import by.andrew.entity.User;
import by.andrew.utilits.Kufar;
import by.andrew.utilits.PreparerKeyboard;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    //Содержит текущее состояние бота у пользователя
    private StatusBot CURRENT_STATUS = StatusBot.START;

    //Обьект для предоставления меню в зависимости CURRENT_STATE
    private PreparerKeyboard preparerKeyboard = new PreparerKeyboard();

    //База данных (в будущем SQL) для хранения всей необходимой информации (пользователи и их аккаунты)
    private DataBase db = DataBase.getInstance();

    //Обьект для работы с сайтом kufar
    private Kufar kufar = new Kufar();

    //Содержит ID текущего user
    private static Long IDUser;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            //TODO: перелопатить код и вставить IDUSER вместе прочего (содаётся отдельный поток для каждого запроса)
            IDUser = update.getMessage().getFrom().getId();
            determineStatusChatBot(update.getMessage());
            executeByStatus(update);
        }
    }

    @SneakyThrows
    private void determineStatusChatBot(Message message){
        if(message.hasText()){
            //предподготовка введённого сообщения
            String command = message.getText().toLowerCase().trim();

            switch (command){
                case "/start":
                    CURRENT_STATUS = StatusBot.DEFAULT;

                    Long user_id = message.getFrom().getId();
                    User user = db.getUserByID(user_id);
                    if(user == null){
                        user = new User(message);
                        db.addUserInDataBase(user);
                    }
                    System.out.println("/start от: " + db.getUserByID(user_id).toString());
                break;

                case "добавить аккаунт":
                    CURRENT_STATUS = StatusBot.LOGIN;

                    SendMessage answerAddAcc = new SendMessage();
                    ReplyKeyboardMarkup keyboard = preparerKeyboard.getKeyboardByStatus(CURRENT_STATUS);
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
                    CURRENT_STATUS = StatusBot.SHOW_ACC;
                break;

                case "объявления":
                    CURRENT_STATUS = StatusBot.SHOW_ADS;
                break;

                case "добавить объявление":
                    //TODO: добавить объявление - в разработке, потом поменять статус на  StatusBot.CREATE_ADS;
                    CURRENT_STATUS = StatusBot.DEFAULT;
                    SendMessage answerAddAds = new SendMessage();
                    answerAddAds.setChatId(String.valueOf(message.getChatId()));
                    answerAddAds.setText(">Раздел 'Добавить объявление' в разработке<");
                    try {
                        execute(answerAddAds);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                 break;

                case "отмена":
                    CURRENT_STATUS = StatusBot.DEFAULT;
                break;
            }
        }
    }

    private void executeByStatus(Update update){
        Message message = update.getMessage();

        if(message.hasText()){
            switch (CURRENT_STATUS){
                case DEFAULT:
                    System.out.println("МЕНЮ DEFAULT");

                    SendMessage answer = new SendMessage();
                    ReplyKeyboardMarkup keyboard = preparerKeyboard.getKeyboardByStatus(CURRENT_STATUS);
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
                        Account account = kufar.login(login, password);

                        SendMessage answerStatusAuth = new SendMessage();
                        answerStatusAuth.setChatId(update.getMessage().getChatId().toString());

                        if(account != null){
                            answerStatusAuth.setText(Constants.SUCCESS_AUTH);
                            db.addAccountForUser(message.getChatId(), account);
                            CURRENT_STATUS = StatusBot.DEFAULT;
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
                    ReplyKeyboardMarkup keyboardShowAcc = preparerKeyboard.getKeyboardByStatus(CURRENT_STATUS);
                    User userShowAcc = db.getUserByID(message.getFrom().getId());

                    answerShowAcc.setText(userShowAcc.showAccounts().toString());
                    answerShowAcc.setChatId(String.valueOf(message.getChatId()));
                    answerShowAcc.setReplyMarkup(keyboardShowAcc);

                    try {
                        execute(answerShowAcc);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                break;

                //показывает обьявления пользователя
                case SHOW_ADS:
                    Long id_user = update.getMessage().getFrom().getId();
                    //получаю пользователя user пользователя (хранит аккаунты и свединья)
                    User user = db.getUserByID(id_user);

                    SendMessage answerShowAds = new SendMessage();
                    answerShowAds.setChatId(id_user.toString());
                    StringBuilder textAds = new StringBuilder("Для работы с объявлениями - подключите аккаунты");

                    //Если пользователь подключил аккаунты
                    ArrayList<Account> accounts = user.getAccounts();
                    if(accounts != null && !user.getAccounts().isEmpty()){
                        textAds.delete(0,textAds.length());
                        textAds.append("Ваши аккаунты и существующие объявления:\n");

                        //TODO доделать обработку и вывод информации об обьявлениях в бот!!
                        for(Account account : accounts){
                            textAds.append("Имя аккаунта: " + account.getName()+"\n");
                            textAds.append("Обьявления: " + kufar.getAllAds(account));
                        }
                    }

                    answerShowAds.setText(textAds.toString());

                    try {
                        execute(answerShowAds);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                break;
            }
        }
    }

    public static Long getCurrentIDUser(){
        return IDUser;
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
