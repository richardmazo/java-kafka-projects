package com.devs4j.kafka.callbacks;

import com.devs4j.kafka.producers.Devs4jProducer;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Devs4jCallbackProducer {

    public static Logger log = LoggerFactory.getLogger(Devs4jProducer.class);

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("acks","all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("linger.ms", "10");

        try(Producer<String, String> producer = new KafkaProducer<>(props);){
            for(int i = 0; i < 10000; i++){
                producer.send(new ProducerRecord<String, String>("devs4j-topic", String.valueOf(i), "devs4j-value"),
                        new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if(exception != null){
                            log.info("There was an error {} ", exception.getMessage());
                        }
                        log.info("Offset = {}, Partition = {}, Topic = {}", metadata.offset(), metadata.partition(), metadata.topic());
                    }
                });
            }
            producer.flush();
        }
        log.info("Processing info time = {} ms", (System.currentTimeMillis() - startTime));

    }


}
