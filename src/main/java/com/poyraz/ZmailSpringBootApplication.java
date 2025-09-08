package com.poyraz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@PropertySource("classpath:messages.properties")
@SpringBootApplication
public class ZmailSpringBootApplication {

    private static final Logger logger = LoggerFactory.getLogger(ZmailSpringBootApplication.class);

    public static void main(String[] args) {
        logger.info("ZmailSpringBootApplication is starting...");
        SpringApplication.run(ZmailSpringBootApplication.class, args);
        logger.info("ZmailSpringBootApplication started successfully.");
    }

}
