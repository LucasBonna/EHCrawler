package br.com.ecomhub.crawler.EcomHubCrawler.helpers;

import br.com.ecomhub.crawler.EcomHubCrawler.enums.StateEnum;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class StateMapper {
    public static final Map<StateEnum, Integer> ufIndexMap = new EnumMap<>(StateEnum.class);

    static {
        ufIndexMap.put(StateEnum.AC, 1);
        ufIndexMap.put(StateEnum.AL, 2);
        ufIndexMap.put(StateEnum.AM, 3);
        ufIndexMap.put(StateEnum.AP, 4);
        ufIndexMap.put(StateEnum.BA, 5);
        ufIndexMap.put(StateEnum.CE, 6);
        ufIndexMap.put(StateEnum.DF, 7);
        ufIndexMap.put(StateEnum.GO, 8);
        ufIndexMap.put(StateEnum.MA, 9);
        ufIndexMap.put(StateEnum.MT, 10);
        ufIndexMap.put(StateEnum.MS, 11);
        ufIndexMap.put(StateEnum.MG, 12);
        ufIndexMap.put(StateEnum.PA, 13);
        ufIndexMap.put(StateEnum.PB, 14);
        ufIndexMap.put(StateEnum.PR, 15);
        ufIndexMap.put(StateEnum.PE, 16);
        ufIndexMap.put(StateEnum.PI, 17);
        ufIndexMap.put(StateEnum.RJ, 18);
        ufIndexMap.put(StateEnum.RN, 19);
        ufIndexMap.put(StateEnum.RS, 20);
        ufIndexMap.put(StateEnum.RO, 21);
        ufIndexMap.put(StateEnum.RR, 22);
        ufIndexMap.put(StateEnum.SC, 23);
        ufIndexMap.put(StateEnum.SP, 24);
        ufIndexMap.put(StateEnum.SE, 25);
        ufIndexMap.put(StateEnum.TO, 26);
    }
}
