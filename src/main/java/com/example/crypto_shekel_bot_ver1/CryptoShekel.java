package com.example.crypto_shekel_bot_ver1;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class CryptoShekel {

    public void init() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        Bot bot = new Bot();

        try {
            telegramBotsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}