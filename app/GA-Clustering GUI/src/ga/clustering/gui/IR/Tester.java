/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.IR;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author CorneliusDavid
 */
public class Tester {

    public static void main(String[] args) {
        List<Document> docs=new LinkedList<>();
        File folder = new File("D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc");
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
        
        System.out.println(Lexicon.getInstance().getNumberOfDocument());
        
        double res=docs.get(0).getVector().calculateDistance(docs.get(1).getVector());
        System.out.println(res);
    }
}
