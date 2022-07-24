package com.ghostcoderz.blog_application.utils;

import com.ghostcoderz.blog_application.payload.PostDto;
import org.springframework.web.client.RestTemplate;
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
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForObject("https://ghostarena.herokuapp.com/api/v1/posts/1",
                        PostDto.class);
                System.out.println("Post object retrieved!");
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
