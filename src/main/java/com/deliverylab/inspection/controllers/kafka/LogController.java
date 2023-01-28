package com.deliverylab.inspection.controllers.kafka;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverylab.inspection.common.utils.FileUtils;
import com.deliverylab.inspection.kafka.messages.LogMessage;
import com.deliverylab.inspection.models.Log;
import com.deliverylab.inspection.models.enums.EAction;
import com.deliverylab.inspection.payload.request.kafka.log.CreateLogRequest;
import com.deliverylab.inspection.services.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/kafka/log")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Please use methods.");
    }

    // 로그 생성
    @PostMapping("/create")
    public ResponseEntity<?> createLog(@Valid @RequestBody CreateLogRequest createLogRequest,
            HttpServletRequest request) throws Exception {
        Log logData = new Log(createLogRequest);

        logData.setDate(new Date());

        // 카프카로 데이터 전송
        logService.sendMessage(new LogMessage(logData, EAction.CREATE));
        return ResponseEntity.ok(logData);
    }

    // 로그 전부 읽어오기
    @GetMapping("/read")
    public ResponseEntity<String> readLog() throws Exception {
        String logsData = FileUtils.fileReader("./logs/access.json");

        return ResponseEntity.ok(logsData);
    }

    // 로그 뒤에서 부터 읽기
    @GetMapping("/tail/{offset}")
    public ResponseEntity<String> tailLog(@PathVariable int offset) throws Exception {
        String logsData = FileUtils.fileReaderOnTail("./logs/access.json", offset);

        return ResponseEntity.ok(logsData);
    }
}
