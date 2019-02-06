package GA;

import IR.Document;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
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
public class Clusterer {

    private List<Chromosome> population;
    private Chromosome solution;
    private Document docs[];
    private static Clusterer instance;

    private Clusterer() {
        population = new LinkedList<>();
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

    public List<Chromosome> selection() {
        List<Chromosome> nextGen = new LinkedList<>();
        for (int i = 0; i < population.size(); i++) {
            Chromosome temp = this.rouletteWheelSelect();
            nextGen.add(temp);
        }

        return nextGen;
    }

    public void cluster(int numOfGenerations, int k) {
        int popSize = 10;
        //document indexing
        File folder = new File("documents");
        File[] files = folder.listFiles();
        docs = new Document[files.length];
        for (int i = 0; i < docs.length; i++) {
            docs[i] = new Document(files[i]);
        }

        List<Document> list = Arrays.asList(docs);

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
            population = selection();
            for (int j = 0; j < popSize; j++) {
                Chromosome temp=population.get(j);
                if (solution == null || solution.computeFitness()< temp.computeFitness()) {
                    solution = temp;
                }
            }
//            System.out.println(solution.computeFitness());
            //mutation
            for (Chromosome c : population) {
                c.mutate();
            }
        }
    }

    public Document[] getAllDocs() {
        return docs;
    }
    
    public Chromosome getSolution() {
        return solution;
    }
}
