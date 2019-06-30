package ga.clustering.gui.GA;



import ga.clustering.gui.Params;
import ga.clustering.gui.IR.Document;
import ga.clustering.gui.IR.Lexicon;
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
public class Chromosome implements Comparable<Chromosome>{
    private List<Gene> genes;
    private Random rand;
    private double fitnessValue;
    private HashMap<Document, Integer> clusteringResult;

    public Chromosome() {
        this.genes = new LinkedList<>();
        this.rand = new Random();
        this.fitnessValue=-1;
        this.clusteringResult=new HashMap<>();
    }
    
    public void addGene(Gene gene){
        this.genes.add(gene);
    }
    
    public void mutate(){
        if(rand.nextDouble()<Params.getInstance().getMu_m()){
            int mutatedGeneIdx=rand.nextInt(genes.size());
            genes.get(mutatedGeneIdx).mutate();
        }
    }
    
    public Chromosome crossover(Chromosome otherChromosome){
        int breakPoint=rand.nextInt(genes.size());
        Chromosome result=new Chromosome();
        for (int i = 0; i < breakPoint; i++) {
            result.addGene(new Gene(this.genes.get(i)));
        }
        
        for (int i = breakPoint; i < this.genes.size(); i++) {
            result.addGene(new Gene(otherChromosome.genes.get(i)));
        }
        
        return result;
    }
    
    private void determineCluster(List<Document> docs){
        for (int i = 0; i < docs.size(); i++) {
            int cluster=0;
            double similarity=Double.MIN_VALUE;
            for (int j = 0; j < genes.size(); j++) {
                double temp=docs.get(i).getVector().calculateSimilarity(genes.get(j).getValue());
                if(temp>similarity){
                    similarity=temp;
                    cluster=j;
                }
            }
            this.clusteringResult.put(docs.get(i), cluster);
        }
    }
    
    public double computeFitness(){
        List<Document> docs=GAClusterer.getInstance().getAllDocs();
        HashMap<String, Double> sumOfCentroid[]=new HashMap[genes.size()];
        for (int i = 0; i < genes.size(); i++) {
            sumOfCentroid[i]=new HashMap<>();
        }
        int pointCount[]=new int[genes.size()];
        
        //assign dokumen ke cluster
        long cur=System.currentTimeMillis();
        determineCluster(docs);
        cur=System.currentTimeMillis();
        
        //hitung centroid baru
        for (int i = 0; i < docs.size(); i++) {
            int clusterCode=clusteringResult.get(docs.get(i));
            for(String term:docs.get(i).getVector().getKeySet()){
                double curValue=sumOfCentroid[clusterCode].containsKey(term)?sumOfCentroid[clusterCode].get(term):0;
                curValue+=docs.get(i).getVector().getWeight(term);
                if(curValue!=0){
                    sumOfCentroid[clusterCode].put(term, curValue);
                }
            }
            pointCount[clusterCode]++;
        }

        cur=System.currentTimeMillis();
        
        for (int i = 0; i < genes.size(); i++) {
            for(String term:Lexicon.getInstance().getAllTermList()){
                double nextValue=sumOfCentroid[i].containsKey(term)?sumOfCentroid[i].get(term):0;
                nextValue=pointCount[i]==0.0?0.0:nextValue/pointCount[i];
                if(nextValue!=0){
                    genes.get(i).getValue().setWeight(term, nextValue);
                }
            }
        }
        this.fitnessValue=-1;
        return getFitness();
    }
    
    public double getFitness(){
        if(fitnessValue!=-1)return this.fitnessValue;
        List<Document> docs=GAClusterer.getInstance().getAllDocs();
        double res=0.0;
        determineCluster(docs);
        for (int i = 0; i < docs.size(); i++) {
            int clusterCode=clusteringResult.get(docs.get(i));
            double tmp=docs.get(i).getVector().calculateSimilarity(this.genes.get(clusterCode).getValue());
            res+=tmp;
        }
        fitnessValue=res;
        return res;
    }

    public List<Gene> getAllGenes() {
        return genes;
    }

    public HashMap<Document, Integer> getClusteringResult() {
        return clusteringResult;
    }

    @Override
    public int compareTo(Chromosome o) {
        return this.getFitness()>o.getFitness()?-1:this.getFitness()<o.getFitness()?1:0;
    }
}