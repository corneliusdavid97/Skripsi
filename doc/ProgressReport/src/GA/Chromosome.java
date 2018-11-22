package GA;


import IR.Dictionary;
import IR.Document;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CorneliusDavid
 */
public class Chromosome {
    public static final double MUTATION_PROBABILITY=.20;
    private List<Gene> genes;
    private Random rand;

    public Chromosome() {
        this.genes = new LinkedList<>();
        this.rand = new Random();
//        this.fitnessValue=-1;
    }
    
    public void addGene(Gene gene){
        this.genes.add(gene);
    }
    
    public void mutate(){
        if(rand.nextDouble()<MUTATION_PROBABILITY){
            int mutatedGeneIdx=rand.nextInt(genes.size());
            genes.get(mutatedGeneIdx).mutate();
        }
    }
    
    public Chromosome crossover(Chromosome otherChromosome){
        int breakPoint=rand.nextInt(genes.size());
        Chromosome result=new Chromosome();
        for (int i = 0; i < breakPoint; i++) {
            result.addGene(this.genes.get(i));
        }
        
        for (int i = breakPoint; i < this.genes.size(); i++) {
            result.addGene(otherChromosome.genes.get(i));
        }
        
        return result;
    }
    
    public double computeFitness(){
//        if(fitnessValue!=-1)return fitnessValue;
        int fitnessValue=0;
        Document docs[]=Clusterer.getInstance().getAllDocs();
        HashMap<String, Double> sumOfCentroid[]=new HashMap[genes.size()];
        for (int i = 0; i < genes.size(); i++) {
            sumOfCentroid[i]=new HashMap<>();
        }
        int pointCount[]=new int[genes.size()];
        for (int i = 0; i < docs.length; i++) {
            docs[i].determineClusterCode(this);
            int clusterCode=docs[i].getClusterCode();
            double tmp=docs[i].getVector().calculateDistance(this.genes.get(clusterCode).getValue());
            fitnessValue+=tmp;
            System.out.println(tmp);
            for(String term:Dictionary.getInstance().getAllTermList()){
                double nextValue=sumOfCentroid[clusterCode].containsKey(term)?sumOfCentroid[clusterCode].get(term):0;
                nextValue+=tmp;
                sumOfCentroid[clusterCode].put(term, nextValue);
            }
            pointCount[clusterCode]++;
        }
        
        for (int i = 0; i < genes.size(); i++) {
            for(String term:Dictionary.getInstance().getAllTermList()){
                double nextValue=sumOfCentroid[i].containsKey(term)?sumOfCentroid[i].get(term):0;
                nextValue=pointCount[i]==0.0?0.0:nextValue/pointCount[i];
                sumOfCentroid[i].put(term, nextValue);
            }
            System.out.println(sumOfCentroid[i]);
            genes.get(i).setTermsWeight(sumOfCentroid[i]);
        }
        return fitnessValue;
    }

    public List<Gene> getAllGenes() {
        return genes;
    }
    
    
}