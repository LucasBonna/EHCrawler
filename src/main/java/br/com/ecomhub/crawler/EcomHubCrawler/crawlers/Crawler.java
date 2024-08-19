package br.com.ecomhub.crawler.EcomHubCrawler.crawlers;

import lombok.Setter;

@Setter
public class Crawler {
    protected String url;

    public Crawler() {
        this(null);
    }

    public Crawler(String url) {
        this.url = url;
    }
}
