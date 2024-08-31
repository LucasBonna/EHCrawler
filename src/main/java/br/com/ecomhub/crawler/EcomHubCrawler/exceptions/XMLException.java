package br.com.ecomhub.crawler.EcomHubCrawler.exceptions;

public class XMLException extends Exception {
    public XMLException() {
        super();
    }

    public XMLException(String message) {
        super(message);
    }

    public XMLException(Throwable exception) {
        super(exception);
    }

    public XMLException(String message, Throwable exception) {
        super(message, exception);
    }
}
