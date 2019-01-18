/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ford.fulkerson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Iginčan
 */
public class FordFulkerson {
    
    private static int[] vrchol;
    private static int[][] hrana;
    private static int[][] y;                   //y - tok hrany

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int pocetVrcholov = 0;
        
        vrchol = null;
        hrana = null;
        y = null;
        
        try {
            BufferedReader in = new BufferedReader(new FileReader("graf.txt"));
            pocetVrcholov = Integer.parseInt(in.readLine());
            hrana = new int[pocetVrcholov + 1][pocetVrcholov + 1];                          //graf.txt
            String riadok;                                                                  //prvy riadok - pocet vrcholov
            while ((riadok = in.readLine()) != null) {                                      //kazdy dalsi riadok - jednotlive hrany s ohodnoteniami
                String[] arRiadok = riadok.split("\\s+");                                   //napr. 1 2 3 - hrana {1,2} s ohodnotenim 3
                /*for (String string : arRiadok) {
                    System.out.println(string + "/");
                }*/
                hrana[Integer.parseInt(arRiadok[1])][Integer.parseInt(arRiadok[2])] = Integer.parseInt(arRiadok[3]);
                //hrana[Integer.parseInt(arRiadok[2])][Integer.parseInt(arRiadok[1])] = Integer.parseInt(arRiadok[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        vrchol = new int[pocetVrcholov + 1];
        for (int i = 1; i < vrchol.length; i++) {
            vrchol[i] = i;
        }
        
        y = new int[vrchol.length][vrchol.length];
        
        ArrayList<Integer> polocesta = null;
        int rezerva;
        
        while(true) {
            polocesta = najdiZvacsujucuPolocestu();

            if (polocesta == null) {
                for (int i = 1; i < vrchol.length; i++) {
                    for (int j = 1; j < vrchol.length; j++) {
                        if (hrana[i][j] != 0) {
                            System.out.println("c(" + i + "," + j + ") = " + hrana[i][j] + "; y(" + i + "," + j + ") = " + y[i][j]);
                        }
                    }
                }
                return;
            }

            rezerva = dajRezervu((ArrayList<Integer>)polocesta.clone());

            for (int i = 1; i < vrchol.length; i++) {
                for (int j = 1; j < vrchol.length; j++) {
                    if (hrana[i][j] != 0) {
                        if (jeHranaVSmere((ArrayList<Integer>)polocesta.clone(), i, j)) {
                            y[i][j] += rezerva;
                        } else if (jeHranaVSmere((ArrayList<Integer>)polocesta.clone(), j, i)) {
                            y[i][j] -= rezerva;
                        }
                    }
                }
            }
        }
        
    }
    
    public static boolean jeHranaVSmere(ArrayList<Integer> polocesta, int i, int j) {
        int i1;
        int j1;
        while (true) {
            if (polocesta.size() > 1) {
                i1 = polocesta.remove(0);
                j1 = polocesta.get(0);
                if (i == i1 && j == j1) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }
    
    public static int dajRezervu(ArrayList<Integer> polocesta) {
        int rezerva = Integer.MAX_VALUE;
        while (true) {
            if (polocesta.size() > 1) {
                int i = polocesta.remove(0);
                int j = polocesta.get(0);
                if (hrana[i][j] != 0) {
                    if (rezerva > hrana[i][j] - y[i][j]) {
                        rezerva = hrana[i][j] - y[i][j];
                    } 
                } else if (hrana[j][i] != 0) {
                    if (rezerva > y[j][i]) {
                        rezerva = y[j][i];
                    }
                }
            } else {
                return rezerva;
            }
        }
    }
    
    public static ArrayList<Integer> najdiZvacsujucuPolocestu() {
        int zdroj = 1;
        int ustie = vrchol.length - 1;
        int[] x = new int[vrchol.length];                   //x - predosly vrchol polocesty
        ArrayList<Integer> E = new ArrayList<>();           //E - pomocna mnozina Epsylon
        ArrayList<Integer> polocesta = new ArrayList<>();   //polocesta - vrcholy iduce v poradi polocesty
        int r = zdroj;                                      //riadiaci vrchol (aktualny vrchol)
        
        E.add(zdroj);
        for (int i = 1; i < vrchol.length; i++) {
            x[i] = Integer.MAX_VALUE;
        }
        x[zdroj] = 0;
        while (true) {
            if (x[ustie] < Integer.MAX_VALUE) {
                for (int i = ustie; i != 0; i = Math.abs(x[i])) {
                    polocesta.add(0, i);
                }
                return polocesta;
            }
            if (E.isEmpty()) {
                return null;
            }
            r = E.remove(0);
            for (int j = 1; j < vrchol.length; j++) {
                if (hrana[r][j] != 0) {
                    if (x[j] == Integer.MAX_VALUE) {
                        if (y[r][j] < hrana[r][j]) {
                            x[j] = r;
                            E.add(j);
                        }
                    }
                }
                if (hrana[j][r] != 0) {
                    if (x[j] == Integer.MAX_VALUE) {
                        if (y[j][r] > 0) {
                            x[j] = -r;
                            E.add(j);
                        }
                    }
                }
            }
        }
    }
    
}
