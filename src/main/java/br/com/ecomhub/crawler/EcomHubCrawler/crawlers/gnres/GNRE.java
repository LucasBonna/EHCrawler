package br.com.ecomhub.crawler.EcomHubCrawler.crawlers.gnres;

import br.com.ecomhub.crawler.EcomHubCrawler.crawlers.Crawler;
import br.com.ecomhub.crawler.EcomHubCrawler.entities.GNREGenerateEntity;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.CrawlerException;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.Helpers;
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
import org.springframework.beans.factory.annotation.Autowired;
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

  private final CapMonster solver;
  private final Helpers helpers;
  private final GNREGenerateStates generateStates;

  @Autowired
  public GNRE(final CapMonster solver, final Helpers helpers, final GNREGenerateStates generateStates) {
    this.solver = solver;
    this.helpers = helpers;
    this.generateStates = generateStates;
  }

  public void setup(String url) {
    FirefoxOptions options = new FirefoxOptions();
//    options.addArguments("--headless");
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

  public File GNREGenerate(GNREGenerateEntity gnreGenerateEntity, File sessionDir) throws CrawlerException {
    System.out.println("comecou crawler");
    File downloadedFile = null;

      try {
        setup("https://www.gnre.pe.gov.br:444/gnre/v/guia/index");
        this.driver.get(this.url);
        String title = this.driver.getTitle();
        System.out.println(title);

        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ufFavorecida")));

        WebElement ufMenu = this.driver.findElement(By.id("ufFavorecida"));
        Select uf = new Select(ufMenu);
        uf.selectByValue(String.valueOf(gnreGenerateEntity.getDestUF()));

        WebElement gnreSimplesButton = this.driver.findElement(By.id("optGnreSimples"));
        gnreSimplesButton.click();

        WebElement notFavoredUFButton = this.driver.findElement(By.id("optNaoInscrito"));
        notFavoredUFButton.click();

        WebElement cnpjButton = this.driver.findElement(By.id("tipoCNPJ"));
        cnpjButton.click();

        WebElement cpfCpnjInput = this.driver.findElement(By.id("documentoEmitente"));
        cpfCpnjInput.sendKeys(gnreGenerateEntity.getEmitCNPJ());

        WebElement razaoInput = this.driver.findElement(By.id("razaoSocialEmitente"));
        razaoInput.sendKeys(gnreGenerateEntity.getEmitRazao());

        WebElement enderecoInput = this.driver.findElement(By.id("enderecoEmitente"));
        enderecoInput.sendKeys("Rua dos Emboabas, 25, Jardim Guerreiro");

        WebElement ufEmitMenu = this.driver.findElement(By.id("ufEmitente"));
        Select ufEmit = new Select(ufEmitMenu);
        ufEmit.selectByValue("SP");

        WebElement municipioMenu = this.driver.findElement(By.id("municipioEmitente"));
        Select municipio = new Select(municipioMenu);
        municipio.selectByVisibleText("COTIA");

        WebElement cepInput = this.driver.findElement(By.id("cepEmitente"));
        cepInput.sendKeys("06.710-520");

        WebElement phoneInput = this.driver.findElement(By.id("cepEmitente"));
        phoneInput.sendKeys("(11) 99167-6618");

        WebElement receitaMenu = this.driver.findElement(By.id("receita"));
        Select receita = new Select(receitaMenu);
        receita.selectByValue("100102");

//        WebElement docOrigemMenu = this.driver.findElement(By.id("tipoDocOrigem"));
//        Select docOrigem = new Select(docOrigemMenu);
//        docOrigem.selectByIndex(1);;
//
//        WebElement numDocOrigemInput = this.driver.findElement(By.id("numeroDocumentoOrigem"));
//        numDocOrigemInput.sendKeys(gnreGenerateEntity.getChaveNota());

        if (gnreGenerateEntity.getDestUF() == StateEnum.AC || gnreGenerateEntity.getDestUF() == StateEnum.AL) {
          generateStates.generateAcAl(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.AM || gnreGenerateEntity.getDestUF() == StateEnum.AP) {
          generateStates.generateAmAp(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.BA) {
          generateStates.generateBA(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.CE) {
          generateStates.generateCE(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.DF) {
          generateStates.generateDF(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.GO) {
          generateStates.generateGO(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.MA) {
          generateStates.generateMA(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.MS) {
          generateStates.generateMS(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.MT) {
          generateStates.generateMT(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.PA) {
          generateStates.generatePA(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.PB) {
          generateStates.generatePB(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.PE) {
          generateStates.generatePE(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.PI) {
          generateStates.generatePI(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.RN) {
          generateStates.generateRN(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.RO) {
          generateStates.generateRO(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.RR) {
          generateStates.generateRR(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.SC) {
          generateStates.generateSC(this.driver, gnreGenerateEntity);
        } else if (gnreGenerateEntity.getDestUF() == StateEnum.SE || gnreGenerateEntity.getDestUF() == StateEnum.TO) {
          generateStates.generateSeTo(this.driver, gnreGenerateEntity);
        }

        this.solver.setWebsiteKey("6LfvrIEaAAAAAJ7xrn1d7KvsMF_a27D2l8WsWFQk");
        this.solver.setWebsiteURL(this.driver.getCurrentUrl());
        this.solver.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
        String gRecaptchaResponse = this.solver.solveRecaptchaV2();
        System.out.println("solved: " + gRecaptchaResponse);

        if (gRecaptchaResponse == null) {
          throw new CrawlerException("Recaptcha failed");
        }

        System.out.println("Insere o token");
        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        js.executeScript("document.getElementById('g-recaptcha-response').innerHTML = arguments[0];", gRecaptchaResponse);

        System.out.println("clicando...");
        js.executeScript("onSubmit()");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("baixar")));

        WebElement baixarButton = this.driver.findElement(By.id("baixar"));
        baixarButton.click();

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

  public File GNREReceipt(String barcode, StateEnum uf, File sessionDir) throws CrawlerException {
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

      this.solver.setWebsiteKey(siteKey);
      this.solver.setWebsiteURL(this.driver.getCurrentUrl());
      this.solver.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
      String gRecaptchaResponse = this.solver.solveRecaptchaV2();
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
