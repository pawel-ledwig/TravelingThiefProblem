package com.company;

import java.io.File;

public class Main {

    private final static int headerLength = 8;

    public static void main(String[] args) {

        File file = new File("files/easy_4.ttp");

        FileParser fileParser = new FileParser(file, headerLength);
        fileParser.init();

        TTP ttp = fileParser.getTtp();
        ttp.setKnpTypeBestRatio();

        System.out.println(ttp.headersToString());
        System.out.println(ttp.nodesToString());
        System.out.println(ttp.itemsToString());

        ttp.createRandomTSP();
        System.out.println(ttp.tspToString());
        System.out.println(ttp.calculateKNP());
        System.out.println(ttp.velocityToString());
        System.out.println(ttp.calculateTravelTime());
        System.out.println(ttp.evaluate());

        GA ga = new GA(10, ttp);
        ga.createPopulation();
        System.out.println(ga.populationToString());
        System.out.println(ga.getBestRating());
        System.out.println(ga.getWorstRating());
        ga.makeCompetition();
        ga.swapGenerations();
        System.out.println(ga.getBestRating());
        System.out.println(ga.getWorstRating());
    }
}
