package com.deliverylab.inspection.kafka.messages;

import java.io.Serializable;

import com.deliverylab.inspection.models.enums.EAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage implements Serializable {
    EAction action;
}
