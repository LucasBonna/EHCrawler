package br.com.ecomhub.crawler.EcomHubCrawler.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    private String message;
    private int statusCode;
    private String error;

    public ErrorResponse(String message, int statusCode, String error) {
        this.message = message;
        this.statusCode = statusCode;
        this.error = error;
    }
}
