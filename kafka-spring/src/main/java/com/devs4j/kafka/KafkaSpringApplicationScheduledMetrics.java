package com.devs4j.kafka;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootApplication
public class KafkaSpringApplicationScheduledMetrics {

    private static final Logger log = LoggerFactory.getLogger(KafkaSpringApplicationScheduledMetrics.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(id = "devs4jId",  autoStartup = "true", topics = "devs4j-topic", containerFactory = "listenerContainerFactory", groupId = "devs4j-group", properties = {"max.poll.interval.ms:4000", "max.poll.records:50"})
    public void listen(List<ConsumerRecord<String, String>> messages) {
        log.info("Messages received {} ", messages.size());
        //log.info("Start reading messages");
        for (ConsumerRecord<String, String> message : messages) {
            //log.info("Partition: {}, Offset: {}, Key: {}, Value: {}", message.partition(), message.offset(), message.key(), message.value());
        }
        //log.info("Batch complete");
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringApplicationScheduledMetrics.class, args);
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 100)
    public void sendKafkaMessages(){
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send("devs4j-topic", String.valueOf(i), String.format("Sample Message %d", i));
        }
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 100)
    public void printMetrics(){

        List<Meter> metrics = meterRegistry.getMeters();

        for (Meter metric : metrics) {
            log.info("Metric: {}", metric.getId().getName());
        }

        double count = meterRegistry.get("kafka.producer.record.send.total").functionCounter().count();
        log.info("Count: {}", count);
    }

}
