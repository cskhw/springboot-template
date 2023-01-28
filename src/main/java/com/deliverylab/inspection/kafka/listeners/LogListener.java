package com.deliverylab.inspection.kafka.listeners;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.deliverylab.inspection.common.utils.FileUtils;
import com.deliverylab.inspection.kafka.messages.LogMessage;
import com.deliverylab.inspection.models.Log;
import com.deliverylab.inspection.models.enums.EAction;

@Slf4j
@Service
@SuppressWarnings("unchecked")
public class LogListener {
    @KafkaListener(topics = "log", containerFactory = "logKafkaListenerContainerFactory")
    public void newLogListener(LogMessage msg) throws Exception {
        Log logData = msg.getLog();
        EAction action = msg.getAction();

        try {
            // 로그 스트링 만들고 파일에 add
            if (action == EAction.CREATE) {

                Reader reader = new FileReader("./logs/access.json");
                JSONParser parser = new JSONParser();
                JSONArray ja = (JSONArray) parser.parse(reader);

                HashMap<String, Object> hm = new HashMap<String, Object>();

                hm.put("id", logData.getId());
                hm.put("userId", logData.getUserId());
                hm.put("date", logData.getDate().toString());
                hm.put("path", logData.getPath());
                hm.put("url", logData.getUrl());
                hm.put("ip", logData.getIp());
                hm.put("event", logData.getEvent());

                ja.add(new JSONObject(hm));

                String logStr = ja.toJSONString();

                log.info("KAFKA LOG LISTEN --- [ACTION: " + action + "] " + logStr);
                FileWriter file = new FileWriter("./logs/access.json");
                file.write(logStr);
                file.flush();
                file.close();

                // FileUtils.newFileWriter("./logs", "access.json", logStr + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}