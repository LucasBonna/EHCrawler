package br.com.ecomhub.crawler.EcomHubCrawler.services;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREReceiptDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.crawlers.GNRE;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class GNREService {
    public ResponseEntity<byte[]> getGNREReceipt(@RequestBody @Valid GNREReceiptDTO gnreReceipt) throws CrawlerException {
        GNRE gnreCrawler = new GNRE("https://www.gnre.pe.gov.br:444/gnre/v/guia/consultar");

        File downloadedFile = gnreCrawler.GNREReceipt(gnreReceipt.barcode(), gnreReceipt.uf());

        if (downloadedFile != null && downloadedFile.exists()) {
            try {
                byte[] fileContent = Files.readAllBytes(downloadedFile.toPath());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", downloadedFile.getName());
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            } catch (IOException e) {
                throw new CrawlerException("Erro ao ler arquivo baixado", e);
            } finally {
                downloadedFile.delete();
            }
        } else {
            throw new CrawlerException("Erro no crawler: Arquivo não encontrado ou não baixado.");
        }
    }
}
