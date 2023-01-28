package com.deliverylab.inspection.kafka.producers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.deliverylab.inspection.kafka.messages.LogMessage;

import java.io.Serializable;

@Slf4j
@NoArgsConstructor
@Component
public class LogProducer {
    final String logTopic = "log";

    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplate;

    public void send(LogMessage msg) throws Exception {
        kafkaTemplate.send(logTopic, msg).thenAcceptAsync((SendResult<String, Serializable> result) -> {
            log.info("kafka/log/create successfully with offset = " + result.getRecordMetadata().offset());
        }).exceptionally(throwable -> {
            System.out.println("exception occurred!!");
            return null;
        });
    }
}
