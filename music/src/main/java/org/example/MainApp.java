package org.example;

import org.apache.camel.main.Main;
import org.apache.camel.main.MainConfigurationProperties;

import java.util.concurrent.CountDownLatch;

public class MainApp {

    public static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String... args) throws Exception {

        Main main = new Main();

        try(MainConfigurationProperties configurationProperties = main.configure()) {
            configurationProperties.addRoutesBuilder(new MyRouteBuilder());
            main.start();
            latch.await();
            main.stop();
        }
    }

}

