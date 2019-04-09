package GA;

import IR.Document;
import IR.Lexicon;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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
public class Clusterer {

    private List<Chromosome> population;
    private List<Document> docs;
    private static Clusterer instance;
    private List<Chromosome> solution;

    private Clusterer() {
        population = new LinkedList<>();
        docs = new LinkedList<>();
        solution=new LinkedList<>();
    }

    public static Clusterer getInstance() {
        if (instance == null) {
            instance = new Clusterer();
        }
        return instance;
    }

    public Chromosome rouletteWheelSelect() {
        double totalWeight = 0;
        for (int i = 0; i < population.size(); i++) {
            totalWeight += population.get(i).getFitness();
        }

        double selectedValue = (new Random()).nextDouble() * totalWeight;

        for (int i = 0; i < population.size(); i++) {
            selectedValue -= population.get(i).getFitness();
            if (selectedValue <= 0) {
                return population.get(i);
            }
        }
        return population.get(population.size() - 1);
    }
    
    private List<Chromosome> elitism(int numOfInstance){
        PriorityQueue<Chromosome> q=new PriorityQueue<>();
        for (int i = 0; i < population.size(); i++) {
            if(i<numOfInstance){
                q.offer(population.get(i));
            }else{
                Chromosome cur=population.get(i);
                if(cur.getFitness()<q.peek().getFitness()){
                    q.poll();
                    q.offer(cur);
                }
            }
        }
        return new ArrayList<>(q);
    }
    
    public List<Chromosome> selection() {
        int elitismCount=1;
        long cur=System.currentTimeMillis();
        List<Chromosome> nextGen=elitism(elitismCount);
        System.out.println("elitism: "+(System.currentTimeMillis()-cur)/1000.0);
        cur=System.currentTimeMillis();
        for (int i = 0; i < population.size()-elitismCount; i++) {
            long roul=System.currentTimeMillis();
            Chromosome parentA = this.rouletteWheelSelect();
//            System.out.println("single-roulette: "+(System.currentTimeMillis()-roul));
            Chromosome parentB = this.rouletteWheelSelect();
            Chromosome offspring=parentA.crossover(parentB);
            offspring.mutate();
            nextGen.add(offspring);
        }
        System.out.println("roulette: "+(System.currentTimeMillis()-cur)/1000.0);
        return nextGen;
    }

    public Chromosome cluster(int numOfGenerations, int k, String filepath) {
        long cur=System.currentTimeMillis();
        int popSize = 1;
        //document indexing
        File folder = new File(filepath);
        File[] files = folder.listFiles();
        Queue<File> queue=new LinkedList<>();
        for (int i = 0; i < files.length; i++) {
            queue.offer(files[i]);
        }
        LinkedList<File> allFiles=new LinkedList<>();
        
        while(!queue.isEmpty()){
            File tmp=queue.poll();
            if(tmp.isDirectory()){
                File[] tmpFolder=tmp.listFiles();
                for (int i = 0; i < tmpFolder.length; i++) {
                    queue.offer(tmpFolder[i]);
                }
            }else{
                allFiles.add(tmp);
            }
        }
        
        Lexicon.getInstance().setNumberOfDocument(allFiles.size());
        
        for(File file:allFiles){
            docs.add(new Document(file));
        }

        List<Document> list = new LinkedList<>();
        list.addAll(docs);
        
        System.out.println("Precompute: "+(System.currentTimeMillis()-cur)/1000.0);
        cur=System.currentTimeMillis();
        //generate initial population
        for (int i = 0; i < popSize; i++) {
            Chromosome temp = new Chromosome();
            Collections.shuffle(list);
            for (int j = 0; j < k; j++) {
                temp.addGene(new Gene(list.get(j).getVector()));
            }
            population.add(temp);
        }
        
        System.out.println("Init pop: "+(System.currentTimeMillis()-cur)/1000.0);
        cur=System.currentTimeMillis();
        
        for (int i = 0; i < numOfGenerations; i++) {
            cur=System.currentTimeMillis();
            System.out.println("Gen "+(i+1));
            //compute fitness
            double totalTime=0;
            for (Chromosome c : population) {
                cur=System.currentTimeMillis();
                c.computeFitness();
                totalTime+=(System.currentTimeMillis()-cur)/1000.0;
            }
            System.out.println("fitness avg time: "+(totalTime/population.size()));
            cur=System.currentTimeMillis();
            
            //selection
            population = selection();
            System.out.println("selection: "+(System.currentTimeMillis()-cur)/1000.0);
            Chromosome solution=null;
            for (int j = 0; j < popSize; j++) {
                Chromosome temp=population.get(j);
                if (solution == null || solution.getFitness()> temp.getFitness()) {
                    solution = temp;
                }
            }
            
            this.solution.add(solution);
            System.out.println(solution.getFitness());
            
            if(this.solution.size()>3){
                double delta=0;
                this.solution.remove(0);
                for (int j = 1; j < this.solution.size(); j++) {
                    double curr=this.solution.get(j).getFitness();
                    double prev=this.solution.get(j-1).getFitness();
                    delta+=Math.abs(curr-prev);
                }
                delta/=this.solution.size();
                if(delta<0.001){
                    break;
                }
            }
        }
        return solution.get(solution.size()-1);
    }

    public List<Document> getAllDocs() {
        return docs;
    }
}
