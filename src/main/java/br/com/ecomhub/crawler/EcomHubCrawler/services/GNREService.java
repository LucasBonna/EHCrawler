package br.com.ecomhub.crawler.EcomHubCrawler.services;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREGenerateDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREReceiptDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.crawlers.GNRE;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.PDFException;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREGenerateSchema;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREReceiptSchema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GNREService {

    @Autowired
    public GNRE gnreCrawler = new GNRE();
    public PDFService pdfService = new PDFService();

    public File getGNREDocument(GNREGenerateSchema data, File sessionDir) throws CrawlerException, PDFException {
        System.out.println("Entrou getGNRE");
        try {
            List<GNREGenerateDTO> infos = new ArrayList<>();
            List<File> downloadedFiles = new ArrayList<>();

            for (MultipartFile file : data.files()) {
                System.out.println("file: " + file.getOriginalFilename());
                File convertedFile = convertMultipartToFile(file, sessionDir);
                List<GNREGenerateDTO> dtos = this.pdfService.getGenerateInfo(convertedFile);
                infos.addAll(dtos);
            }
            System.out.println("converteu dto");

            for (GNREGenerateDTO dto : infos) {
                File downloadedFile = gnreCrawler.GNREGenerate(dto, sessionDir);
                if (downloadedFile != null && downloadedFile.exists()) {
                    downloadedFiles.add(downloadedFile);
                }
            }

            if (downloadedFiles.isEmpty()) {
                throw new CrawlerException("Erro ao criar arquivo ZIP");
            }

            return createZipFile(downloadedFiles, sessionDir);
        } catch (IOException e) {
            throw new CrawlerException("Erro ao rodar crawler");
        }
    }

    public File getGNREReceipt(GNREReceiptSchema data, File sessionDir) throws CrawlerException, PDFException {
        List<File> downloadedFiles = new ArrayList<>();

        for (MultipartFile file : data.files()) {
            File convertedFile = convertMultipartToFile(file, sessionDir);
            GNREReceiptDTO dto = pdfService.getReceiptInfo(convertedFile, data.gnreType());
            if (dto == null) {
                throw new PDFException("Erro ao extrair dados do PDF");
            }

            File downloadedFile = gnreCrawler.GNREReceipt(dto.barcode(), dto.uf(), sessionDir);

            if (downloadedFile != null && downloadedFile.exists()) {
                downloadedFiles.add(downloadedFile);
            } else {
                throw new CrawlerException("Erro no crawler: Arquivo não encontrado ou não baixado.");
            }
        }

        if (!downloadedFiles.isEmpty()) {
            try {
                return createZipFile(downloadedFiles, sessionDir);
            } catch (IOException e) {
                throw new CrawlerException("Erro ao criar arquivo ZIP");
            }
        } else {
            throw new CrawlerException("Nenhum arquivo baixado");
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile, File sessionDir) throws CrawlerException {
        try {
            File tempFile = new File(sessionDir, multipartFile.getOriginalFilename());
            System.out.println("chegou aq");
            multipartFile.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new CrawlerException("Erro ao converter MultipartFile para File", e);
        }
    }

    private File createZipFile(List<File> files, File sessionDir) throws CrawlerException, IOException {
        File zipFile = new File(sessionDir, "receipts.zip");

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                zos.putNextEntry(new ZipEntry(file.getName()));
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        } catch (IOException e) {
            throw new CrawlerException("Erro ao zipar arquivos" + e);
        }

        return zipFile;
    }

    public void cleanUpDirectory(File directory) throws CrawlerException {
        try {
            Files.walk(directory.toPath())
                    .sorted((p1, p2) -> -p1.compareTo(p2))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path + ". " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new CrawlerException("Erro ao limpar diretório temporário", e);
        }
    }
}
