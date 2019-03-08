package com.company;

import java.util.ArrayList;

/*
Class TTP is used to store all data from parsed file
 */
public class TTP {
    private String problemName;
    private String knapsackType;

    private int dimensionsNumber;
    private int itemsNumber;
    private int capacity;

    private double min_speed;
    private double max_speed;
    private double renting_ratio;

    private ArrayList<NodeData> nodesList;
    private ArrayList<ItemData> itemsList;


    TTP(){
        nodesList = new ArrayList<>();
        itemsList = new ArrayList<>();
    }


    // toString methods zone

    String headersToString(){
        return  "Problem name: " + problemName + "\n" +
                "Knapsack type: " + knapsackType + "\n" +
                "Dimensions: " + dimensionsNumber + "\n" +
                "Items: " + itemsNumber + "\n" +
                "Capacity: " + capacity + "\n" +
                "Min_speed: " + min_speed + "\n" +
                "Max_speed: " + max_speed + "\n" +
                "Renting ratio: " + renting_ratio;
    }

    String nodesToString(){
        String result = "";

        for (NodeData node : nodesList) {
            result += node.getIndex() + ": " + node.getX() + ", " + node.getY() + "\n";
        }

        return result;
    }

    String itemsToString() {
        String result = "";

        for (ItemData item : itemsList) {
            result += item.getIndex() + ": " + item.getProfit() + ", " + item.getWeight() + ", " + item.getNode() +  "\n";
        }

        return result;
    }


    // getters and setters zone

    void addNode(int index, int x, int y){
        nodesList.add(new NodeData(index, x, y));
    }

    void addItem(){

    }

    public String getProblemName() {
        return problemName;
    }

    void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    String getKnapsackType() {
        return knapsackType;
    }

    void setKnapsackType(String knapsackType) {
        this.knapsackType = knapsackType;
    }

    int getDimensionsNumber() {
        return dimensionsNumber;
    }

    void setDimensionsNumber(int dimensionsNumber) {
        this.dimensionsNumber = dimensionsNumber;
    }

    int getItemsNumber() {
        return itemsNumber;
    }

    void setItemsNumber(int itemsNumber) {
        this.itemsNumber = itemsNumber;
    }

    int getCapacity() {
        return capacity;
    }

    void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    double getMinSpeed() {
        return min_speed;
    }

    void setMinSpeed(double min_speed) {
        this.min_speed = min_speed;
    }

    double getMaxSpeed() {
        return max_speed;
    }

    void setMaxSpeed(double max_speed) {
        this.max_speed = max_speed;
    }

    double getRenting_ratio() {
        return renting_ratio;
    }

    void setRenting_ratio(double renting_ratio) {
        this.renting_ratio = renting_ratio;
    }

    public ArrayList<NodeData> getNodesList() {
        return nodesList;
    }

    public ArrayList<ItemData> getItemsList() {
        return itemsList;
    }

    void setNodesList(ArrayList<NodeData> nodesList){
        this.nodesList = nodesList;
    }

    void setItemsList(ArrayList<ItemData> itemsList){
        this.itemsList = itemsList;
    }
}
