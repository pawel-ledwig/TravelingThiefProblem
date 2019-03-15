package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
Class GA is used to implement all needed genetic algorithms and solve a TTP problem
 */
class GA {
    private int populationSize;
    private int generationsNumber;

    private double crossoverProbability;
    private double mutationProbability;

    private ArrayList<Specimen> specimensGenerationCurrent;
    private ArrayList<Specimen> specimensGenerationNext;

    private ArrayList<Double> bestRatings;
    private ArrayList<Double> avgRatings;
    private ArrayList<Double> worstRatings;

    private TTP ttp;

    GA (int populationSize, int generationsNumber, TTP ttp){
        this.populationSize     = populationSize;
        this.generationsNumber  = generationsNumber;

        this.ttp = ttp;

        specimensGenerationCurrent  = new ArrayList<>();
        specimensGenerationNext     = new ArrayList<>();

        bestRatings  = new ArrayList<>();
        avgRatings   = new ArrayList<>();
        worstRatings = new ArrayList<>();
    }

    void makeGenerations(){
        for (int i = 0; i < generationsNumber; i++){
            makeCompetition();
            swapGenerations();
            fillPopulation();
            mutatePopulation();

            bestRatings.add(getBestRating());
            avgRatings.add(getAvgRating());
            worstRatings.add(getWorstRating());
        }
    }

    private void mutation(Specimen specimen){
        Random r = new Random();

        int nodeA = r.nextInt(ttp.getDimensionsNumber());
        int nodeB = r.nextInt(ttp.getDimensionsNumber());

        int tmp = specimen.getTspOrderList().get(nodeA);
        specimen.getTspOrderList().set(nodeA, specimen.getTspOrderList().get(nodeB));
        specimen.getTspOrderList().set(nodeB, tmp);
    }

    private void mutatePopulation(){
        for (Specimen specimen : specimensGenerationCurrent){
            Random r = new Random();

            if (r.nextDouble() < mutationProbability){
                mutation(specimen);
            }
        }
    }

    /*
    Method used to fill population with children up to max population size.
     */
    private void fillPopulation(){
        int parents = specimensGenerationCurrent.size();

        while (specimensGenerationCurrent.size() < populationSize){
            Random r = new Random();

            if (r.nextDouble() < crossoverProbability){
                int parentA = r.nextInt(parents);
                int parentB = r.nextInt(parents);

                int startPoint = Math.max(r.nextInt(ttp.getDimensionsNumber()) - 1, 0);
                int endPoint = Math.min(startPoint + r.nextInt(ttp.getDimensionsNumber() / 10), ttp.getDimensionsNumber() - 1);

                specimensGenerationCurrent.add(
                        createChildPMX(specimensGenerationCurrent.get(parentA), specimensGenerationCurrent.get(parentB), startPoint, endPoint)
                );
            }
        }
    }

    /*
    Creating a child from two parents using PMX method
     */
    private Specimen createChildPMX(Specimen parentA, Specimen parentB, int startPoint, int endPoint){
        ArrayList<Integer> subArrayA = new ArrayList<>();
        ArrayList<Integer> subArrayB = new ArrayList<>();

        /*
        Child array at the beginning is made from parentA array
         */
        ArrayList<Integer> childArray = new ArrayList<>(parentA.getTspOrderList());

        /*
        Get elements which will be used to crossover
         */
        for (int i = startPoint; i < endPoint; i++){
            subArrayA.add(parentA.getTspOrderList().get(i));
            subArrayB.add(parentB.getTspOrderList().get(i));
        }

        /*
        Replace all nodes from subArrayB with nodes from subArrayA in childArray
         */
        for (int i = 0; i < subArrayB.size(); i++){
            for (int j = 0; j < childArray.size(); j++){
                if (subArrayB.get(i).equals(childArray.get(j))){
                    childArray.set(j, subArrayA.get(i));
                }
            }
        }

        /*
        Replace block of nodes from startPoint to endPoint with nodes from subArrayB
         */
        for (int i = startPoint; i < endPoint; i++){
            childArray.set(i, subArrayB.get(i - startPoint));
        }

        /*
        Update TTP object with new TSP, needed to evaluate it
         */
        ttp.setTspOrderList(new ArrayList<>(childArray));

        /*
        Evaluate new Specimen and return
         */
        return ttp.createSpecimenFromCurrent();
    }

    double getBestRating(){
        double bestRating = specimensGenerationCurrent.get(0).getRating();

        for (Specimen specimen : specimensGenerationCurrent){
            if (specimen.getRating() > bestRating){
                bestRating = specimen.getRating();
            }
        }

        return bestRating;
    }

    double getAvgRating(){
        double sumRating = 0.0;

        for (Specimen specimen : specimensGenerationCurrent){
            sumRating += specimen.getRating();
        }

        return sumRating / specimensGenerationCurrent.size();
    }

    double getWorstRating(){
        double worstRating = specimensGenerationCurrent.get(0).getRating();

        for (Specimen specimen : specimensGenerationCurrent){
            if (specimen.getRating() < worstRating){
                worstRating = specimen.getRating();
            }
        }

        return worstRating;
    }

    private void makeCompetition(){
        for (int i = 0 ; i < populationSize / 2; i++){
            if (specimensGenerationCurrent.get(i * 2).getRating() > specimensGenerationCurrent.get(i * 2 + 1).getRating()) {
                specimensGenerationNext.add(specimensGenerationCurrent.get(i * 2));
            } else {
                specimensGenerationNext.add(specimensGenerationCurrent.get(i * 2 + 1));
            }
        }
    }

    private void swapGenerations(){
        specimensGenerationCurrent = new ArrayList<>(specimensGenerationNext);
        specimensGenerationNext.clear();
    }

    /*
    Create random population
     */
    void createPopulation(){
        for (int i = 0; i < populationSize; i++){
            specimensGenerationCurrent.add(ttp.createRandomSpecimen());
        }
    }

    String populationToString(){
        StringBuilder result = new StringBuilder();

        for(Specimen specimen : specimensGenerationCurrent){
            result.append(specimen);
        }

        return result.toString();
    }

    void resultsToFile(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/GeneticAlgorithm.csv"));
            bw.write("ID;Best;Avg;Worst");
            bw.newLine();
            for (int i = 0 ; i < bestRatings.size(); i++){
                bw.write(i + ";" + Math.round(bestRatings.get(i)) + ";" + Math.round(avgRatings.get(i)) + ";" + Math.round(worstRatings.get(i)) + ";");
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    --------------------------------------------------------------------------------------------------
    */

    public int getGenerationsNumber() {
        return generationsNumber;
    }

    public void setGenerationsNumber(int generationsNumber) {
        this.generationsNumber = generationsNumber;
    }

    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }
}
