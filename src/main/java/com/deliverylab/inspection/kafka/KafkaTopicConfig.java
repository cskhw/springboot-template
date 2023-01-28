package com.deliverylab.inspection.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String baseURL;

    @Bean
    KafkaAdmin kafkaAdmin() {
        System.out.println("kafkaAdmin " + baseURL);
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, baseURL);
        return new KafkaAdmin(configs);
    }

    @Bean
    NewTopic logTopic() {
        return new NewTopic("log", 1, (short) 1);
    }
}