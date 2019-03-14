package com.company;

import java.util.ArrayList;

/*
Class GA is used to implement all needed genetic algorithms and solve a TTP problem
 */
class GA {
    private int populationSize;
    private int generationsNumber;
    private ArrayList<Specimen> specimensList;

    private TTP ttp;

    GA (int populationSize, TTP ttp){
        this.populationSize = populationSize;
        this.ttp = ttp;

        specimensList = new ArrayList<>();
    }

    double getBestRating(){
        double bestRating = specimensList.get(0).getRating();

        for (Specimen specimen : specimensList){
            if (specimen.getRating() > bestRating){
                bestRating = specimen.getRating();
            }
        }

        return bestRating;
    }

    double getWorstRating(){
        double worstRating = specimensList.get(0).getRating();

        for (Specimen specimen : specimensList){
            if (specimen.getRating() < worstRating){
                worstRating = specimen.getRating();
            }
        }

        return worstRating;
    }

    /*
    Create random population
     */
    void createPopulation(){
        for (int i = 0; i < populationSize; i++){
            specimensList.add(ttp.createRandomSpecimen());
        }
    }

    String populationToString(){
        StringBuilder result = new StringBuilder();

        for(Specimen specimen : specimensList){
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
