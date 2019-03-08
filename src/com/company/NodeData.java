package com.company;

class NodeData {
    private int index;

    private double X;
    private double Y;

    NodeData (int newIndex, double newX, double newY){
        index = newIndex;

        X = newX;
        Y = newY;
    }

    int getIndex(){
        return index;
    }

    double getX(){
        return X;
    }

    double getY(){
        return Y;
    }
}
