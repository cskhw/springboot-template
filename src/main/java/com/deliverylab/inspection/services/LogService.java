package com.deliverylab.inspection.services;

import com.deliverylab.inspection.kafka.messages.LogMessage;
import com.deliverylab.inspection.kafka.producers.LogProducer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogService {
    @Autowired
    private LogProducer logProducer;

    public void sendMessage(LogMessage msg) throws Exception {
        log.info("[LogService] send log to topic, message: " + msg.toString());
        logProducer.send(msg);
    }
}