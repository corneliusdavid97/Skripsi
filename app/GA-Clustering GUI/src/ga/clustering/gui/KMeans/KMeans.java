/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.KMeans;


import ga.clustering.gui.GA.Clusterer;
import ga.clustering.gui.GA.Params;
import ga.clustering.gui.IR.Document;
import ga.clustering.gui.IR.Lexicon;
import ga.clustering.gui.IR.Vector;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 *
 * @author Cornelius David
 */
public class KMeans {

    private List<Document> docs;
    private HashMap<Document, Integer> last;
    private static KMeans instance;
    private double lastIntracluster;
    
    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
    
    private KMeans(){
        docs=new LinkedList<>();
        lastIntracluster=-1;
    }
    
    public double getProgress() {
        return progressProperty().get();
    }
    
    public ReadOnlyDoubleProperty progressProperty() {
        return progress ;
    }

    public HashMap<Document, Integer> getLast() {
        return last;
    }
    
    public static KMeans getInstance(){
        if(instance==null)instance=new KMeans();
        return instance;
    }

    public void cluster() {
        int k=Params.getInstance().getK();
        String filepath=Params.getInstance().getFilepath();
        int maxIt=Params.getInstance().getMaxIt();
        File folder = new File(filepath);
        File[] files = folder.listFiles();
        Queue<File> queue = new LinkedList<>();
        for (int i = 0; i < files.length; i++) {
            queue.offer(files[i]);
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

        for (File file : allFiles) {
            docs.add(new Document(file));
        }

        //init cluster
        List<Document> list = new LinkedList<>();
        list.addAll(docs);
        Collections.shuffle(list);
        Vector[] centroids = new Vector[k];
        for (int i = 0; i < centroids.length; i++) {
            centroids[i] = list.get(i).getVector();
        }
        
        System.out.println("init done");
        
        iteration:
        for (int a = 0; a < maxIt; a++) {
            progress.set(0);
            System.out.println("it-"+(a+1));
            HashMap<Document, Integer> cluster=determineCluster(docs, centroids);
            System.out.println("assign");

            HashMap<String, Double> sumOfCentroid[] = new HashMap[k];
            for (int i = 0; i < k; i++) {
                sumOfCentroid[i]=new HashMap<>();
            }
            int pointCount[] = new int[k];
            for (int i = 0; i < docs.size(); i++) {
                int clusterCode = cluster.get(docs.get(i));
                for (String term : docs.get(i).getVector().getKeySet()) {
                    double curValue = sumOfCentroid[clusterCode].containsKey(term) ? sumOfCentroid[clusterCode].get(term) : 0;
                    curValue += docs.get(i).getVector().getWeight(term);
                    if (curValue != 0) {
                        sumOfCentroid[clusterCode].put(term, curValue);
                    }
                }
                pointCount[clusterCode]++;
            }
            System.out.println("recompute");
            for (int i = 0; i < k; i++) {
                HashMap<String,Double> temp=new HashMap<>();
                for (String term : Lexicon.getInstance().getAllTermList()) {
                    double nextValue = sumOfCentroid[i].containsKey(term) ? sumOfCentroid[i].get(term) : 0;
                    nextValue = pointCount[i] == 0.0 ? 0.0 : nextValue / pointCount[i];
                    if (nextValue != 0) {
//                        centroids[i].setWeight(term, nextValue);
                        temp.put(term, nextValue);
                    }
                }
                centroids[i].setTermsWeight(temp);
            }
            System.out.println(Arrays.toString(pointCount));
            System.out.println(computeIntracluster(cluster, centroids));
            if(last==null)last=cluster;
            else{
                for (int i = 0; i < docs.size(); i++) {
                    int before=last.get(docs.get(i));
                    int cur=cluster.get(docs.get(i));
                    if(before!=cur){
                        last=cluster;
                        continue iteration;
                    }
                }
                lastIntracluster=computeIntracluster(cluster, centroids);
                return;
            }
            progress.set((a+1)*1.0/maxIt);
        }
        lastIntracluster=computeIntracluster(last, centroids);
    }

    public double getLastIntracluster() {
        return lastIntracluster;
    }
    
    private double computeIntracluster(HashMap<Document, Integer> cluster, Vector[] centroids){
        double res=0.0;
//        determineCluster(docs, centroids);
        for (int i = 0; i < docs.size(); i++) {
            int clusterCode=cluster.get(docs.get(i));
            double tmp=docs.get(i).getVector().calculateDistance(centroids[clusterCode]);
            res+=tmp;
        }
        return res;
    }
    
    private HashMap<Document, Integer> determineCluster(List<Document> docs, Vector[] centroids){
        HashMap<Document, Integer> clusteringResult=new HashMap<>();
        for (int i = 0; i < docs.size(); i++) {
            int cluster=-1;
            double similarity=Double.MIN_VALUE;
            for (int j = 0; j < centroids.length; j++) {
                double temp=docs.get(i).getVector().calculateDistance(centroids[j]);
                if(temp>similarity){
                    similarity=temp;
                    cluster=j;
                }
            }
            clusteringResult.put(docs.get(i), cluster);
        }
        return clusteringResult;
    }
}
