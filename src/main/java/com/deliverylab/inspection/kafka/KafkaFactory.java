package com.deliverylab.inspection.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.deliverylab.inspection.kafka.messages.KafkaMessage;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaFactory<T extends KafkaMessage> {
    @Value(value = "${kafka.bootstrapAddress}")
    private String baseURL;

    /**
     * 컨슈머 설정
     * 
     * BOOTSTRAP_SERVERS_CONFIG: kafka 첫번째 브로커 URL
     * KEY_DESERIALIZER_CLASS_CONFIG: 키 역직렬화 방법
     * VALUE_DESERIALIZER_CLASS_CONFIG: 값 역직렬화 방법
     * GROUP_ID_CONFIG: 그룹 ID
     * 
     * @return {Map<String, Object>}
     */
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, baseURL);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consuming");
        return props;
    }

    // 기본 타입 매퍼 만들어줌
    public DefaultJackson2JavaTypeMapper typeMapper() {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> classMap = new HashMap<>();
        typeMapper.setIdClassMapping(classMap);
        typeMapper.addTrustedPackages("*");
        return typeMapper;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> type) {
        // 컨슈머 역직렬화 타입 설정
        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(type);
        jsonDeserializer.setTypeMapper(typeMapper());
        jsonDeserializer.setUseTypeMapperForKey(true);

        ConsumerFactory<String, T> logConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(), jsonDeserializer);

        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(logConsumerFactory);
        log.info("Kafka log consumer configure  " + type.toString());
        return factory;
    }
}