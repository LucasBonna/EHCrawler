package br.com.ecomhub.crawler.EcomHubCrawler.DTOs;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.GNREEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.StateMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public record GNREReceiptDTO(@NotNull @Valid String barcode, @NotNull @Valid StateEnum uf) {
}
