package com.devs4j.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

//@SpringBootApplication
public class KafkaSpringApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(KafkaSpringApplication.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "devs4j-topic", groupId = "devs4j-group")
    public void listen(String message){
        log.info("Message received {}", message);
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        kafkaTemplate.send("devs4j-topic", "Sample Message").get(100, TimeUnit.MILLISECONDS);

        //Callback asincrono
        /*CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("devs4j-topic", "Sample Message");
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent {}", result.getRecordMetadata().offset());
            } else {
                log.error("Error sending message", ex);
            }
        });*/
    }
}
