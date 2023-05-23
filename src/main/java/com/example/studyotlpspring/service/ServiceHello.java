package com.example.studyotlpspring.service;

import java.util.Random;

public class ServiceHello {

    public void randonTime() throws InterruptedException {
        Random rdn = new Random();

        var interval = rdn.nextInt(2);

        interval *= 100;

        Thread.sleep(interval);
    }

}
