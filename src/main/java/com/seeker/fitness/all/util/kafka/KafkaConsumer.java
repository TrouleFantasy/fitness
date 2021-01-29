package com.seeker.fitness.all.util.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

//@Component
public class KafkaConsumer {
    /**
     * 订阅指定的主题消息
     * @param consumerRecord 消息记录
     */
    @KafkaListener(topics = {"hello","hello2"})
    public void listen(ConsumerRecord consumerRecord){
        System.out.println(consumerRecord);
        System.out.println(consumerRecord.topic()+":"+consumerRecord.value());
    }
}
