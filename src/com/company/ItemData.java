package com.company;

class ItemData {
    private int index;

    private int profit;
    private int weight;

    private int node;

    ItemData(int index, int profit, int weight, int node){
        this.index = index;
        this.profit = profit;
        this.weight = weight;
        this.node = node;
    }

    public int getIndex() {
        return index;
    }

    public int getProfit() {
        return profit;
    }

    public int getWeight() {
        return weight;
    }

    public int getNode() {
        return node;
    }
}
