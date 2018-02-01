package co.axelrod.mygirlfriends.telegram;

import co.axelrod.mygirlfriends.telegram.token.TelegramTokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 29.01.2018.
 */

@Slf4j
public class MyHotGirlfriendsBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();

            log.debug("New request from Telegram bot with id: " + chatId);

            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton()
                    .setText("Авторизоваться")
                    .setUrl("http://52.166.68.146:4567"));
            rowInline.add(new InlineKeyboardButton()
                    .setText("Результаты")
                    .setCallbackData("getResults"));
            rowsInline.add(rowInline);
            keyboardMarkup.setKeyboard(rowsInline);

            try {
                execute(prepareMessage(chatId, "Добро пожаловать в MyHotGirlfriends!"));
                execute(prepareMessage(chatId, "Вы авторизуетесь через VK.com, мы выгружаем фотогафии ваших подруг, отбираем самые горячие среди всех и отправляем вам!"));
                execute(prepareMessage(chatId, "Авторизуйтесь в ВК для начала обработки"));
                execute(prepareMessage(chatId, "Результаты будут доступны через 30 минут", keyboardMarkup));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        } else if(update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            log.debug("New callback query from Telegram bot with id: " + chatId);

            try {
                SendPhoto sendPhoto = new SendPhoto()
                        .setChatId(chatId)
                        .setPhoto("https://pp.userapi.com/c840632/v840632339/6295/Xm7zAbSn5Xc.jpg")
                        .setCaption("THE HOTTEST ONE");
                sendPhoto(sendPhoto);

                sendPhoto = new SendPhoto()
                        .setChatId(chatId)
                        .setPhoto("https://pp.userapi.com/c841224/v841224931/42c30/6-AY9-felng.jpg")
                        .setCaption("Второе место");

                sendPhoto(sendPhoto);

                sendPhoto = new SendPhoto()
                        .setChatId(chatId)
                        .setPhoto("https://pp.userapi.com/c637127/v637127354/46957/bsTzpgPmSBM.jpg")
                        .setCaption("Тоже ничего");

                sendPhoto(sendPhoto);


                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton()
                        .setText("А можно всех посмотреть?")
                        .setUrl("http://52.166.68.146:4567/results"));
                rowsInline.add(rowInline);
                keyboardMarkup.setKeyboard(rowsInline);

                execute(prepareMessage(chatId, "Наверное, у вас возник вопрос:", keyboardMarkup));
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
        return new TelegramTokenStorage().getToken();
    }

    private SendMessage prepareMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    private SendMessage prepareMessage(Long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private SendMessage prepareWebPreviewMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.enableWebPagePreview();
        return message;
    }
}
