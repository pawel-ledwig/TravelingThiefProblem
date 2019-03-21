package com.company;

import java.util.ArrayList;
import java.util.Collections;

/*
Class TTP is used to store all data from parsed file
 */
public class TTP {
    private String problemName;
    private String knapsackType;

    private int dimensionsNumber;
    private int itemsNumber;
    private int capacity;

    private double minSpeed;
    private double maxSpeed;
    private double rentingRatio;

    private ArrayList<NodeData> nodesList;
    private ArrayList<ItemData> itemsList;

    private double[][] distanceMatrix;

    private ArrayList<Integer> tspOrderList;
    private ArrayList<Double> velocityList;

    private enum KNP_TYPE {
        HIGHEST_VALUE,  // Always take an element with the highest value
        LOWEST_WEIGHT,  // Always take an element with the lowest weight
        BEST_RATIO,     // Always take an element with the highest value/weight ratio
    }

    private KNP_TYPE knpType;

    /*
    Just a constructor :)
     */
    TTP(){
        nodesList = new ArrayList<>();
        itemsList = new ArrayList<>();

        tspOrderList = new ArrayList<>();
        velocityList = new ArrayList<>();
    }

    /*
    --------------------------------------------------------------------------------------------------
    */

    /*
    Method used to create new Specimen from random data (random TSP order, evaluated).
     */

    Specimen createRandomSpecimen(){
        createRandomTSP();
        return new Specimen(evaluate(), new ArrayList<>(tspOrderList));
    }

    /*
    Method used to return new Specimen from current TTP data.
     */
    Specimen createSpecimenFromCurrent(){
        return new Specimen(evaluate(), tspOrderList);
    }

    /*
    Method used to evaluate specimen.
     */
    private double evaluate(){
        return calculateKNP() - calculateTravelTime();
    }

    /*
    Choose the best item from given in a list.
     */
    private ItemData getBestItem(ArrayList<ItemData> itemsInNode) {
        int currentBestIndex = 0;

        switch (knpType){
            case HIGHEST_VALUE:

                for (int i = 0; i < itemsInNode.size(); i++) {
                    if (itemsInNode.get(i).getProfit() > itemsInNode.get(currentBestIndex).getProfit())
                        currentBestIndex = i;
                }

                return itemsInNode.get(currentBestIndex);

            case LOWEST_WEIGHT:

                for (int i = 0; i < itemsInNode.size(); i++) {
                    if (itemsInNode.get(i).getWeight() < itemsInNode.get(currentBestIndex).getWeight())
                        currentBestIndex = i;
                }

                return itemsInNode.get(currentBestIndex);

            case BEST_RATIO:

                for (int i = 0; i < itemsInNode.size(); i++) {
                    if (itemsInNode.get(i).getProfit() / itemsInNode.get(i).getWeight() >
                            itemsInNode.get(currentBestIndex).getProfit() / itemsInNode.get(currentBestIndex).getWeight())

                        currentBestIndex = i;
                }

                return itemsInNode.get(currentBestIndex);

        }
        return new ItemData(0, 0, 0, 0);
    }

    /*
    Calculate current velocity based on current knapsack weight and min-max velocity bounds.
     */
    private double calculateCurrentVelocity(int currentWeight){
        return getMaxSpeed() - currentWeight * ((getMaxSpeed() - getMinSpeed()) / getCapacity());
    }

    /*
    Check if can I grab a selected item (if knapsack capacity is big enough)
     */
    private boolean canGrabItem(int currentWeight, ItemData bestItem) {
        return currentWeight + bestItem.getWeight() <= getCapacity();
    }

    /*
    Calculate value of knapsack after trip.
    Method is also calculating velocity list during the trip, which is needed to calculate trip time.
    Method must be called at least once before calling calculateTravelTime().
     */
    private int calculateKNP(){
        int totalValue = 0;
        int currentWeight = 0;

        // for each node in tspOrderList arrayList calculate best option
        for (int currentNodeID : tspOrderList){

            // arrayList for all items in node
            ArrayList<ItemData> itemsInNode = new ArrayList<>();

            /*
            From list of all items, choose these which are available in current node (currentNodeID)
             */
            for (ItemData currentItem : itemsList) {
                if (currentItem.getNode() == currentNodeID){
                    itemsInNode.add(currentItem);
                }
            }

            /*
            If items exists in this node, take the best one
             */
            if (itemsInNode.size() > 0){
                ItemData bestItem = getBestItem(itemsInNode);

                if (canGrabItem(currentWeight, bestItem)) {
                    totalValue += bestItem.getProfit();
                    currentWeight += bestItem.getWeight();
                }
            }

            velocityList.add(calculateCurrentVelocity(currentWeight));
        }

        return totalValue;
    }

    /*
    Calculate travel time (full loop)
     */
    private double calculateTravelTime(){
        double travelTime = 0.0;

        // for each node in tspOrderList arrayList calculate everything
        for (int i = 1; i < tspOrderList.size(); i++){
            travelTime += getDistanceBetweenNodes(tspOrderList.get(i - 1), tspOrderList.get(i)) / velocityList.get(i - 1);
        }

        // travel time between last and first node
        travelTime += getDistanceBetweenNodes(tspOrderList.get(0), tspOrderList.get(tspOrderList.size() - 1)) / velocityList.get(velocityList.size() - 1);

        return travelTime;
    }

    /*
    Get distance between two nodes
     */
    private double getDistanceBetweenNodes(int indexA, int indexB){
        /*
        Decrementing of index is needed due to way of indexing array and nodes:
        Arrays are indexed from 0 and nodes are indexed from 1
        */
        return distanceMatrix[indexA - 1][indexB - 1];
    }

    /*
    --------------------------------------------------------------------------------------------------
    */

    /*
    Fill tspOrderList array with randomly placed indexes of nodes
     */
    void createRandomTSP(){
        tspOrderList.clear();

        // fill an ArrayList with indexes of available nodes (1..N)
        for (int i = 1 ; i <= getDimensionsNumber(); i++){
            tspOrderList.add(i);
        }

        // swap indexes randomly
        Collections.shuffle(tspOrderList);
    }

    /*
    Calculates Euclidean distance between dwo points in 2D space.
    Used only to create distance matrix.
     */
    private double getDistanceBetweenPoints(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /*
    Calculating distance matrix for all nodes (using getDistanceBetweenPoints())
     */
    void setDistanceMatrix() {
        distanceMatrix = new double[dimensionsNumber][dimensionsNumber];

        /*
        Fill each element of matrix with distance between points
        Must remember: index in matrix is less by 1 than real index of node
        (due to array indexing from 0, nodes indexing from 1)
         */
        for (int i = 0; i < dimensionsNumber; i++){
            for (int j = 0; j < dimensionsNumber; j++){
                distanceMatrix[i][j] =
                        getDistanceBetweenPoints(
                            nodesList.get(i).getX(), nodesList.get(i).getX(),
                            nodesList.get(j).getX(), nodesList.get(j).getY()
                        );
            }
        }
    }

    /*
    --------------------------------------------------------------------------------------------------
     */

    String headersToString(){
        return  "Problem name: " + problemName + "\n" +
                "Knapsack type: " + knapsackType + "\n" +
                "Dimensions: " + dimensionsNumber + "\n" +
                "Items: " + itemsNumber + "\n" +
                "Capacity: " + capacity + "\n" +
                "Min_speed: " + minSpeed + "\n" +
                "Max_speed: " + maxSpeed + "\n" +
                "Renting ratio: " + rentingRatio;
    }

    String nodesToString(){
        StringBuilder result = new StringBuilder();

        for (NodeData node : nodesList) {
            result.append(node.getIndex()).append(": ").append(node.getX()).append(", ").append(node.getY()).append("\n");
        }

        return result.toString();
    }

    String itemsToString() {
        StringBuilder result = new StringBuilder();

        for (ItemData item : itemsList) {
            result.append(item.getIndex()).append(": ").append(item.getProfit()).append(", ").append(item.getWeight()).append(", ").append(item.getNode()).append("\n");
        }

        return result.toString();
    }

    String tspToString(){
        StringBuilder result = new StringBuilder();

        for (int index : tspOrderList){
            result.append(index).append(" ");
        }

        return result.toString();
    }

    String velocityToString(){
        StringBuilder result = new StringBuilder();

        for (double velocity : velocityList){
            result.append(velocity).append(" ");
        }

        return result.toString();
    }

    /*
    --------------------------------------------------------------------------------------------------
    */

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
        return minSpeed;
    }

    void setMinSpeed(double min_speed) {
        this.minSpeed = min_speed;
    }

    double getMaxSpeed() {
        return maxSpeed;
    }

    void setMaxSpeed(double max_speed) {
        this.maxSpeed = max_speed;
    }

    double getRentingRatio() {
        return rentingRatio;
    }

    void setRentingRatio(double rentingRatio) {
        this.rentingRatio = rentingRatio;
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

    void setKnpTypeHighestValue(){
        knpType = KNP_TYPE.HIGHEST_VALUE;
    }

    void setKnpTypeLowestWeight(){
        knpType = KNP_TYPE.LOWEST_WEIGHT;
    }

    void setKnpTypeBestRatio(){
        knpType = KNP_TYPE.BEST_RATIO;
    }

    void setTspOrderList(ArrayList<Integer> tspOrderList) {
        this.tspOrderList = tspOrderList;
    }
}
