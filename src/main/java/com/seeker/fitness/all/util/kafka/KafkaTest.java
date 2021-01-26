package com.seeker.fitness.all.util.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaTest {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @RequestMapping("/sendMsg")
    public String sendMsg(String topic,String message){
        kafkaTemplate.send(topic,message);
        return "success";
    }

}
