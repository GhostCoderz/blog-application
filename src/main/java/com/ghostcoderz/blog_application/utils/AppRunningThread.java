package com.ghostcoderz.blog_application.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppRunningThread extends Thread{

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Thread is running...");
        while (true) {
            System.out.println("Current Time : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("DD-MM-YYYY HH:MM:SS")));
            try {
                //Wait for one min, so it doesn't print too fast
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
