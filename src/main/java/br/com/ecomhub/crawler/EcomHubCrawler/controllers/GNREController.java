package br.com.ecomhub.crawler.EcomHubCrawler.controllers;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREGenerateDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.GNREEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.PDFException;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREGenerateSchema;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREReceiptSchema;
import br.com.ecomhub.crawler.EcomHubCrawler.services.GNREService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREReceiptDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.crawlers.GNRE;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gnre")
public class GNREController {

  @Autowired
  private GNREService gnreService;

  @GetMapping("/")
  public ResponseEntity<Map<String, String>> getStatus() {
    Map<String, String> response = Collections.singletonMap("application", "UP");
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/generate", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<byte[]>generateGNRE(@Valid GNREGenerateSchema data) throws CrawlerException, IOException {
    System.out.println("criando diretorio");
    File sessionDir = new File("/tmp/downloads/session-" + UUID.randomUUID());
    if (!sessionDir.mkdirs()) {
      throw new CrawlerException("Não foi possível criar o diretório da sessão.");
    }

    try {
      File zipFile = gnreService.getGNREDocument(data, sessionDir);
      System.out.println("voltou crawler");

      if (!zipFile.exists()) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }

      byte[] zipContent = Files.readAllBytes(zipFile.toPath());

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDispositionFormData("attachment", "receipts.zip");

      return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
    } catch (PDFException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/receipt", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<byte[]> receiptGNRE(@Valid GNREReceiptSchema data) throws CrawlerException, IOException {
    File sessionDir = new File("/tmp/downloads/session-" + UUID.randomUUID());
    if (!sessionDir.mkdirs()) {
      throw new CrawlerException("Não foi possível criar o diretório da sessão.");
    }

    try {
      File zipFile = gnreService.getGNREReceipt(data, sessionDir);

      if (zipFile != null) {
        byte[] zipContent = Files.readAllBytes(zipFile.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "receipts.zip");

        return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (PDFException e) {
        throw new CrawlerException("Erro ao rodar crawler" + e);
    } finally {
      gnreService.cleanUpDirectory(sessionDir);
    }
  }
}
