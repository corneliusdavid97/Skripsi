package ga.clustering.gui.GA;



import ga.clustering.gui.IR.Document;
import ga.clustering.gui.IR.Lexicon;
import ga.clustering.gui.IR.Vector;
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
//            int mutatedGeneIdx1=rand.nextInt(genes.size());
//            int mutatedGeneIdx2=(mutatedGeneIdx1+rand.nextInt(genes.size()))%genes.size();
//            Vector diff=new Vector(new HashMap<>());
//            for(String t:genes.get(mutatedGeneIdx2).getValue().getKeySet()){
//                Vector tmp1=genes.get(mutatedGeneIdx1).getValue();
//                Vector tmp2=genes.get(mutatedGeneIdx2).getValue();
//                diff.setWeight(t, tmp2.getWeight(t)-tmp1.getWeight(t));
//            }
//            Random r=new Random();
//            double factor=r.nextDouble()*(r.nextBoolean()?-1:1);
//            diff.getKeySet().forEach((t) -> {
//                diff.setWeight(t, diff.getWeight(t)*factor);
//            });
//            diff.getKeySet().forEach((t)->{
//                double newVal=diff.getWeight(t)+genes.get(mutatedGeneIdx1).getValue().getWeight(t);
//                newVal=newVal<0?0:newVal;
//                genes.get(mutatedGeneIdx1).getValue().setWeight(t, newVal);
//            });
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
//        if(fitnessValue!=-1)return fitnessValue;
        List<Document> docs=GAClusterer.getInstance().getAllDocs();
        HashMap<String, Double> sumOfCentroid[]=new HashMap[genes.size()];
        for (int i = 0; i < genes.size(); i++) {
            sumOfCentroid[i]=new HashMap<>();
        }
        int pointCount[]=new int[genes.size()];
        
        //assign dokumen ke cluster
        long cur=System.currentTimeMillis();
        determineCluster(docs);
//        System.out.println("assignment: "+(System.currentTimeMillis()-cur)/1000.0);
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
        
//        System.out.println("compute new centroid: "+(System.currentTimeMillis()-cur)/1000.0);
        cur=System.currentTimeMillis();
        
        HashMap copy[]=new HashMap[genes.size()];
        
        for (int i = 0; i < genes.size(); i++) {
            copy[i]=new HashMap(genes.get(i).getValue().getTermsWeight());
            for(String term:Lexicon.getInstance().getAllTermList()){
                double nextValue=sumOfCentroid[i].containsKey(term)?sumOfCentroid[i].get(term):0;
                nextValue=pointCount[i]==0.0?0.0:nextValue/pointCount[i];
                if(nextValue!=0){
                    genes.get(i).getValue().setWeight(term, nextValue);
                }
            }
        }
        
        //best document
//        Document best[]=new Document[genes.size()];
//        double dist[]=new double[genes.size()];
//        for (int i = 0; i < dist.length; i++) {
//            dist[i]=Double.MAX_VALUE;
//        }
//        
//        for (int i = 0; i < docs.size(); i++) {
//            int idx=clusteringResult.get(docs.get(i));
//            double tempDist=docs.get(i).getVector().calculateSimilarity(genes.get(idx).getValue());
//            if(tempDist<dist[idx]){
//                dist[idx]=tempDist;
//                best[idx]=docs.get(i);
//            }
//        }
//        for (int i = 0; i < best.length; i++) {
//            if(best[i]!=null){
//                genes.get(i).getValue().setTermsWeight(best[i].getVector().getTermsWeight());
//            }else{
//                genes.get(i).getValue().setTermsWeight(copy[i]);
//            }
//            
////            if(genes.get(i).getValue().getDimension()>5000){
////                int axssss=0;
////            }
//        }
////        System.out.print("count: ");
////        for (int i = 0; i < genes.size(); i++) {
////            if(genes.get(i).getValue().getDimension()>5000){
////                int axssss=0;
////            }
////            System.out.print(genes.get(i).getValue().getDimension()+" ");
////        }
////        System.out.println();
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