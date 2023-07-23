package com.example.crypto_shekel_bot_ver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoShekelBotVer1Application {

    public static void main(String[] args) {
        CryptoShekel cryptoShekel = new CryptoShekel();
        cryptoShekel.init();
        SpringApplication.run(CryptoShekelBotVer1Application.class, args);
    }

}
