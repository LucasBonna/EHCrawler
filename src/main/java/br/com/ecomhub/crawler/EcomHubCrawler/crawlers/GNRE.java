package br.com.ecomhub.crawler.EcomHubCrawler.crawlers;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.StateMapper;
import br.com.ecomhub.crawler.EcomHubCrawler.solvers.CapMonster;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

@Setter
@Service
public class GNRE extends Crawler {
  private String siteKey;
  private WebDriver driver;

  public GNRE() {
    super();
  }

  public void setup(String url) {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("--headless");
    options.addArguments(
            "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
    options.addArguments("--width=1920");
    options.addArguments("--height=1080");
    options.addArguments("--disable-gpu");
    options.addArguments("--lang=pt-BR");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-blink-features=AutomationControlled");
    options.addArguments("--enable-javascript");

    options.addPreference("browser.download.dir", "/tmp/downloads");
    options.addPreference("browser.download.folderList", 2);
    options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
    options.addPreference("pdfjs.disabled", true);

    this.url = url;

    this.driver = new FirefoxDriver(options);
  }

  public File GNREGenerate() throws CrawlerException {
    System.out.println("comecou crawler");

      try {
          setup("https://www.gnre.pe.gov.br:444/gnre/v/guia/index");
          this.driver.get(this.url);
          System.out.println(this.driver.getTitle());

          return null;
      } finally {
        if (this.driver != null) {
          try {
            this.driver.quit();
            System.out.println("Driver fechado com sucesso.");
          } catch (Exception e) {
            throw new CrawlerException("Erro ao fechar driver", e);
          }
        }
      }
  }

  public File GNREReceipt(String barcode, StateEnum uf, File sessionDir) throws CrawlerException {
    String userHome = System.getProperty("user.home");
    System.out.println("comecou crawler");
    File downloadedFile = null;

    try {
      setup("https://www.gnre.pe.gov.br:444/gnre/v/guia/consultar");
      this.driver.get(this.url);
      String title = this.driver.getTitle();
      System.out.println(title);

      WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(30));
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rbCodBarra")));
      WebElement codBarrasButton = this.driver.findElement(By.id("rbCodBarra"));
      codBarrasButton.click();

      WebElement dropdownMenu = this.driver.findElement(By.id("cmbUF"));
      Select dropdown = new Select(dropdownMenu);
      Integer estadoIndex = StateMapper.ufIndexMap.get(uf);

      if (estadoIndex != null) {
        dropdown.selectByIndex(estadoIndex);
      } else {
        throw new CrawlerException("UF não encontrado");
      }

      WebElement codBarraInput = this.driver.findElement(By.id("codigoBarra"));
      codBarraInput.sendKeys(barcode);

      WebElement recaptch = this.driver.findElement(By.className("g-recaptcha"));
      String siteKey = recaptch.getAttribute("data-sitekey");

      CapMonster solver = new CapMonster("1e1499f81b741286a40251c6c2fff325");
      solver.setWebsiteKey(siteKey);
      solver.setWebsiteURL(this.driver.getCurrentUrl());
      solver.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
      String gRecaptchaResponse = solver.solveRecaptchaV2();
      System.out.println("solved: " + gRecaptchaResponse);

      if (gRecaptchaResponse == null) {
        throw new CrawlerException("Recaptcha failed");
      }

      System.out.println("Insere o token");
      JavascriptExecutor js = (JavascriptExecutor) this.driver;
      js.executeScript("document.getElementById('g-recaptcha-response').innerHTML = arguments[0];", gRecaptchaResponse);

      System.out.println("clicando...");
      js.executeScript("onSubmit()");

      List<WebElement> errorElements = this.driver.findElements(By.className("ui-dialog-content"));
      if (errorElements.isEmpty()) {
        throw new CrawlerException("Invalid Data");
      }

      List<WebElement> captchaElements = this.driver.findElements(By.id("rc-imageselect-payload"));
      if (!captchaElements.isEmpty()) {
        throw new CrawlerException("Captcha Invalido");
      }

      wait.until(ExpectedConditions.elementToBeClickable(By.id("dvResultado")));
      WebElement printButton = this.driver.findElement(By.id("btnImprimir"));
      printButton.click();
      System.out.println("Baixando");

      downloadedFile = waitForDownload(sessionDir);

      if (downloadedFile == null) {
        throw new CrawlerException("Arquivo PDF não encontrado até download");
      }

      System.out.println("Arquivo baixado: " + downloadedFile.getAbsolutePath());
      return downloadedFile;
    } catch (Exception e) {
      throw new CrawlerException("Erro ao rodar crawler" + e);
    } finally {
      if (this.driver != null) {
        try {
          this.driver.quit();
          System.out.println("Driver fechado com sucesso.");
        } catch (Exception e) {
          throw new CrawlerException("Erro ao fechar driver", e);
        }
      }
    }
  }

  private File waitForDownload(File sessionDir) throws InterruptedException, CrawlerException, IOException {
    int retryCount = 10;
    File downloadDir = new File("/tmp/downloads");
    File downloadedFile = null;

    while (retryCount-- > 0) {
      File[] files = downloadDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
      if (files != null && files.length > 0) {
        downloadedFile = getMostRecentlyDownloadedFile(files);

        if (downloadedFile != null) {
          File destFile = new File(sessionDir, downloadedFile.getName());
          Files.move(downloadedFile.toPath(), destFile.toPath());
          downloadedFile = destFile;
        }
        break;
      }
      Thread.sleep(1000);
    }

    return downloadedFile;
  }

  private File getMostRecentlyDownloadedFile(File[] files) {
    File latestFile = files[0];
    for (File file : files) {
      if (file.lastModified() > latestFile.lastModified()) {
        latestFile = file;
      }
    }
    return latestFile;
  }
}
