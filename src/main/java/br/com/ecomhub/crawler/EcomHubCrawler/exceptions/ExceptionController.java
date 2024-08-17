package br.com.ecomhub.crawler.EcomHubCrawler.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CrawlerException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CrawlerException ex) {
        System.out.println("CrawlerException: " + ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "CrawlerException"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
