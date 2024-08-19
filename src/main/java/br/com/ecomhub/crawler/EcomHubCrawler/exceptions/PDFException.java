package br.com.ecomhub.crawler.EcomHubCrawler.exceptions;

public class PDFException extends Exception {
    public PDFException() {
        super();
    }

    public PDFException(String message) {
        super(message);
    }

    public PDFException(Throwable exception) {
        super(exception);
    }

    public PDFException(String message, Throwable exception) {
        super(message, exception);
    }
}
