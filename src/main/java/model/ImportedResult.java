/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author elenagoncarova
 */
public class ImportedResult {
    public ArrayList<String> names;
    public ArrayList<ArrayList<Double>> dataMatrix;
    
    public ImportedResult (ArrayList<String> names, ArrayList<ArrayList<Double>> dataMatrix){
        this.names = names;
        this.dataMatrix = dataMatrix;
    }
}
