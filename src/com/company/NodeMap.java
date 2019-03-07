package com.company;

import java.util.ArrayList;

public class NodeMap {
    private ArrayList<Double> CoordinateX;
    private ArrayList<Double> CoordinateY;

    private int dimensions;

    NodeMap() {
        CoordinateX = new ArrayList<>();
        CoordinateY = new ArrayList<>();

        dimensions = 0;
    }

    void addPoint(double X, double Y){
        CoordinateX.add(X);
        CoordinateY.add(Y);

        dimensions++;
    }

    ArrayList<Double> getCoordinateX() {
        return CoordinateX;
    }

    ArrayList<Double> getCoordinateY() {
        return CoordinateY;
    }

    int getDimensions(){
        return dimensions;
    }

    String getNodesListText(){
        String list = "";
            for (int i = 0 ; i < dimensions ; i++){
                list += i + 1 + ": " + CoordinateX.get(i) + ", " + CoordinateY.get(i) + "\n";
            }
        return list;
    }
}
