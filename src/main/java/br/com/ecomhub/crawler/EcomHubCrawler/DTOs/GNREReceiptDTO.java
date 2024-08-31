package br.com.ecomhub.crawler.EcomHubCrawler.DTOs;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record GNREReceiptDTO(@NotNull @Valid String barcode, @NotNull @Valid StateEnum uf) {
}
