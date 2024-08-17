package br.com.ecomhub.crawler.EcomHubCrawler.controllers;

import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.services.GNREService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREReceiptDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.crawlers.GNRE;
import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GNREController {

  @Autowired
  private GNREService gnreService;

  @GetMapping("/")
  public ResponseEntity<Map<String, String>> getStatus() {
    Map<String, String> response = Collections.singletonMap("application", "UP");
    return ResponseEntity.ok(response);
  }

  @PostMapping("/gnre/receipt")
  @ResponseBody
  public ResponseEntity<byte[]> gnreRecepit(@RequestBody @Valid GNREReceiptDTO dto) throws CrawlerException {
    return gnreService.getGNREReceipt(dto);
  }
}
