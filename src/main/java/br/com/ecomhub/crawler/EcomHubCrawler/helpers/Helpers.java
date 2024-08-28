package br.com.ecomhub.crawler.EcomHubCrawler.helpers;

import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@Component
public class Helpers {
    public int getCurrentMonth() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue();
    }

    public String getTommorowDate() {
        LocalDate tommorowDate = LocalDate.now().plusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return tommorowDate.format(formatter);
    }

    public String getConvenio() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        return today.format(formatter);
    }

    public String getTodaysDate() {
        LocalDate todaysDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return todaysDate.format(formatter);
    }

    public String getCurrentYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    public File convertMultipartToFile(MultipartFile multipartFile, File sessionDir) throws CrawlerException {
        try {
            File tempFile = new File(sessionDir, Objects.requireNonNull(multipartFile.getOriginalFilename()));
            multipartFile.transferTo(tempFile);
            System.out.println("vai retornar");
            return tempFile;
        } catch (IOException e) {
            throw new CrawlerException("Erro ao converter MultipartFile para File", e);
        }
    }
}
