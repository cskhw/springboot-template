package com.deliverylab.inspection.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverylab.inspection.common.utils.FileUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Please use methods.");
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
