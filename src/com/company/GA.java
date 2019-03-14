package com.company;

import java.util.ArrayList;

/*
Class GA is used to implement all needed genetic algorithms and solve a TTP problem
 */
class GA {
    private int populationSize;
    private ArrayList<Specimen> specimensList;

    private TTP ttp;

    GA (int populationSize, TTP ttp){
        this.populationSize = populationSize;
        this.ttp = ttp;

        specimensList = new ArrayList<>();
    }

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
}
