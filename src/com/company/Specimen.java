package com.company;

import java.util.ArrayList;

public class Specimen {
    private double rating;
    private ArrayList<Integer> tspOrderList;

    Specimen(double rating, ArrayList<Integer> tspOrderList) {
        this.rating = rating;
        this.tspOrderList = tspOrderList;
    }

    Specimen(Specimen bestSpecimen) {
        this.rating = bestSpecimen.getRating();
        this.tspOrderList = new ArrayList<>(bestSpecimen.getTspOrderList());
    }

    double getRating() {
        return rating;
    }

    void setRating(double rating) {
        this.rating = rating;
    }

    ArrayList<Integer> getTspOrderList() {
        return tspOrderList;
    }

    void setTspOrderList(ArrayList<Integer> tspOrderList) {
        this.tspOrderList = tspOrderList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("rating: ").append(rating).append(", ");
        result.append("nodes: ");

        for (int node : tspOrderList) {
            result.append(node).append(", ");
        }

        result.append("\n");

        return result.toString();
    }

    @Override
    public boolean equals(Object o){
        if (getClass() != o.getClass())
            return false;

        return tspOrderList.equals(((Specimen) o).tspOrderList);
    }
}
