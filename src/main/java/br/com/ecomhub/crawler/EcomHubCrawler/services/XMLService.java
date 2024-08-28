package br.com.ecomhub.crawler.EcomHubCrawler.services;

import br.com.ecomhub.crawler.EcomHubCrawler.entities.GNREGenerateEntity;
import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import br.com.ecomhub.crawler.EcomHubCrawler.exceptions.XMLException;
import br.com.ecomhub.crawler.EcomHubCrawler.helpers.StateMapper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class XMLService {
    public GNREGenerateEntity extractXMLNFeData(File xmlFile) throws XMLException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            try (FileReader fileReader = new FileReader(xmlFile)) {
                Document doc = builder.parse(new InputSource(fileReader));

                doc.getDocumentElement().normalize();

                String numNota = getElementContent(doc, "nNF");
                String chaveNota = getElementContent(doc, "chNFe");
                String valorNota = getElementContent(doc, "vNF");

                Node emitNode = doc.getElementsByTagName("emit").item(0);
                String emitRazao = getChildNodeContent(emitNode, "xNome");
                String emitCNPJ = getChildNodeContent(emitNode, "CNPJ");

                Node destNode = doc.getElementsByTagName("dest").item(0);
                String destRazao = getChildNodeContent(destNode, "xNome");
                String destCPFCNPJ = getChildNodeContent(destNode, "CPF");
                if (destCPFCNPJ == null) {
                    destCPFCNPJ = getChildNodeContent(destNode, "CNPJ");
                }
                String destMun = getChildNodeContent(destNode, "xBairro");
                String destUF =  getChildNodeContent(destNode, "UF");

                StateEnum uf = StateEnum.valueOf(destUF);

                return new GNREGenerateEntity(numNota, chaveNota, valorNota, emitRazao, emitCNPJ, destRazao, destCPFCNPJ, destMun, uf);
            }


        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new XMLException("Erro ao extrair dados da XML");
        }

    }

    private String getElementContent(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private String getChildNodeContent(Node parentNode, String childNodeName) {
        NodeList nodeList = ((Element) parentNode).getElementsByTagName(childNodeName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
