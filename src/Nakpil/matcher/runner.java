/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nakpil.matcher;

import java.util.Arrays;

/**
 *
 * @author HERU
 */
public class runner {
    
    private static MatchUI MatcherUI;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("-ui")){
            MatcherUI = new MatchUI();
            MatcherUI.setVisible(true);
        }else{
            
        }
    }
    
}
