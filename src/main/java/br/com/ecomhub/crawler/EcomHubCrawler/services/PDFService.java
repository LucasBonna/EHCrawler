package br.com.ecomhub.crawler.EcomHubCrawler.services;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREReceiptDTO;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.GNREEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.PDFException;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.StateMapper;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Service
@Setter
public class PDFService {
    private File pdf;

    public PDFService() {

    }

    public PDFService(File pdf) {
        this.pdf = pdf;
    }

    public GNREReceiptDTO getInfo(File file, GNREEnum gnreType) throws PDFException {
        try {
            PDDocument pdf = PDDocument.load(file);
            GNREReceiptDTO dto = null;
            switch (gnreType) {
                case SHOPEE:
                    dto = extractGNREPDFInfo(pdf, 60, 205,250, 10, 410, 555, 15, 10);
                case ML:
//                    extractGNREPDFInfo()
            }

            return dto;
        } catch (IOException e) {
            throw new PDFException("Erro extraindo informações do PDF");
        }
    }

    private GNREReceiptDTO extractGNREPDFInfo(PDDocument doc, int bX, int bY, int bW, int bH, int ufX, int ufY, int ufW, int ufH) throws PDFException {
        try {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);

            Rectangle barcodeArea = new Rectangle(bX, bY, bW, bH);
            Rectangle ufArea = new Rectangle(ufX, ufY, ufW, ufH);

            stripper.addRegion("barcode", barcodeArea);
            stripper.addRegion("uf", ufArea);

            stripper.extractRegions(doc.getPage(0));

            String barcode = stripper.getTextForRegion("barcode").trim();
            String uf = stripper.getTextForRegion("uf").trim();

            String treatedBarcode = barcode.replaceAll("\\s+", "");
            if (treatedBarcode.length() != 48 && uf.length() != 2) {
                throw new PDFException("Código de barras ou UF inválidos");
            }

            StateEnum ufEnum = StateEnum.valueOf(uf);
            doc.close();
            return new GNREReceiptDTO(treatedBarcode, ufEnum);
        } catch (IOException e) {
            throw new PDFException("Erro ao extrair informações do PDF");
        }
    }
}
