/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IR;

import java.util.HashMap;

/**
 *
 * @author Cornelius David
 */
public abstract class TermWeighting {
    
    public abstract double calculateWeight(String term, HashMap<String, Integer> wordCount);
}
