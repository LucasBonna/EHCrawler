package br.com.ecomhub.crawler.EcomHubCrawler.DTOs;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record GNREGenerateDTO(
        @NotNull @Valid String barcode,
        @NotNull @Valid StateEnum uf,
        @NotNull @Valid String numNF,
        @NotNull @Valid String preco,
        @NotNull @Valid String destCNPJ,
        @NotNull @Valid String destCEP,
        @NotNull @Valid String destRazao,
        @NotNull @Valid String destMun
        ) {
}
