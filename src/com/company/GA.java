package com.company;

import java.util.ArrayList;
import java.util.Collections;

/*
Class GA is used to implement all needed genetic algorithms and solve a TTP problem
 */
class GA {

    // reference to created TTP object, which is storing all data from parsed file
    private TTP ttp;

    /*
    TSP is an array list used to store indexes of the nodes in specific order.
    Tt should be created randomly at the very beginning
    */
    private ArrayList<Integer> tsp;

    GA(TTP ttp){
        this.ttp = ttp;

        tsp = new ArrayList<>();
    }

    private double getDistanceBetweenNodes(int indexA, int indexB){
        /*
        Decrementing of index is needed due to way of indexing array and nodes:
        Arrays are indexed from 0 and nodes are indexed from 1
         */
        return ttp.getDistanceValue(indexA - 1, indexB - 1);
    }

    double calculateTravelTime(double velocity){
        double travelTime = 0.0;

        for (int i = 1 ; i < tsp.size(); i++){
            travelTime += getDistanceBetweenNodes(tsp.get(i - 1), tsp.get(i)) / velocity;
        }

        return travelTime;
    }

    // fill tsp array with randomly placed indexes of nodes
    void createRandomTSP(){
        tsp.clear();

        // fill an ArrayList with indexes of available nodes (1..N)
        for (int i = 1 ; i <= ttp.getDimensionsNumber(); i++){
            tsp.add(i);
        }

        // swap indexes randomly
        Collections.shuffle(tsp);
    }


    // toString methods zone

    String tspToString(){
        StringBuilder result = new StringBuilder();

        for (int index : tsp){
            result.append(index).append(" ");
        }

        return result.toString();
    }
}
