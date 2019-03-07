package com.company;

public class TTP {
    private String problemName;
    private String knapsackType;

    private int dimensions;
    private int items;
    private int capacity;

    private double min_speed;
    private double max_speed;
    private double renting_ratio;

    private NodeMap nodeMap;
    private ItemList itemList;


    // methods zone

    String getHeaders(){
        return  "Problem name: " + problemName + "\n" +
                "Knapsack type: " + knapsackType + "\n" +
                "Dimensions: " + dimensions  + "\n" +
                "Items: " + items + "\n" +
                "Capacity: " + capacity + "\n" +
                "Min_speed: " + min_speed + "\n" +
                "Max_speed: " + max_speed + "\n" +
                "Renting ratio: " + renting_ratio;
    }


    // getters and setters zone

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

    int getDimensions() {
        return dimensions;
    }

    void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    int getItems() {
        return items;
    }

    void setItems(int items) {
        this.items = items;
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

    NodeMap getNodeMap() {
        return nodeMap;
    }

    void setNodeMap(NodeMap nodeMap) {
        this.nodeMap = nodeMap;
    }

    ItemList getItemList() {
        return itemList;
    }

    void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }
}
