
package com.deliverylab.inspection.kafka.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.deliverylab.inspection.models.Log;
import com.deliverylab.inspection.models.enums.EAction;

@Getter
@Setter
@NoArgsConstructor
public class LogMessage extends KafkaMessage {
    private Log log;

    public LogMessage(Log log, EAction action) {
        super(action);
        this.log = log;
    }
}
