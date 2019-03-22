package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private final static int headerLength = 8;

    public static void main(String[] args) {

        File file = new File("files/medium_4.ttp");

        FileParser fileParser = new FileParser(file, headerLength);
        fileParser.init();

        examineAll();

        // Runnable runnable_population    = () -> examinePopulationSize("easy_1");
        // Runnable runnable_generation    = () -> examineGenerationNumber("easy_1");
        // Runnable runnable_factor_mx     = () -> examineFactorMX("easy_1");
        // Runnable runnable_factor_mm     = () -> examineFactorMM("easy_1");
        // Runnable runnable_tournament    = () -> examineTournament("easy_1");
//
        // Thread t_population     = new Thread(runnable_population);
        // Thread t_generation     = new Thread(runnable_generation);
        // Thread t_factor_mx      = new Thread(runnable_factor_mx);
        // Thread t_factor_mm      = new Thread(runnable_factor_mm);
        // Thread t_tournament     = new Thread(runnable_tournament);
//
        // t_population.start();
        // t_generation.start();
        // t_factor_mx.start();
        // t_factor_mm.start();
        // t_tournament.start();

        // examinePopulationSize("easy_1");
        // examineGenerationNumber("easy_1");
        // examineFactorMX("easy_1");
        // examineFactorMM("easy_1");
        // examineTournament("easy_1");

        // TTP ttp = fileParser.getTtp();
        // ttp.setKnpTypeBestRatio();

        //System.out.println(ttp.headersToString());
        //System.out.println(ttp.nodesToString());
        //System.out.println(ttp.itemsToString());

        // ttp.createRandomTSP();
        //System.out.println(ttp.tspToString());
        //System.out.println(ttp.calculateKNP());
        //System.out.println(ttp.velocityToString());
        //System.out.println(ttp.calculateTravelTime());
        //System.out.println(ttp.evaluate());

        //GA ga = new GA(100, 100, ttp);
        //ga.setCrossoverProbability(0.7);
        //ga.setMutationProbability(0.3);
        //ga.setTournamentSize(5);
//
        //ga.createPopulation();
        ////System.out.println(ga.populationToString());
        //System.out.println("Best: " + ga.getBestRating());
        //System.out.println("Avg: " + ga.getAvgRating());
        //System.out.println("Worst: " + ga.getWorstRating());
        //ga.makeGenerationsByClassicTournament();
        //System.out.println();
        //System.out.println("Best: " + ga.getBestRating());
        //System.out.println("Avg: " + ga.getAvgRating());
        //System.out.println("Worst: " + ga.getWorstRating());
        //ga.resultsToFile();
        //System.out.println(ga.populationToString());
        //System.out.println(ga.getWorstRating());
    }

    private static void examineAll() {
        ArrayList<String> filenames = new ArrayList<>();
        filenames.add("easy_2");
        filenames.add("easy_3");
        filenames.add("medium_1");
        filenames.add("medium_2");
        filenames.add("hard_1");

        for (String filename : filenames) {
            Runnable runnable_population    = () -> examinePopulationSize(filename);
            Runnable runnable_generation    = () -> examineGenerationNumber(filename);
            Runnable runnable_factor_mx     = () -> examineFactorMX(filename);
            Runnable runnable_factor_mm     = () -> examineFactorMM(filename);
            Runnable runnable_tournament    = () -> examineTournament(filename);

            Thread t_population     = new Thread(runnable_population);
            Thread t_generation     = new Thread(runnable_generation);
            Thread t_factor_mx      = new Thread(runnable_factor_mx);
            Thread t_factor_mm      = new Thread(runnable_factor_mm);
            Thread t_tournament     = new Thread(runnable_tournament);

            t_population.start();
            t_generation.start();
            t_factor_mx.start();
            t_factor_mm.start();
            t_tournament.start();

            try {
                t_factor_mm.join();
                t_factor_mx.join();
                t_tournament.join();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<Double> getAverageValues(String filename, int iterations, int populationSize, int generationsNumber, double mx, double mm, int tournament){
        ArrayList<Double> listOfBest = new ArrayList<>();
        ArrayList<Double> listOfAvg = new ArrayList<>();
        ArrayList<Double> listOfWorst = new ArrayList<>();

        File file = new File("files/" + filename + ".ttp");

        FileParser fileParser = new FileParser(file, headerLength);
        fileParser.init();

        TTP ttp = fileParser.getTtp();
        ttp.setKnpTypeBestRatio();

        for (int i = 0; i < iterations; i++){
            ttp.createRandomTSP();

            GA ga = new GA(populationSize, generationsNumber, ttp);
            ga.setCrossoverProbability(mx);
            ga.setMutationProbability(mm);
            ga.setTournamentSize(tournament);

            ga.createPopulation();
            ga.makeGenerationsByClassicTournament();

            listOfBest.add(ga.getBestRating());
            listOfAvg.add(ga.getAvgRating());
            listOfWorst.add(ga.getWorstRating());

            // System.out.println("Calculating avg: " + (i + 1) + "/" + iterations);
        }

        ArrayList<Double> results = new ArrayList<>();

        double sum = 0.0;
        for (double d : listOfBest){
            sum += d;
        }
        results.add(sum / iterations);

        sum = 0.0;
        for (double d : listOfAvg){
            sum += d;
        }
        results.add(sum / iterations);

        sum = 0.0;
        for (double d : listOfWorst){
            sum += d;
        }
        results.add(sum / iterations);

        return results;
    }

    private static void examinePopulationSize(String filename){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/" + filename + "_population_size.txt"));
            bw.write("GENERATIONS: 200, MX: 0.7, MM: 0.2, TOUR: 5");
            bw.newLine();

            bw.write("POP_SIZE;BEST;AVG;WORST");
            bw.newLine();

            for (int i = 1; i < 21; i++){
                ArrayList<Double> results = getAverageValues(filename, 5, 100 * i, 200, 0.7, 0.2, 5);
                bw.write(100 * i + ";" + results.get(0) + ";" + results.get(1) + ";" + results.get(2));
                bw.newLine();

                System.out.println(filename + ": examining population " + i + "/" + 20);
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void examineGenerationNumber(String filename){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/" + filename + "_generation_number.txt"));
            bw.write("POPULATION: 250, MX: 0.7, MM: 0.2, TOUR: 5");
            bw.newLine();

            bw.write("GEN_NUM;BEST;AVG;WORST");
            bw.newLine();

            for (int i = 1; i < 21; i++){
                ArrayList<Double> results = getAverageValues(filename, 5, 250, 100 * i, 0.7, 0.2, 5);
                bw.write(100 * i + ";" + results.get(0) + ";" + results.get(1) + ";" + results.get(2));
                bw.newLine();

                System.out.println(filename + ": examining generation " + i + "/" + 20);
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void examineFactorMX(String filename){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/" + filename + "_factor_mx.txt"));
            bw.write("POPULATION: 250, GENERATION: 200, MM: 0.2, TOUR: 5");
            bw.newLine();

            bw.write("MX;BEST;AVG;WORST");
            bw.newLine();

            for (int i = 0; i < 14; i++){
                ArrayList<Double> results = getAverageValues(filename, 5, 250, 200, 0.05 * i + 0.1, 0.2, 5);
                bw.write((0.05 * i + 0.1) + ";" + results.get(0) + ";" + results.get(1) + ";" + results.get(2));
                bw.newLine();

                System.out.println(filename + ": examining factor MX " + i + "/" + 13);
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void examineFactorMM(String filename){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/" + filename + "_factor_mm.txt"));
            bw.write("POPULATION: 250, GENERATION: 200, MX: 0.7, TOUR: 5");
            bw.newLine();

            bw.write("MX;BEST;AVG;WORST");
            bw.newLine();

            for (int i = 0; i < 14; i++){
                ArrayList<Double> results = getAverageValues(filename, 5, 250, 200, 0.7,0.05 * i + 0.1, 5);
                bw.write((0.05 * i + 0.1) + ";" + results.get(0) + ";" + results.get(1) + ";" + results.get(2));
                bw.newLine();

                System.out.println(filename + ": examining factor MM " + i + "/" + 13);
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void examineTournament(String filename){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/" + filename + "_tournament.txt"));
            bw.write("POPULATION: 250, GENERATION: 200, MX: 0.7, MM: 0.2");
            bw.newLine();

            bw.write("TOUR;BEST;AVG;WORST");
            bw.newLine();

            for (int i = 2; i < 16; i++){
                ArrayList<Double> results = getAverageValues(filename, 5, 250, 200, 0.7,0.2, i);
                bw.write(i + ";" + results.get(0) + ";" + results.get(1) + ";" + results.get(2));
                bw.newLine();

                System.out.println(filename + ": examining tournament " + i + "/" + 15);
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
