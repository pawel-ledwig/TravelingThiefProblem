package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class GA is used to implement all needed genetic algorithms and solve a TTP problem
 */
class GA {
    private int populationSize;
    private int generationsNumber;
    private int tournamentSize;

    private double crossoverProbability;
    private double mutationProbability;

    private ArrayList<Specimen> specimensGenerationCurrent;
    private ArrayList<Specimen> specimensGenerationNext;

    private ArrayList<Double> bestRatings;
    private ArrayList<Double> avgRatings;
    private ArrayList<Double> worstRatings;

    private TTP ttp;

    GA (int populationSize, int generationsNumber, TTP ttp){
        this.populationSize     = populationSize;
        this.generationsNumber  = generationsNumber;

        this.ttp = ttp;

        specimensGenerationCurrent  = new ArrayList<>();
        specimensGenerationNext     = new ArrayList<>();

        bestRatings  = new ArrayList<>();
        avgRatings   = new ArrayList<>();
        worstRatings = new ArrayList<>();
    }

    /**
     * Method used to create new generation of specimens by classic tournament.
     */
    void makeGenerationsByClassicTournament(){
        for (int i = 0; i < generationsNumber; i++){
            fillPopulationByClassicTournament();
            swapGenerations();
            mutatePopulation();

            bestRatings.add(getBestRating());
            avgRatings.add(getAvgRating());
            worstRatings.add(getWorstRating());
        }
    }

    /**
     * Method used to create new generation of specimens by parents population.
     */
    void makeGenerationsByParentsPopulation(){
        for (int i = 0; i < generationsNumber; i++){
            createPopulationOfParents();
            swapGenerations();
            fillPopulationByParentsPopulation();
            mutatePopulation();

            bestRatings.add(getBestRating());
            avgRatings.add(getAvgRating());
            worstRatings.add(getWorstRating());
        }
    }

    /**
     * Method used to mutate a specimen
     */
    private void mutation(Specimen specimen){
        Random r = new Random();

        int nodeA = r.nextInt(ttp.getDimensionsNumber());
        int nodeB = r.nextInt(ttp.getDimensionsNumber());

        int tmp = specimen.getTspOrderList().get(nodeA);
        specimen.getTspOrderList().set(nodeA, specimen.getTspOrderList().get(nodeB));
        specimen.getTspOrderList().set(nodeB, tmp);
    }

    /**
     * Method used to mutate all population, dependent on mutation probability factor.
     */
    private void mutatePopulation(){
        for (Specimen specimen : specimensGenerationCurrent){
            Random r = new Random();

            if (r.nextDouble() < mutationProbability){
                mutation(specimen);
            }
        }
    }

    /**
     * Method used to fill population with children up to max population size (new tournament at each iteration).
     */
    private void fillPopulationByClassicTournament() {
        while (specimensGenerationNext.size() < populationSize) {
            Specimen parentA = getParentFromTournament();
            Specimen parentB = getParentFromTournament();

            Random r = new Random();

            if (r.nextDouble() < crossoverProbability){
                int startPoint = Math.max(r.nextInt(ttp.getDimensionsNumber()) - 1, 0);
                int endPoint = Math.min(startPoint + r.nextInt(ttp.getDimensionsNumber() / 10), ttp.getDimensionsNumber() - 1);

                specimensGenerationNext.add(createChildPMX(parentA, parentB, startPoint, endPoint));
            } else {
                specimensGenerationNext.add(new Specimen(parentA));
            }
        }
    }

    /**
     * Method used to fill population with children up to max population size (from parents population).
     */
    private void fillPopulationByParentsPopulation(){
        int parents = specimensGenerationCurrent.size();

        while (specimensGenerationCurrent.size() < populationSize){
            Random r = new Random();

            if (r.nextDouble() < crossoverProbability){
                int parentA = r.nextInt(parents);
                int parentB = r.nextInt(parents);

                int startPoint = Math.max(r.nextInt(ttp.getDimensionsNumber()) - 1, 0);
                int endPoint = Math.min(startPoint + r.nextInt(ttp.getDimensionsNumber() / 10), ttp.getDimensionsNumber() - 1);

                specimensGenerationCurrent.add(
                        createChildPMX(specimensGenerationCurrent.get(parentA), specimensGenerationCurrent.get(parentB), startPoint, endPoint)
                );
            }
        }
    }

    /**
     * Method used to choose a parent by the classical tournament.
     */
    private Specimen getParentFromTournament(){
        ArrayList<Specimen> specimensToTournament = new ArrayList<>();

        Random r = new Random();

        /*
        While not enough specimens to make an tournament
         */
        while (specimensToTournament.size() < tournamentSize){
            Specimen parentCandidate = specimensGenerationCurrent.get(r.nextInt(populationSize));

            if (canAddSpecimenToTournament(parentCandidate, specimensToTournament)){
                specimensToTournament.add(parentCandidate);
            }
        }

        /*
        Make a tournament and get the best of specimens
         */

        Specimen bestSpecimen = specimensToTournament.get(0);

        for (Specimen specimen : specimensToTournament){
            if (specimen.getRating() > bestSpecimen.getRating()){
                bestSpecimen = specimen;
            }
        }

        /*
        Return deep copy of a best specimen.
         */
        return new Specimen(bestSpecimen);
    }


    /**
     * Method used to check if there is a possibility to add a specimen to a unique arrayList (if element not in yet).
     */
    private boolean canAddSpecimenToTournament(Specimen parentCandidate, ArrayList<Specimen> specimensToTournament) {
        boolean canAddSpecimen = true;

        for (Specimen specimen : specimensToTournament){
            if (specimen.equals(parentCandidate)){
                canAddSpecimen = false;
            }
        }

        return canAddSpecimen;
    }

    /**
     * Creating a child from two parents using PMX method
     */
    private Specimen createChildPMX(Specimen parentA, Specimen parentB, int startPoint, int endPoint){
        ArrayList<Integer> subArrayA = new ArrayList<>();
        ArrayList<Integer> subArrayB = new ArrayList<>();

        /*
        Child array at the beginning is made from parentA array
         */
        ArrayList<Integer> childArray = new ArrayList<>(parentA.getTspOrderList());

        /*
        Get elements which will be used to crossover
         */
        for (int i = startPoint; i < endPoint; i++){
            subArrayA.add(parentA.getTspOrderList().get(i));
            subArrayB.add(parentB.getTspOrderList().get(i));
        }

        /*
        Replace all nodes from subArrayB with nodes from subArrayA in childArray
         */
        for (int i = 0; i < subArrayB.size(); i++){
            for (int j = 0; j < childArray.size(); j++){
                if (subArrayB.get(i).equals(childArray.get(j))){
                    childArray.set(j, subArrayA.get(i));
                }
            }
        }

        /*
        Replace block of nodes from startPoint to endPoint with nodes from subArrayB
         */
        for (int i = startPoint; i < endPoint; i++){
            childArray.set(i, subArrayB.get(i - startPoint));
        }

        /*
        Update TTP object with new TSP, needed to evaluate it
         */
        ttp.setTspOrderList(new ArrayList<>(childArray));

        /*
         * Evaluate new Specimen and return
         */
        return ttp.createSpecimenFromCurrent();
    }

    /**
     * Method returns best rating from all specimens in generation
     */
    double getBestRating(){
        double bestRating = specimensGenerationCurrent.get(0).getRating();

        for (Specimen specimen : specimensGenerationCurrent){
            if (specimen.getRating() > bestRating){
                bestRating = specimen.getRating();
            }
        }

        return bestRating;
    }

    /**
     * Method returns average rating from all specimens in generation
     */
    double getAvgRating(){
        double sumRating = 0.0;

        for (Specimen specimen : specimensGenerationCurrent){
            sumRating += specimen.getRating();
        }

        return sumRating / specimensGenerationCurrent.size();
    }

    /**
     * Method returns worst rating from all specimens in generation
     */
    double getWorstRating(){
        double worstRating = specimensGenerationCurrent.get(0).getRating();

        for (Specimen specimen : specimensGenerationCurrent){
            if (specimen.getRating() < worstRating){
                worstRating = specimen.getRating();
            }
        }

        return worstRating;
    }

    /**
     * Method used to create population of the parents (next generation)
     */
    private void createPopulationOfParents(){
        for (int i = 0 ; i < populationSize / 2; i++){
            if (specimensGenerationCurrent.get(i * 2).getRating() > specimensGenerationCurrent.get(i * 2 + 1).getRating()) {
                specimensGenerationNext.add(specimensGenerationCurrent.get(i * 2));
            } else {
                specimensGenerationNext.add(specimensGenerationCurrent.get(i * 2 + 1));
            }
        }
    }

    /**
     * Method used to move next generation into current
     */
    private void swapGenerations(){
        specimensGenerationCurrent = new ArrayList<>(specimensGenerationNext);
        specimensGenerationNext.clear();
    }

    /**
    Create random population
     */
    void createPopulation(){
        for (int i = 0; i < populationSize; i++){
            specimensGenerationCurrent.add(ttp.createRandomSpecimen());
        }
    }


    /**
     * Put all population specimens into string.
     */
    String populationToString(){
        StringBuilder result = new StringBuilder();

        for(Specimen specimen : specimensGenerationCurrent){
            result.append(specimen);
        }

        return result.toString();
    }

    /**
     * Save results into CSV file.
     */
    void resultsToFile(String filename){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/" + filename + "_learning.csv"));

            bw.write("GENERATIONS: " + generationsNumber + "POP_SIZE: " + populationSize + ", MX: " + crossoverProbability + ", MM: " + mutationProbability + ", TOUR: " + tournamentSize);
            bw.newLine();

            bw.write("ID;Best;Avg;Worst");
            bw.newLine();

            for (int i = 0 ; i < bestRatings.size(); i++){
                bw.write(i + ";" + Math.round(bestRatings.get(i)) + ";" + Math.round(avgRatings.get(i)) + ";" + Math.round(worstRatings.get(i)) + ";");
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    --------------------------------------------------------------------------------------------------
    */

    public int getGenerationsNumber() {
        return generationsNumber;
    }

    public void setGenerationsNumber(int generationsNumber) {
        this.generationsNumber = generationsNumber;
    }

    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
}
