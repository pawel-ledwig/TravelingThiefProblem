package com.company;

import java.io.*;
import java.util.ArrayList;

class FileParser {

    private File file;
    private TTP ttp;
    private int headerLength; // how many rows of file contains configuration info

    FileParser(File file, int headerLength){
        this.file = file;
        this.headerLength = headerLength;
        ttp = new TTP();
    }

    void init(){
        parseHeader();
        setNodeMap();
        setItemList();
        setDistanceMatrix();
    }

    private void setDistanceMatrix(){
        ttp.setDistanceMatrix();
    }

    private void setItemList(){
        ttp.setItemsList(getItemsFromFile());
    }

    private void setNodeMap() {
        ttp.setNodesList(getNodesFromFile());
    }

    private void parseHeader(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            // for loop for each row of files header
            for (int i = 0 ; i < headerLength; i++){
                String line = br.readLine();
                String header_field_name = line.substring(0, line.indexOf(":"));
                String header_field_value = line.substring(line.indexOf(":") + 1).trim();

                switch(header_field_name){
                    case "PROBLEM NAME":
                        ttp.setProblemName(header_field_value);
                        break;
                    case "KNAPSACK DATA TYPE":
                        ttp.setKnapsackType(header_field_value);
                        break;
                    case "DIMENSION":
                        ttp.setDimensionsNumber(Integer.parseInt(header_field_value));
                        break;
                    case "NUMBER OF ITEMS":
                        ttp.setItemsNumber(Integer.parseInt(header_field_value));
                        break;
                    case "CAPACITY OF KNAPSACK":
                        ttp.setCapacity(Integer.parseInt(header_field_value));
                        break;
                    case "MIN SPEED":
                        ttp.setMinSpeed(Double.parseDouble(header_field_value));
                        break;
                    case "MAX SPEED":
                        ttp.setMaxSpeed(Double.parseDouble(header_field_value));
                        break;
                    case "RENTING RATIO":
                        ttp.setRenting_ratio(Double.parseDouble(header_field_value));
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ItemData> getItemsFromFile(){
        ArrayList<ItemData> itemsList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();

            while (!line.startsWith("ITEMS")){
                // skip all lines from beginning to node coordinate section
                line = br.readLine();
            }
            // here caret should be on the first row of node coordinates

            for (int i = 0; i < ttp.getItemsNumber(); i++){
                line = br.readLine();

                // split row to table (one value per cell, split by tab)
                String[] nodeTable = line.split("\t");

                /*
                Add new item
                nodeTable[0] - index
                nodeTable[1] - profit
                nodeTable[2] - weight
                nodeTable[3] - node reference
                 */
                itemsList.add(new ItemData(Integer.parseInt(nodeTable[0]), Integer.parseInt(nodeTable[1]), Integer.parseInt(nodeTable[2]), Integer.parseInt(nodeTable[3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemsList;
    }

    private ArrayList<NodeData> getNodesFromFile(){
        ArrayList<NodeData> nodesList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();

            while (!line.startsWith("NODE_COORD_SECTION")){
                // skip all lines from beginning to node coordinate section
                line = br.readLine();
            }
            // here caret should be on the first row of node coordinates

            for (int i = 0; i < ttp.getDimensionsNumber(); i++){
                line = br.readLine();

                // split row to table (one value per cell, split by tab)
                String[] nodeTable = line.split("\t");

                /*
                Add new item
                nodeTable[0] - index
                nodeTable[1] - X
                nodeTable[2] - Y
                 */
                nodesList.add(new NodeData(Integer.parseInt(nodeTable[0]), Double.parseDouble(nodeTable[1]), Double.parseDouble(nodeTable[2])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodesList;
    }

    TTP getTtp(){
        return ttp;
    }
}
