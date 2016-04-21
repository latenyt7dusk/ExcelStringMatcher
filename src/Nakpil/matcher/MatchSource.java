/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nakpil.matcher;

import com.smartxls.WorkBook;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HERU
 */
public class MatchSource {

    private int StartIndex;
    private int EndIndex;
    private WorkBook Source;
    private String SelectedSheet;
    private List<String> Sheets = new ArrayList();
    private String Target;
    private String Column;
    private String SRC_File;
    private boolean DupCheck = false;

    public MatchSource(String src) {
        try {
            Source = new WorkBook();
            this.SRC_File = src;
            if (src.endsWith(".xlsx")) {
                Source.readXLSX(src);
            } else if (src.endsWith(".xlsb")) {
                Source.readXLSB(src);
            } else {
                Source.read(src);
            }
            for (int i = 0; i < Source.getNumSheets(); i++) {
                Sheets.add(Source.getSheetName(i));
            }
        } catch (Exception er) {

        }
    }
    
    public void CheckDuplicates(boolean b){
        this.DupCheck = b;
    }
    
    public boolean CheckForDuplicates(){
        return DupCheck;
    }
    
    public String getSourceFile(){
        return SRC_File;
    }
    
    public void setSourceColumn(String s){
        this.Column = s;
    }
    public String getSourceColumn(){
        return Column;
    }
    
    public void setTargetColumn(String s){
        this.Target = s;
    }
    
    public String getTargetColumn(){
        return Target;
    }
    
    public WorkBook getWorkBook(){
        return Source;
    }
    
    public List<String> getSheets(){
        return Sheets;
    }

    public void setSelectedSheet(String s) {
        try {
            int x = 0;
            for (int i = 0; i < Source.getNumSheets(); i++) {
                if (Source.getSheetName(i).equals(s)) {
                    x = i;
                    break;
                }
            }
            this.Source.setSheet(x);
            this.SelectedSheet = s;
        } catch (Exception er) {

        }
    }

    public String getSelectedSheet() {
        return SelectedSheet;
    }

    public int getSelectedSheetIndex() {
        try {
            for (int i = 0; i < Source.getNumSheets(); i++) {
                if (Source.getSheetName(i).equals(SelectedSheet)) {
                    return i;
                }
            }
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(MatchSource.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public void setStartIndex(int i){
        this.StartIndex = i;
    }
    public int getStartIndex(){
        return StartIndex;
    }
    
    public void setEndIndex(int i){
        this.EndIndex =i;
    }
    public int getEndIndex(){
        return EndIndex;
    }

}
