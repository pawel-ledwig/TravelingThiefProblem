package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
Class GA is used to implement all needed genetic algorithms and solve a TTP problem
 */
class GA {
    private int populationSize;
    private int generationsNumber;
    private ArrayList<Specimen> specimensGenerationCurrent;
    private ArrayList<Specimen> specimensGenerationNext;

    private TTP ttp;

    GA (int populationSize, TTP ttp){
        this.populationSize = populationSize;
        this.ttp = ttp;

        specimensGenerationCurrent = new ArrayList<>();
        specimensGenerationNext = new ArrayList<>();
    }

    /*
    Method used to fill population with children up to max population size.
     */
    void fillPopulation(){
        specimensGenerationCurrent.add(createChildPMX(specimensGenerationCurrent.get(0), specimensGenerationCurrent.get(1), 2, 5));
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

    double getWorstRating(){
        double worstRating = specimensGenerationCurrent.get(0).getRating();

        for (Specimen specimen : specimensGenerationCurrent){
            if (specimen.getRating() < worstRating){
                worstRating = specimen.getRating();
            }
        }

        return worstRating;
    }

    void makeCompetition(){
        for (int i = 0 ; i < populationSize / 2; i++){
            if (specimensGenerationCurrent.get(i * 2).getRating() > specimensGenerationCurrent.get(i * 2 + 1).getRating()) {
                specimensGenerationNext.add(specimensGenerationCurrent.get(i * 2));
            } else {
                specimensGenerationNext.add(specimensGenerationCurrent.get(i * 2 + 1));
            }
        }
    }

    void swapGenerations(){
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

    /*
    --------------------------------------------------------------------------------------------------
    */

    public int getGenerationsNumber() {
        return generationsNumber;
    }

    public void setGenerationsNumber(int generationsNumber) {
        this.generationsNumber = generationsNumber;
    }
}
