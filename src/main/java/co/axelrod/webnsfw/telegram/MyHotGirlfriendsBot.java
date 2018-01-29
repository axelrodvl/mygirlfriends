package co.axelrod.webnsfw.telegram;

import co.axelrod.webnsfw.telegram.token.TokenStorageImpl;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 29.01.2018.
 */
public class MyHotGirlfriendsBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            System.out.println("New request from Telegram bot with id: " + chatId);


            // Create ReplyKeyboardMarkup object
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            // Create the keyboard (list of keyboard rows)
            List<KeyboardRow> keyboard = new ArrayList<>();
            // Create a keyboard row
            KeyboardRow row = new KeyboardRow();
            // Set each button, you can also use KeyboardButton objects if you need something else than text
            row.add("Хочу горячего");
            // Add the first row to the keyboard
            keyboard.add(row);
            // Set the keyboard to the markup
            keyboardMarkup.setKeyboard(keyboard);

            try {
                execute(prepareMessage(chatId, "Добро пожаловать в MyHotGirlfriends!"));
                execute(prepareMessage(chatId, "Вы авторизуетесь через VK.com, мы выгружаем фотогафии ваших подруг, отбираем самые горячие среди всех и отправляем вам!"));
                execute(prepareMessage(chatId, "Telegram интерфейс находится в разработке..."));
                execute(prepareMessage(chatId, "Но вы можете воспользоваться веб версией: http://52.166.68.146:4567"));
                execute(prepareMessage(chatId, "Откройте ссылку, авторизуйтесь через ВК и ожидайте результата"));
                execute(prepareMessage(chatId, "Через 30-60 минут результаты будут здесь: http://52.166.68.146:4567/results"));
                execute(prepareMessage(chatId, "Авторизуйтесь здесь: http://52.166.68.146:4567", keyboardMarkup));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Мои горячие подруги";
    }

    @Override
    public String getBotToken() {
        return new TokenStorageImpl().getToken();
    }

    private SendMessage prepareMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    private SendMessage prepareMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }
}
