/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KMeans;

import IR.Document;
import IR.Lexicon;
import IR.Vector;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Cornelius David
 */
public class KMeans {

    private List<Document> docs;

    public void cluster(int k, String filepath) {
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

        for (File file : allFiles) {
            docs.add(new Document(file));
        }

        //init cluster
        List<Document> list = new LinkedList<>();
        list.addAll(docs);
        Collections.shuffle(list);
        Vector[] solution = new Vector[k];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = list.get(i).getVector();
        }

        for (int a = 0; a < 100; a++) {
            HashMap<Document, Integer> cluster=determineCluster(docs, solution);

            HashMap<String, Double> sumOfCentroid[] = new HashMap[k];
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
            for (int i = 0; i < k; i++) {
                for (String term : Lexicon.getInstance().getAllTermList()) {
                    double nextValue = sumOfCentroid[i].containsKey(term) ? sumOfCentroid[i].get(term) : 0;
                    nextValue = pointCount[i] == 0.0 ? 0.0 : nextValue / pointCount[i];
                    if (nextValue != 0) {
                        solution[k].setWeight(term, nextValue);
                    }
                }
            }
        }
    }
    
    private HashMap<Document, Integer> determineCluster(List<Document> docs, Vector[] solution){
        HashMap<Document, Integer> clusteringResult=new HashMap<>();
        for (int i = 0; i < docs.size(); i++) {
            int cluster=0;
            double distance=Double.MAX_VALUE;
            for (int j = 0; j < solution.length; j++) {
                double temp=docs.get(i).getVector().calculateDistance(solution[j]);
                if(temp<distance){
                    distance=temp;
                    cluster=i;
                }
            }
            clusteringResult.put(docs.get(i), cluster);
        }
        return clusteringResult;
    }
}
