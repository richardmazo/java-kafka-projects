package com.devs4j.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

//@SpringBootApplication
public class KafkaSpringApplicationBatch implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(KafkaSpringApplicationBatch.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @KafkaListener(id = "devs4jId",  autoStartup = "false", topics = "devs4j-topic", containerFactory = "listenerContainerFactory", groupId = "devs4j-group", properties = {"max.poll.interval.ms:4000", "max.poll.records:10"})
    public void listen(List<ConsumerRecord<String, String>> messages) {
        log.info("Start reading messages");
        for (ConsumerRecord<String, String> message : messages) {
            log.info("Partition: {}, Offset: {}, Key: {}, Value: {}", message.partition(), message.offset(), message.key(), message.value());
        }
        log.info("Batch complete");
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringApplicationBatch.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send("devs4j-topic", String.valueOf(i), String.format("Sample Message %d", i));
        }
        log.info("Waiting to start");
        Thread.sleep(5000);
        log.info("Starting");
        registry.getListenerContainer("devs4jId").start();
        Thread.sleep(5000);
        registry.getListenerContainer("devs4jId").stop();

    }
}
