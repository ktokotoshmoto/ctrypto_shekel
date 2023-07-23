package com.example.crypto_shekel_bot_ver1;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class Bot extends TelegramLongPollingBot {
    static String COINBASE_API_URL = "https://api.coinbase.com/v2/exchange-rates?currency=";
    static DecimalFormat decimalFormat = new DecimalFormat("#.#######");

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage()
                    .setChatId(chatId);

            try {
                switch (messageText) {
                    case "bitcoin":
                        float bitcoinRate = (float) getCryptoPrice("BTC");
                        message.setText("Курс биткоина: " + bitcoinRate + " USD");
                        break;

                    case "ethereum":
                        float ethereumRate = (float) getCryptoPrice("ETH");
                        message.setText("Курс эфира: " + ethereumRate + " USD");
                        break;

                    case "doge":
                        float dogeRate = (float) getCryptoPrice("DOGE");
                        message.setText("Курс доги: " + dogeRate + " USD");
                        break;

                    case "shiba":
                        float shibaRate = (float) getCryptoPrice("SHIB");
                        message.setText("Курс шибы: " + decimalFormat.format(shibaRate) + " USD");
                        break;

                    default:
                        message.setText("Выберите одну из кнопок");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow row1 = new KeyboardRow();
            KeyboardRow row2 = new KeyboardRow();

            row1.add(new KeyboardButton("bitcoin"));
            row1.add(new KeyboardButton("ethereum"));
            row2.add(new KeyboardButton("doge"));
            row2.add(new KeyboardButton("shiba"));

            keyboard.add(row1);
            keyboard.add(row2);

            keyboardMarkup.setKeyboard(keyboard);
            keyboardMarkup.setOneTimeKeyboard(true);
            keyboardMarkup.setResizeKeyboard(true);

            message.setReplyMarkup(keyboardMarkup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static double getCryptoPrice(String currencyName) throws Exception {
        HttpClient client = HttpClients.createDefault();

        HttpGet request = new HttpGet(COINBASE_API_URL + currencyName);

        String response = EntityUtils.toString(client.execute(request).getEntity());

        return parseCryptoPriceFromJson(response);
    }

    private static double parseCryptoPriceFromJson(String json) {
        JSONObject obj = new JSONObject(json);
        return obj.getJSONObject("data").getJSONObject("rates").getFloat("USD");
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}
