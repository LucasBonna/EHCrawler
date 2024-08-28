package br.com.ecomhub.crawler.EcomHubCrawler.services;

import br.com.ecomhub.crawler.EcomHubCrawler.DTOs.GNREGenerateDTO;
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
@Setter
public class PDFService {
    public GNREReceiptDTO getReceiptInfo(File file, GNREEnum gnreType) throws PDFException {
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

    public List<GNREGenerateDTO> getGenerateInfo(File file) throws PDFException {
        try {
            List<GNREGenerateDTO> gnreGenerateDTOs = new ArrayList<>();

            PDDocument pdf = PDDocument.load(file);
            int numPages = pdf.getNumberOfPages();
            System.out.println("numPages: " + numPages);

            for (int i = 0; i < numPages; i++) {
                GNREGenerateDTO dto = extractGNREGenInfo(pdf, i);
                System.out.println("dto" + dto);
                EnumSet<StateEnum> excludeStates = EnumSet.of(StateEnum.SP, StateEnum.RJ, StateEnum.RS, StateEnum.PR, StateEnum.MG);
                if (!excludeStates.contains(dto.uf())) {
                    gnreGenerateDTOs.add(dto);
                }
            }

            if (gnreGenerateDTOs.isEmpty()) {
                System.out.println("caiu error");
                throw new PDFException("Erro ao extrair informações do PDF");
            }

            System.out.println("gnreGenerateDTOs" + gnreGenerateDTOs);
            return gnreGenerateDTOs;
        } catch (IOException e) {
            throw new PDFException("Erro extraindo informacoes do PDF");
        }
    }

    private GNREReceiptDTO extractGNREPDFInfo(PDDocument doc, int bX, int bY, int bW, int bH, int ufX, int ufY, int ufW, int ufH) throws PDFException, IOException {
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
            return new GNREReceiptDTO(treatedBarcode, ufEnum);
        } catch (IOException e) {
            throw new PDFException("Erro ao extrair informações do PDF");
        } finally {
            doc.close();
        }
    }

    private GNREGenerateDTO extractGNREGenInfo(PDDocument doc, int pageIndex) throws PDFException{
        try {
            PDFTextStripperByArea stripper = getPdfTextStripperByArea();

            System.out.println("pageIndex: " + pageIndex);

            stripper.extractRegions(doc.getPage(pageIndex));

            String barcode = stripper.getTextForRegion("barcode").trim().replaceAll("\\s+", "");
            String uf = stripper.getTextForRegion("uf").trim();
            String num = stripper.getTextForRegion("numNF").trim().replaceAll("[^0-9]", "");
            String preco = stripper.getTextForRegion("preco").trim();
            String destCNPJ = stripper.getTextForRegion("destCNPJ").trim();
            String destCEP = stripper.getTextForRegion("destCEP").trim();
            String destRazao = stripper.getTextForRegion("destRazao").trim();
            String destMun = stripper.getTextForRegion("destMun").trim();

            System.out.println("data" + barcode + uf + num + preco + destCNPJ + destCEP + destRazao + destMun);
            StateEnum ufEnum = StateEnum.valueOf(uf);

            System.out.println(barcode);

            return new GNREGenerateDTO(barcode, ufEnum, num, preco, destCNPJ, destCEP, destRazao, destMun);
        } catch (IOException e) {
            throw new PDFException("Erro ao extrair informações do PDF");
        }
    }

    private static PDFTextStripperByArea getPdfTextStripperByArea() throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        Rectangle barcodeArea = new Rectangle(340, 144, 210, 11);
        Rectangle numNFArea = new Rectangle(520, 45, 40, 17);
        Rectangle precoNFArea = new Rectangle(310, 470, 30, 10);
        Rectangle ufArea = new Rectangle(250, 290, 100, 10);
        Rectangle destCNPJArea = new Rectangle(290, 252, 100, 12);
        Rectangle destCEPArea = new Rectangle(405, 270, 60, 12);
        Rectangle destRazaoArea = new Rectangle(30, 252, 180, 12);
        Rectangle destMunArea = new Rectangle(30, 290, 180, 12);

        stripper.addRegion("barcode", barcodeArea);
        stripper.addRegion("uf", ufArea);
        stripper.addRegion("numNF", numNFArea);
        stripper.addRegion("preco", precoNFArea);
        stripper.addRegion("destCNPJ", destCNPJArea);
        stripper.addRegion("destCEP", destCEPArea);
        stripper.addRegion("destRazao", destRazaoArea);
        stripper.addRegion("destMun", destMunArea);
        return stripper;
    }
}
