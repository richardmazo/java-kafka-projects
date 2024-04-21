package com.devs4j.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Devs4jProducerOrdenado {

    public static Logger log = LoggerFactory.getLogger(Devs4jProducerOrdenado.class);

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("acks","all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("linger.ms", "10");

        try(Producer<String, String> producer = new KafkaProducer<>(props);){
            for(int i = 0; i < 100000; i++){
                producer.send(new ProducerRecord<String, String>("devs4j-topic", (i%2==0) ? "key-2.1" : "key-3.1", String.valueOf(i)));
            }
            producer.flush();
        }
        log.info("Processing info time = {} ms", (System.currentTimeMillis() - startTime));

    }

}
