package com.devs4j.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Devs4jProducer {

    public static Logger log = LoggerFactory.getLogger(Devs4jProducer.class);

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092"); //Broker de kafka al que nos vamos a conectar
        props.put("acks","all"); //Confirmación de que llega el mensaje
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("linger.ms", "10"); //Tiempo que espera el producer para enviar los mensajes

        try(Producer<String, String> producer = new KafkaProducer<>(props);){
            for(int i = 0; i < 100; i++){
                producer.send(new ProducerRecord<String, String>("devs4j-topic", String.valueOf(i), "devs4j-value"))/*.get()*/; //El .get es para hacerlo de manera sincrono
            }
            producer.flush(); //Envia to-do lo que queda pendiente y se asegura que todos los mensajes se entreguen
        } /*catch (InterruptedException | ExecutionException e) {
            log.error("Message producer interruped ", e);
        }*/
        log.info("Processing info time = {} ms", (System.currentTimeMillis() - startTime));
    }

}
