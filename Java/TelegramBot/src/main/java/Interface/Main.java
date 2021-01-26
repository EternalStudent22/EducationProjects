package Interface;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static void main(String[] args) {
        Bot.inicilizeBot();
//        ApiContextInitializer.init();
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//
//        try {
//            botsApi.registerBot(new Bot());
//        } catch (TelegramApiException var3) {
//            var3.printStackTrace();
//        }

    }
}
