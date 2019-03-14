package com.company;

import java.util.ArrayList;

public class Specimen {
    private double rating;
    private ArrayList<Integer> tspOrderList;

    Specimen(double rating, ArrayList<Integer> tspOrderList) {
        this.rating = rating;
        this.tspOrderList = tspOrderList;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<Integer> getTspOrderList() {
        return tspOrderList;
    }

    public void setTspOrderList(ArrayList<Integer> tspOrderList) {
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
}
