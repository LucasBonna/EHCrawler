package br.com.ecomhub.crawler.EcomHubCrawler.entities;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GNREGenerateEntity {
    private String numNota;
    private String chaveNota;
    private String valorNota;
    private String emitRazao;
    private String emitCNPJ;
    private String destRazao;
    private String destCPFCNPJ;
    private String destMun;
    private StateEnum destUF;

    public GNREGenerateEntity() {
    }

    public GNREGenerateEntity(String numNota, String chaveNota, String valorNota, String emitRazao, String emitCNPJ, String destRazao, String destCPFCNPJ, String destMun, StateEnum destUF) {
        this.numNota = numNota;
        this.chaveNota = chaveNota;
        this.valorNota = valorNota;
        this.emitRazao = emitRazao;
        this.emitCNPJ = emitCNPJ;
        this.destRazao = destRazao;
        this.destCPFCNPJ = destCPFCNPJ;
        this.destMun = destMun;
        this.destUF = destUF;
    }
}
