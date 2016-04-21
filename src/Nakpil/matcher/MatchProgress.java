/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nakpil.matcher;

import com.nakpil.StringMatcher;
import com.smartxls.WorkBook;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HERU
 */
public class MatchProgress implements Runnable {

    private MatchSource Primary;
    private MatchSource Secondary;
    private WorkBook Src1;
    private WorkBook Src2;
    private int SENSITIVITY;
    private DecimalFormat Formater = new DecimalFormat("#0.00");
    private StringMatcher Matcher = new StringMatcher();

    public static int PRIMARY_VS_SECONDARY = 0;
    public static int SECONDARY_VS_PRIMARY = 1;

    public MatchProgress(MatchSource src1, MatchSource src2, int sensitivity) {
        this.Primary = src1;
        this.Secondary = src2;
        this.SENSITIVITY = sensitivity;
        this.Src1 = Primary.getWorkBook();
        this.Src2 = Secondary.getWorkBook();
    }

    @Override
    public void run() {
        try {
            List<String> DuplicateList = new ArrayList();

            MatchUI.ProgressBar.setValue(0);
            MatchUI.ProgressBar.setMaximum(100);
            MatchUI.ProgressButton.setEnabled(false);
            MatchUI.MatchingConsole.setText("");
            String Prime;
            String Compared;

            MatchUI.ProgressBar.setValue(5);
            if (Primary.CheckForDuplicates()) {
                MatchUI.MatchingConsole.append("\n" + "================= Primary DuplicateCheck =================");
                for (int x = Primary.getStartIndex(); x <= Primary.getEndIndex(); x++) {
                    Prime = Src1.getText(Primary.getSourceColumn() + String.valueOf(x));
                    for (int y = Primary.getStartIndex(); y <= Primary.getEndIndex(); y++) {
                        Compared = Src1.getText(Primary.getSourceColumn() + String.valueOf(y));
                        System.out.println("Matching : " + Prime + " --> " + Compared);
                        if (Prime != null || Compared != null) {
                            if(x != y){
                            if (Double.parseDouble(Formater.format(100 * Matcher.similarity(Prime.toUpperCase(), Compared.toUpperCase()))) >= SENSITIVITY) {
                                DuplicateList.add(Prime + " --> " + Compared);
                                MatchUI.MatchingConsole.append("\n" + "Possible Duplicate : " + Prime + " --> " + Compared);
                            }
                            }
                        }
                    }
                }
                MatchUI.MatchingConsole.append("\n" + "========================= Result =========================");
                MatchUI.MatchingConsole.append("\n" + "Possible Duplicates : " + DuplicateList.size());
                MatchUI.MatchingConsole.append("\n" + "================= DuplicateCheckFinished =================");
            }
            MatchUI.ProgressBar.setValue(10);
            DuplicateList.clear();
            if (Secondary.CheckForDuplicates()) {
                MatchUI.MatchingConsole.append("\n" + "================ Secondary DuplicateCheck ================");
                for (int x = Secondary.getStartIndex(); x <= Secondary.getEndIndex(); x++) {
                    Prime = Src2.getText(Secondary.getSourceColumn() + String.valueOf(x));
                    for (int y = Secondary.getStartIndex(); y <= Secondary.getEndIndex(); y++) {
                        Compared = Src2.getText(Secondary.getSourceColumn() + String.valueOf(y));
                        if (Prime != null || Compared != null) {
                            if(x != y){
                            if (Double.parseDouble(Formater.format(100 * Matcher.similarity(Prime.toUpperCase(), Compared.toUpperCase()))) >= SENSITIVITY) {
                                DuplicateList.add(Prime + " --> " + Compared);
                                MatchUI.MatchingConsole.append("\n" + "Possible Duplicate : " + Prime + " --> " + Compared);
                            }
                            }
                        }
                    }
                }
                MatchUI.MatchingConsole.append("\n" + "========================= Result =========================");
                MatchUI.MatchingConsole.append("\n" + "Possible Duplicates : " + DuplicateList.size());
                MatchUI.MatchingConsole.append("\n" + "================= DuplicateCheckFinished =================");
            }
            MatchUI.ProgressBar.setValue(15);
            List<Integer> NoMatches_1 = new ArrayList();
            MatchUI.MatchingConsole.append("\n" + "================ Initial Matching Started ================");

            boolean hasMatch = true;
            for (int x = Primary.getStartIndex(); x <= Primary.getEndIndex(); x++) {
                Prime = Src1.getText(Primary.getSourceColumn() + String.valueOf(x));
                for (int y = Secondary.getStartIndex(); y <= Secondary.getEndIndex(); y++) {
                    Compared = Src2.getText(Secondary.getSourceColumn() + String.valueOf(y));
                    if (Prime != null || Compared != null) {
                        if (Double.parseDouble(Formater.format(100 * Matcher.similarity(Prime.toUpperCase(), Compared.toUpperCase()))) >= SENSITIVITY) {
                            Src1.setText(Primary.getTargetColumn() + String.valueOf(x), Compared);
                            break;
                        } else {
                            hasMatch = false;
                        }
                    }
                }
                if (hasMatch == false) {
                    NoMatches_1.add(x);
                    MatchUI.MatchingConsole.append("\n" + "No Match Found : " + Prime);
                }
            }
            MatchUI.MatchingConsole.append("\n" + "================ Initial Matching Results ================");
            MatchUI.MatchingConsole.append("\n" + "Remaining Unfound : " + NoMatches_1.size());
            System.out.println( NoMatches_1);
            MatchUI.ProgressBar.setValue(30);
            List<Integer> NoMatches_2 = new ArrayList();
            if (NoMatches_1.size() > 0) {
                MatchUI.MatchingConsole.append("\n" + "============= Remaining (" + NoMatches_1.size() + ") Matching Started =============");
                for (int i : NoMatches_1) {
                    Prime = Src1.getText(Primary.getSourceColumn() + String.valueOf(i));
                    for (int y = Secondary.getStartIndex(); y <= Secondary.getEndIndex(); y++) {
                        Compared = Src2.getText(Secondary.getSourceColumn() + String.valueOf(y));
                        if (Prime != null || Compared != null) {
                            System.out.println("Matching : " + Prime + " --> " + Compared);
                            if (Double.parseDouble(Formater.format(100 * Matcher.similarity(Prime.toUpperCase(), Compared.toUpperCase()))) >= SENSITIVITY - 2) {
                                Src1.setText(Primary.getTargetColumn() + String.valueOf(i), Compared);
                                break;
                            } else {
                                hasMatch = false;
                            }
                        }
                    }
                    if (hasMatch == false) {
                        NoMatches_2.add(i);
                        MatchUI.MatchingConsole.append("\n" + "No Match Found : " + Prime);
                    }
                }
                MatchUI.MatchingConsole.append("\n" + "============= Remaining (" + NoMatches_1.size() + ") Matching Results =============");
                MatchUI.MatchingConsole.append("\n" + "Remaining Unfound : " + NoMatches_2.size());
                MatchUI.ProgressBar.setValue(60);
                List<Integer> NoMatches_3 = new ArrayList();
                if (NoMatches_2.size() > 0) {
                    MatchUI.MatchingConsole.append("\n" + "============= Remaining (" + NoMatches_2.size() + ") Matching Results =============");
                    for (int i : NoMatches_2) {
                        Prime = Src1.getText(Primary.getSourceColumn() + String.valueOf(i));
                        for (int y = Secondary.getStartIndex(); y <= Secondary.getEndIndex(); y++) {
                            Compared = Src2.getText(Secondary.getSourceColumn() + String.valueOf(y));
                            if (Prime != null || Compared != null) {
                                if (Double.parseDouble(Formater.format(100 * Matcher.similarity(Prime.toUpperCase(), Compared.toUpperCase()))) >= SENSITIVITY - 4) {
                                    Src1.setText(Primary.getTargetColumn() + String.valueOf(i), Compared);
                                    break;
                                } else {
                                    hasMatch = false;
                                }
                            }
                        }
                        if (hasMatch == false) {
                            NoMatches_3.add(i);
                            MatchUI.MatchingConsole.append("\n" + "No Match Found : " + Prime);
                        }
                    }
                    MatchUI.MatchingConsole.append("\n" + "============= Remaining (" + NoMatches_2.size() + ") Matching Results =============");
                    MatchUI.MatchingConsole.append("\n" + "Remaining Unfound : " + NoMatches_3.size());
                    
                }
            }
            MatchUI.ProgressBar.setValue(80);
            Thread.sleep(2000);
            MatchUI.ProgressBar.setValue(85);
            Thread.sleep(2000);
            MatchUI.ProgressBar.setValue(90);
            Thread.sleep(2000);
            MatchUI.ProgressBar.setValue(95);
            Thread.sleep(2000);
            Src1.writeXLSX(Primary.getSourceFile().replace(".xl", " Matched.xl"));
            MatchUI.MatchingConsole.append("\n" + "================ MatcherProgress Finished ================");
            MatchUI.ProgressBar.setValue(100);
            MatchUI.ProcThread.interrupt();
            MatchUI.ProcThread = null;
            System.gc();
            MatchUI.ProgressButton.setEnabled(true);

        } catch (Exception er) {
            MatchUI.ProcThread = null;
            System.gc();
            MatchUI.ProgressButton.setEnabled(true);
        }
    }

}
