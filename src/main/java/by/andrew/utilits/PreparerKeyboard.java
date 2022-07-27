package by.andrew.utilits;

import by.andrew.StatusBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

//класс который предоставляет нужную клавиатуру взависимости от состояния программы
public class PreparerKeyboard {

    public ReplyKeyboardMarkup getKeyboardByStatus(StatusBot statusBot){
        ReplyKeyboardMarkup replyKeyboard = null;

        switch (statusBot){
            case DEFAULT:
                replyKeyboard = getDefaultKeyboard();
            break;

            case LOGIN:
                replyKeyboard = getCancelKeyboard();
            break;

            case SHOW_ACC:
                replyKeyboard = getCancelKeyboard();
            break;
        }

        return replyKeyboard;
    }

    private ReplyKeyboardMarkup getDefaultKeyboard(){
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setSelective(true);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Добавить аккаунт"));
        keyboardFirstRow.add(new KeyboardButton("Аккаунты"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Добавить объявление"));
        keyboardSecondRow.add(new KeyboardButton("Объявления"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboard.setKeyboard(keyboard);

        return replyKeyboard;
    }

    private ReplyKeyboardMarkup getCancelKeyboard(){
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setSelective(true);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Отмена"));
        keyboard.add(keyboardFirstRow);
        replyKeyboard.setKeyboard(keyboard);

        return replyKeyboard;
    }
}
