package com.devs4j.kafka.lambda;

import com.devs4j.kafka.producers.Devs4jProducer;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

class Devs4jCallbackProducer implements Callback{

    public static Logger log = LoggerFactory.getLogger(Devs4jCallbackProducer.class);

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if(exception != null){
            log.info("There was an error {} ", exception.getMessage());
        }
        log.info("Offset = {}, Partition = {}, Topic = {}", metadata.offset(), metadata.partition(), metadata.topic());
    }
}

public class Devs4jLambdaProducer {

    public static Logger log = LoggerFactory.getLogger(Devs4jLambdaProducer.class);

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
                        (metadata, exception) -> {
                            if(exception != null){
                                log.info("There was an error {} ", exception.getMessage());
                            }
                            log.info("Offset = {}, Partition = {}, Topic = {}", metadata.offset(), metadata.partition(), metadata.topic());
                        });
            }
            producer.flush();
        }
        log.info("Processing info time = {} ms", (System.currentTimeMillis() - startTime));

    }

}
