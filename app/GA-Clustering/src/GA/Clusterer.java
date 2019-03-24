package GA;

import IR.Document;
import IR.Lexicon;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
    private Chromosome solution;
    private List<Document> docs;
    private static Clusterer instance;

    private Clusterer() {
        population = new LinkedList<>();
        docs = new LinkedList<>();
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
            totalWeight += population.get(i).computeFitness();
        }

        double selectedValue = (new Random()).nextDouble() * totalWeight;

        for (int i = 0; i < population.size(); i++) {
            selectedValue -= population.get(i).computeFitness();
            if (selectedValue <= 0) {
                return population.get(i);
            }
        }
        return population.get(population.size() - 1);
    }
    
    private List<Chromosome> elitism(int numOfInstance){
        List<Chromosome> sorted=new LinkedList<>();
        sorted.addAll(population);
        Collections.sort(sorted);
        List<Chromosome> nextGen = new LinkedList<>();
        for (int i = 0; i < numOfInstance; i++) {
            nextGen.add(sorted.get(i));
        }
        return nextGen;
    }
    
    public List<Chromosome> selection() {
        int elitismCount=1;
        List<Chromosome> nextGen=elitism(elitismCount);
        for (int i = 0; i < population.size()-elitismCount; i++) {
            Chromosome parentA = this.rouletteWheelSelect();
            Chromosome parentB = this.rouletteWheelSelect();
            nextGen.add(parentA.crossover(parentB));
        }

        return nextGen;
    }

    public void cluster(int numOfGenerations, int k, String filepath) {
        int popSize = 10;
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

        //generate initial population
        for (int i = 0; i < popSize; i++) {
            Chromosome temp = new Chromosome();
            Collections.shuffle(list);
            for (int j = 0; j < k; j++) {
                temp.addGene(new Gene(list.get(j).getVector()));
            }
            population.add(temp);
        }
        
        for (int i = 0; i < numOfGenerations; i++) {
            //selection
            System.out.println("Gen "+(i+1));
            population = selection();
            System.out.println(popSize+" "+population.size());
            for (int j = 0; j < popSize; j++) {
                Chromosome temp=population.get(j);
                if (solution == null || solution.computeFitness()< temp.computeFitness()) {
                    solution = temp;
                }
            }
            System.out.println(solution.computeFitness());
            //mutation
            for (Chromosome c : population) {
                c.mutate();
            }
        }
    }

    public List<Document> getAllDocs() {
        return docs;
    }
    
    public Chromosome getSolution() {
        return solution;
    }
}
