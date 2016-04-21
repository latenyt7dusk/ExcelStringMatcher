/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nakpil;

import java.text.DecimalFormat;

/**
 *
 * @author HERU
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        DecimalFormat f = new DecimalFormat("#0.00");
        StringMatcher jw = new StringMatcher();
        
        // Example matching
        System.out.println(f.format(100*jw.similarity("BALONDO JR, ARTURO MAGALLON","Balondo, Arturo Jr. Magallon".toUpperCase()))+"%");
    }
}
