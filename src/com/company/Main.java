package com.company;

import java.io.File;

public class Main {

    private final static int headerLength = 8;

    public static void main(String[] args) {

        File file = new File("files/easy_1.ttp");

        FileParser fileParser = new FileParser(file, headerLength);
        fileParser.parseHeader();
        fileParser.setNodeMap();

        TTP ttp = fileParser.getTtp();

        System.out.println(ttp.getHeaders());
        System.out.println(ttp.getNodeMap().getNodesListText());

    }
}
