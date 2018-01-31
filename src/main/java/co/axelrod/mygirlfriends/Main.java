package co.axelrod.mygirlfriends;

import co.axelrod.mygirlfriends.spark.Routes;
import co.axelrod.mygirlfriends.telegram.MyHotGirlfriendsBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 27.01.2018.
 */
public class Main {
    public static void main(String[] args) {
        String domain = args[0];
        //String domain = "localhost";

        new Routes(domain);

        // Starting Telegram bot
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MyHotGirlfriendsBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}