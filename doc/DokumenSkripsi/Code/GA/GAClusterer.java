package ga.clustering.gui.GA;

import ga.clustering.gui.Params;
import ga.clustering.gui.IR.Document;
import ga.clustering.gui.IR.Lexicon;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author CorneliusDavid
 */
public class GAClusterer{

    private List<Chromosome> population;
    private List<Document> docs;
    private static GAClusterer instance;
    private List<Chromosome> solutionList;
    
    private boolean isRunning;
    
    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();

    private GAClusterer() {
        population = new LinkedList<>();
        docs = new LinkedList<>();
        solutionList = new LinkedList<>();
        isRunning=true;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    public double getProgress() {
        return progressProperty().get();
    }
    
    public ReadOnlyDoubleProperty progressProperty() {
        return progress ;
    }

    public static GAClusterer getInstance() {
        if (instance == null) {
            instance = new GAClusterer();
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

    private List<Chromosome> elitism(int numOfInstance) {
        PriorityQueue<Chromosome> q = new PriorityQueue<>();
        for (int i = 0; i < population.size(); i++) {
            if(numOfInstance==0)break;
            if (i < numOfInstance) {
                q.offer(population.get(i));
            } else {
                Chromosome cur = population.get(i);
                if (cur.getFitness() > q.peek().getFitness()) {
                    q.poll();
                    q.offer(cur);
                }
            }
        }
        ArrayList<Chromosome> res=new ArrayList<>(q);
//        System.out.println("elite");
//        for (int i = 0; i < res.size(); i++) {
//            System.out.println(res.get(i).getFitness());
//        }
//        System.out.println("end...");
        return res;
    }

    public List<Chromosome> selection(int elitismCount) {
        long cur = System.currentTimeMillis();
        List<Chromosome> nextGen = elitism(elitismCount);
        System.out.println("elitism: " + (System.currentTimeMillis() - cur) / 1000.0);
        cur = System.currentTimeMillis();
        for (int i = 0; i < population.size() - elitismCount; i++) {
//            long roul = System.currentTimeMillis();
            Chromosome parentA = this.rouletteWheelSelect();
//            System.out.println("single-roulette: "+(System.currentTimeMillis()-roul));
            Chromosome parentB = this.rouletteWheelSelect();
            Chromosome offspring = parentA.crossover(parentB);
//            offspring.computeFitness();
//            HashMap<Document,Integer> tmp=new HashMap<>(offspring.getClusteringResult());
            offspring.mutate();
//            offspring.computeFitness();
//            String debug="";
//            for (int j = 0; j < docs.size(); j++) {
//                if(!Objects.equals(tmp.get(docs.get(j)), offspring.getClusteringResult().get(docs.get(j)))){
//                    debug+=docs.get(j).getDocName().substring(Params.getInstance().getFilepath().length())+"("+tmp.get(docs.get(j))+","+offspring.getClusteringResult().get(docs.get(j))+"), ";
//                }
//            }
//            System.out.println(debug);

            nextGen.add(offspring);
            progress.set(0.6+(0.2*i/population.size()));
        }
        System.out.println("roulette: " + (System.currentTimeMillis() - cur) / 1000.0);
        return nextGen;
    }

    public void initialize() {
        String filepath = Params.getInstance().getFilepath();
        int k = Params.getInstance().getK();
        int popSize = Params.getInstance().getP();

        long cur = System.currentTimeMillis();
        //document indexing
        File folder = new File(filepath);
        File[] files = folder.listFiles();
        Queue<File> queue = new LinkedList<>();
        for (File file : files) {
            queue.offer(file);
        }
        LinkedList<File> allFiles = new LinkedList<>();

        while (!queue.isEmpty()) {
            File tmp = queue.poll();
            if (tmp.isDirectory()) {
                File[] tmpFolder = tmp.listFiles();
                for (int i = 0; i < tmpFolder.length; i++) {
                    queue.offer(tmpFolder[i]);
                }
            } else {
                allFiles.add(tmp);
            }
        }

        Lexicon.getInstance().setNumberOfDocument(allFiles.size());

        allFiles.forEach((file) -> {
            docs.add(new Document(file));
        });

        List<Document> list = new LinkedList<>();
        list.addAll(docs);

        System.out.println("Precompute: " + (System.currentTimeMillis() - cur) / 1000.0);
        cur = System.currentTimeMillis();
        //generate initial population
        for (int i = 0; i < popSize; i++) {
            Chromosome temp = new Chromosome();
            Collections.shuffle(list);
            for (int j = 0; j < k; j++) {
                temp.addGene(new Gene(list.get(j).getVector()));
            }
            population.add(temp);
        }

        System.out.println("Init pop: " + (System.currentTimeMillis() - cur) / 1000.0);
    }

    public List<Document> getAllDocs() {
        return docs;
    }

    public void cluster() {
        for (int a = 0; a < Params.getInstance().getMaxIt(); a++) {
            progress.set(0);
            if(!isRunning){
                progress.set(0);
                return;
            }
            System.out.println("Gen-"+(a+1)+":");
            int popSize = Params.getInstance().getP();
            int elitismCount = Params.getInstance().getElitismCount();
            int convergeGen = Params.getInstance().getConvergeGen();
            double convergeEpsilon = Params.getInstance().getConvergeEpsilon();
            long cur = System.currentTimeMillis();
    //        System.out.println("Gen " + (i + 1));
            //compute fitness : 0.6
            double totalTime = 0;
            for (int i = 0; i < popSize; i++) {
                if(!isRunning){
                    progress.set(0);
                    return;
                }
                Chromosome c=population.get(i);
                cur = System.currentTimeMillis();
                c.computeFitness();
                progress.set(0.6*(i*1.0/popSize));
                totalTime += (System.currentTimeMillis() - cur) / 1000.0;
            }
            System.out.println("fitness avg time: " + (totalTime / population.size()));
            cur = System.currentTimeMillis();
            
            if(!isRunning){
                progress.set(0);
                return;
            }
            //selection
            population = selection(elitismCount);
            if(!isRunning){
                progress.set(0);
                return;
            }
            System.out.println("selection: " + (System.currentTimeMillis() - cur) / 1000.0);
            Chromosome solution = null;
            for (int j = 0; j < popSize; j++) {
                Chromosome temp = population.get(j);
//                System.out.println(temp.getFitness());
                if (solution == null || solution.getFitness() < temp.getFitness()) {
                    solution = temp;
                }
                progress.set(0.8+(0.2*j/popSize));
            }
//            System.out.println("...");
            if(!isRunning){
                progress.set(0);
                return;
            }

            this.solutionList.add(solution);
            System.out.println(solution.getFitness());

            if (this.solutionList.size() > convergeGen) {
                double delta = 0;
                this.solutionList.remove(0);
                for (int j = 1; j < this.solutionList.size(); j++) {
                    double curr = this.solutionList.get(j).getFitness();
                    double prev = this.solutionList.get(j - 1).getFitness();
                    delta += Math.abs(curr - prev);
                }
                delta /= this.solutionList.size();
                if (delta < convergeEpsilon) {
                    a=Params.getInstance().getMaxIt();
                }
            }
            progress.set(1);
            if(!isRunning){
                progress.set(0);
                return;
            }
        }
    }
    
    public Chromosome getSolution(){
        return this.solutionList.get(this.solutionList.size()-1);
    }
    
    public void reset(){
        instance=null;
    }
}
