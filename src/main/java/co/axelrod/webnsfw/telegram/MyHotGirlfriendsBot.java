package co.axelrod.webnsfw.telegram;

import co.axelrod.webnsfw.telegram.token.TokenStorageImpl;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 29.01.2018.
 */
public class MyHotGirlfriendsBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            System.out.println("New request from Telegram bot with id: " + chatId);
            try {
                execute(prepareMessage(chatId, "Hello from MyGirlfriendsBot!"));
                execute(prepareMessage(chatId, "Bot will download your girlfriends photos from VK.com, find the hottest photo and send it to you!"));
                execute(prepareMessage(chatId, "Work in progres for now..."));
                execute(prepareMessage(chatId, "But you can try service now at: http://52.166.68.146:4567"));
                execute(prepareMessage(chatId, "Open this link, authorize by your VK.com account and wait for 30-60 minutes"));
                execute(prepareMessage(chatId, "Results will be here: http://52.166.68.146:4567/results"));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "My Hot Girlfriends";
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
}
