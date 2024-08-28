package br.com.ecomhub.crawler.EcomHubCrawler.crawlers.gnres;

import br.com.ecomhub.crawler.EcomHubCrawler.entities.GNREGenerateEntity;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.Helpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class GNREGenerateStates {

    @Autowired
    private Helpers helpers;

    public void generateAcAl(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 2);

        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());

        if (entity.getDestUF() == StateEnum.AL) {
            inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
        } else {
            inputText(driver, "campoAdicional01", 10, entity.getChaveNota());
        }
    }

    public void generateAmAp(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);

        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "periodo", 10, 1);

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());

        if (entity.getDestUF() == StateEnum.AP) {
            inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
        }
    }

    public void generateBA(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "periodo", 10, 1);
        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generateCE(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 2);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        inputText(driver, "dataVencimento", 10, helpers.getTodaysDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());
    }

    public void generateDF(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 2);

        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());
    }


    public void generateGO(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 2);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generateMA(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByVisibleText(driver, "produto", 10, "Outros");
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 2);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        clickButton(driver, "tipoValorTotal", 10);

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generateMS(WebDriver driver, GNREGenerateEntity entity) {
        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generateMT(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "detalheReceita", 10, 1);

        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        clickButton(driver, "tipoValorTotal", 10);
        inputText(driver, "valor", 10, entity.getValorNota());
    }

    public void generatePA(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 2);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        clickButton(driver, "tipoValorTotal", 10);
        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());
    }

    public void generatePB(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generatePE(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());
    }

    public void generatePI(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        clickButton(driver, "tipoValorTotal", 10);
        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());
    }

    public void generateRN(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "convenio", 10, helpers.getConvenio());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByValue(driver, "municipioDestinatario", 10, entity.getDestMun());
    }

    public void generateRO(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generateRR(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getNumNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        clickButton(driver, "tipoValorTotal", 10);
        inputText(driver, "valor", 10, entity.getValorNota());

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    public void generateSC(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getChaveNota());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());
    }

    public void generateSeTo(WebDriver driver, GNREGenerateEntity entity) {
        selectMenuByIndex(driver, "tipoDocOrigem", 10, 1);
        inputText(driver, "numeroDocumentoOrigem", 10, entity.getNumNota());

        selectMenuByIndex(driver, "mesReferencia", 10, helpers.getCurrentMonth());
        selectMenuByValue(driver, "anoReferencia", 10, helpers.getCurrentYear());

        inputText(driver, "dataVencimento", 10, helpers.getTommorowDate());

        inputText(driver, "valor", 10, entity.getValorNota());

        clickButton(driver, "optNaoInscritoDest", 10);

        if (entity.getDestCPFCNPJ().length() > 11) {
            clickButton(driver, "tipoCNPJDest", 10);
        } else {
            clickButton(driver, "tipoCPFDest", 10);
        }

        inputText(driver, "documentoDestinatario", 10, entity.getDestCPFCNPJ());

        inputText(driver, "razaoSocialDestinatario", 10, entity.getDestRazao());

        selectMenuByIndex(driver, "municipioDestinatario", 10, 2);

        inputText(driver, "campoAdicional00", 10, entity.getChaveNota());
    }

    private void clickButton(WebDriver driver, String id, int await) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(await));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement button = driver.findElement(By.id(id));
        button.click();
    }

    private void inputText(WebDriver driver, String id, int await, String value) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(await));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement element = driver.findElement(By.id(id));
        element.sendKeys(value);
    }

    private void selectMenuByValue(WebDriver driver, String id, int await, String value) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(await));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement element = driver.findElement(By.id(id));
        Select elementSelect = new Select(element);
        elementSelect.selectByValue(value);
    }

    private void selectMenuByVisibleText(WebDriver driver, String id, int await, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(await));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement element = driver.findElement(By.id(id));
        Select elementSelect = new Select(element);
        elementSelect.selectByVisibleText(text);
    }


    private void selectMenuByIndex(WebDriver driver, String id, int await, int index) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(await));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement element = driver.findElement(By.id(id));
        Select elementSelect = new Select(element);
        elementSelect.selectByIndex(index);
    }
}
