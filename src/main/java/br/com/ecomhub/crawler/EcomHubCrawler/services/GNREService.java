package br.com.ecomhub.crawler.EcomHubCrawler.services;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREReceiptDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.crawlers.gnres.GNRE;
import br.com.ecomhub.crawler.EcomHubCrawler.entities.GNREGenerateEntity;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.PDFException;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.XMLException;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.Helpers;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREGenerateSchema;
import br.com.ecomhub.crawler.EcomHubCrawler.schemas.GNREReceiptSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GNREService {
    private final GNRE gnreCrawler;
    private final PDFService pdfService;
    private final XMLService xmlService;
    private final Helpers helpers;

    @Autowired
    public GNREService(GNRE gnreCrawler, PDFService pdfService, XMLService xmlService, Helpers helpers) {
        this.gnreCrawler = gnreCrawler;
        this.pdfService = pdfService;
        this.xmlService = xmlService;
        this.helpers = helpers;
    }

    public File getGNREDocument(GNREGenerateSchema data, File sessionDir) throws CrawlerException, PDFException {
        System.out.println("Entrou getGNRE");
        try {
            List<File> downloadedFiles = new ArrayList<>();
            Set<StateEnum> unwantedStates = Set.of(StateEnum.SP, StateEnum.RJ, StateEnum.RS, StateEnum.PR, StateEnum.MG);

            for (MultipartFile file : data.files()) {
                System.out.println("file: " + file.getOriginalFilename());
                File convertedFile = helpers.convertMultipartToFile(file, sessionDir);
                GNREGenerateEntity gnreGenerateEntity = xmlService.extractXMLNFeData(convertedFile);
                System.out.println("passou?");

                // Verificar se é um dos estados
                if (unwantedStates.contains(gnreGenerateEntity.getDestUF())) {
                    System.out.println("estado nao suportado: " + gnreGenerateEntity.getDestUF());
                    continue;
                }
                File downloadedFile = gnreCrawler.GNREGenerate(gnreGenerateEntity, sessionDir);
                if (downloadedFile != null && downloadedFile.exists()) {
                    downloadedFiles.add(downloadedFile);
                }
            }

            if (downloadedFiles.isEmpty()) {
                throw new CrawlerException("Erro ao criar arquivo ZIP");
            }

            return createZipFile(downloadedFiles, sessionDir);
        } catch (IOException | XMLException e) {
            throw new CrawlerException("Erro ao rodar crawler");
        }
    }

    public File getGNREReceipt(GNREReceiptSchema data, File sessionDir) throws CrawlerException, PDFException {
        List<File> downloadedFiles = new ArrayList<>();

        for (MultipartFile file : data.files()) {
            File convertedFile = helpers.convertMultipartToFile(file, sessionDir);
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
