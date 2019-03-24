/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import IR.Document;
import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author CorneliusDavid
 */
public class Tester {
    public static void main(String[] args) throws Exception{
        Clusterer.getInstance().cluster(10, 5, "D:\\DAVID\\University\\Semester 7\\SKRIPSI\\Skripsi\\app\\GA-Clustering\\dataset\\bbc");
        
    }
}
