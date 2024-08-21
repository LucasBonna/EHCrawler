package br.com.ecomhub.crawler.EcomHubCrawler.schemas;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record GNREGenerateSchema(
        @RequestPart(value = "files") @Valid @NotNull MultipartFile[] files
        ) {
}
