package com.company;

import java.io.File;

public class Main {

    private final static int headerLength = 8;

    public static void main(String[] args) {

        File file = new File("files/easy_4.ttp");

        FileParser fileParser = new FileParser(file, headerLength);
        fileParser.init();

        TTP ttp = fileParser.getTtp();

        System.out.println(ttp.headersToString());
        System.out.println(ttp.nodesToString());
        System.out.println(ttp.itemsToString());

        GA ga = new GA(ttp);
        ga.createRandomTSP();
        System.out.println(ga.tspToString());

    }
}
