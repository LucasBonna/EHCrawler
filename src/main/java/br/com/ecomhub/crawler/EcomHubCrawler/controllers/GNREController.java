package br.com.ecomhub.crawler.EcomHubCrawler.controllers;

import br.com.ecomhub.crawler.EcomHubCrawler.entities.GNREGenerateEntity;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.PDFException;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.XMLException;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.Helpers;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREGenerateSchema;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREReceiptSchema;
import br.com.ecomhub.crawler.EcomHubCrawler.services.GNREService;
import br.com.ecomhub.crawler.EcomHubCrawler.services.XMLService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequestMapping("/ehcrawler/gnre")
public class GNREController {
  private final GNREService gnreService;
  private final XMLService xmlService;
  private final Helpers helpers;

  @Autowired
  public GNREController(final GNREService gnreService, final XMLService xmlService, final Helpers helpers) {
    this.gnreService = gnreService;
    this.xmlService = xmlService;
    this.helpers = helpers;
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

  @PostMapping(value = "/testeXML", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<GNREGenerateEntity> testXML(@NotNull @Valid MultipartFile file) throws XMLException, CrawlerException {
    File sessionDir = new File("/tmp/downloads/session-" + UUID.randomUUID());
    if (!sessionDir.mkdirs()) {
      throw new XMLException("Não foi possível criar o diretório da sessão.");
    }

    File convertedFile = helpers.convertMultipartToFile(file, sessionDir);
    GNREGenerateEntity data = xmlService.extractXMLNFeData(convertedFile);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
