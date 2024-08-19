package br.com.ecomhub.crawler.EcomHubCrawler.schemas;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.GNREEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record GNREReceiptSchema(
        @RequestPart(value = "files") @Valid @NotNull MultipartFile[] files,
        @RequestPart(value = "gnreType")@NotNull GNREEnum gnreType
        ) {
}
