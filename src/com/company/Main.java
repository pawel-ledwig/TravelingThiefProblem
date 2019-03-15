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

        //System.out.println(ttp.headersToString());
        //System.out.println(ttp.nodesToString());
        //System.out.println(ttp.itemsToString());

        ttp.createRandomTSP();
        //System.out.println(ttp.tspToString());
        //System.out.println(ttp.calculateKNP());
        //System.out.println(ttp.velocityToString());
        //System.out.println(ttp.calculateTravelTime());
        //System.out.println(ttp.evaluate());

        GA ga = new GA(100, 100, ttp);
        ga.setCrossoverProbability(0.7);
        ga.setMutationProbability(0.3);

        ga.createPopulation();
        System.out.println(ga.populationToString());
        System.out.println("Best: " + ga.getBestRating());
        System.out.println("Avg: " + ga.getAvgRating());
        System.out.println("Worst: " + ga.getWorstRating());
        ga.makeGenerations();
        System.out.println("Best: " + ga.getBestRating());
        System.out.println("Avg: " + ga.getAvgRating());
        System.out.println("Worst: " + ga.getWorstRating());
        ga.resultsToFile();
        //System.out.println(ga.populationToString());
        //System.out.println(ga.getWorstRating());
    }
}
