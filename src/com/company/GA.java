package com.company;

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
