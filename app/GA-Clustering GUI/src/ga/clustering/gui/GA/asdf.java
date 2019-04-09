/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.clustering.gui.GA;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Cornelius David
 */
public class asdf {
    public static void main(String[] args) {
        PriorityQueue<Integer> q= new  PriorityQueue<>();
        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()){
            String cmd=sc.nextLine();
            if(cmd.equals("offer")){
                q.offer(sc.nextInt());
            }
            if(cmd.equals("poll")){
                q.poll();
            }
            
            Iterator<Integer> it=q.iterator();
            
            while(it.hasNext()){
                System.out.print(it.next()+" ");
            }
            System.out.println("");
        }
    }
}
