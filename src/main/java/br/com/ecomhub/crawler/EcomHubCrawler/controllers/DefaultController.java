package br.com.ecomhub.crawler.EcomHubCrawler.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DefaultController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getStatus() {
        Map<String, String> response = Collections.singletonMap("application", "UP");
        return ResponseEntity.ok(response);
    }
}
