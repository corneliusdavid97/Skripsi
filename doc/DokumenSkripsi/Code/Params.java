/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui;

/**
 *
 * @author Cornelius David
 */
public class Params {
    private static Params instance;
    private String filepath;
    private int K;
    private int P;
    private int weightingMethod; //0=TF-IDF, 1=Frekuensi
    private double mu_m;
    private int maxIt;
    private int elitismCount;
    private int convergeGen;
    private double convergeEpsilon;
    
    private Params(){
        
    }
    
    public static Params getInstance(){
        if(instance==null){
            instance=new Params();
        }
        return instance;
    }
    
    public void insertParam(String filepath, int K, int P, int weightMethod, double mu_m, int maxIt, int elitismCount, int convergeGen, double convergeEpsilon){
        this.K=K;
        this.P=P;
        this.weightingMethod=weightMethod;
        this.mu_m=mu_m;
        this.maxIt=maxIt;
        this.elitismCount=elitismCount;
        this.convergeGen=convergeGen;
        this.convergeEpsilon=convergeEpsilon;
        this.filepath=filepath;
    }

    public int getK() {
        return K;
    }

    public int getP() {
        return P;
    }

    public int getWeightingMethod() {
        return weightingMethod;
    }

    public double getMu_m() {
        return mu_m;
    }

    public int getMaxIt() {
        return maxIt;
    }

    public int getElitismCount() {
        return elitismCount;
    }

    public int getConvergeGen() {
        return convergeGen;
    }

    public double getConvergeEpsilon() {
        return convergeEpsilon;
    }

    public String getFilepath() {
        return filepath;
    }
}
