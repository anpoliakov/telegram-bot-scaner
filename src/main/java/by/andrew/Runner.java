package by.andrew;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//Точка входа в прогрумму
public class Runner {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
            System.out.println("########### BOT IS WORKING! ###########");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}