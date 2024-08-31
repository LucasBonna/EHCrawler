package br.com.ecomhub.crawler.EcomHubCrawler.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/ehcrawler")
@CrossOrigin(origins = "*")
public class DefaultController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getStatus() {
        Map<String, String> status = Collections.singletonMap("application", "crawler-up");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/docs")
    @Hidden
    public String redocHtml() {
        return """
            <!DOCTYPE html>
            <html>
              <head>
                <title>API Documentation</title>
                <meta charset="utf-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <link href="https://fonts.googleapis.com/css?family=Montserrat:300,400,700|Roboto:300,400,700" rel="stylesheet">
                <style>
                  body {
                    margin: 0;
                    padding: 0;
                  }
                </style>
              </head>
              <body>
                <redoc spec-url="http://localhost:8001/api-docs"></redoc>
                <script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
              </body>
            </html>
        """;
    }
}
