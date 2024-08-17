package br.com.ecomhub.crawler.EcomHubCrawler.exceptions;

public class CrawlerException extends Exception{

    public CrawlerException() {
        super();
    }

    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(Throwable exception) {
        super(exception);
    }

    public CrawlerException(String message, Throwable exception) {
        super(message, exception);
    }
}
