package br.com.ecomhub.crawler.EcomHubCrawler.DTOs;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import jakarta.validation.constraints.NotNull;

public record GNREReceiptDTO(@NotNull String barcode, @NotNull StateEnum uf) {
}
