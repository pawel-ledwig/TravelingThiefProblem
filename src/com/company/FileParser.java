package com.company;

import java.io.*;

public class FileParser {

    private File file;
    private TTP ttp;
    private int headerLength; // how many rows of file contains configuration info

    FileParser(File file, int headerLength){
        this.file = file;
        this.headerLength = headerLength;
        ttp = new TTP();
    }

    void setItemList(){

    }

    void setNodeMap() {
        ttp.setNodeMap(getNodesFromFile());
    }

    void parseHeader(){
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
                        ttp.setDimensions(Integer.parseInt(header_field_value));
                        break;
                    case "NUMBER OF ITEMS":
                        ttp.setItems(Integer.parseInt(header_field_value));
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

    private NodeMap getNodesFromFile(){
        NodeMap nodeMap = new NodeMap();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();

            while (!line.startsWith("NODE_COORD_SECTION")){
                // skip all lines from beginning to node coordinate section
                line = br.readLine();
            }
            // here caret should be on the first row of node coordinates

            for (int i = 0 ; i < ttp.getDimensions(); i++){
                line = br.readLine();

                // split row to table (one value per cell, split by tab)
                String[] nodeTable = line.split("\t");

                // add new point - table index [1] is X, table index [2] is Y
                nodeMap.addPoint(Double.parseDouble(nodeTable[1]), Double.parseDouble(nodeTable[2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodeMap;
    }

    TTP getTtp(){
        return ttp;
    }
}
