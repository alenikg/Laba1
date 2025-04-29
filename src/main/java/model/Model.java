/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author elenagoncarova
 */
public class Model {
    public double[][] importedDataMatrix = new double[0][];
    public String[] names = new String[0];
    
    public void importFromXlsxFile(String filePath, int listIndex) throws IOException, URISyntaxException {
        ImportedResult result = readXlsx(filePath, listIndex);
        prepareImportedDataMatrix(result.dataMatrix);
        prepareNames(result.names);
    }
    
    private void prepareImportedDataMatrix (ArrayList<ArrayList<Double>> originData) {
        if (originData == null || originData.isEmpty()){
            return;
        }
        System.out.println(originData);
        double[][] result = new double[originData.get(0).size()][originData.size()];
        for (int i = 0; i < originData.size(); i++) {
            for (int j = 0; j < originData.get(i).size(); j++) {
                result[j][i] = originData.get(i).get(j);
            }
        } 
        System.out.println(Arrays.toString(result));
        importedDataMatrix = result;
    }
    
    private void prepareNames (ArrayList<String> originNames) {
        if (originNames == null || originNames.isEmpty()){
            return;
        }
        System.out.println(originNames);
        String[] result = new String[originNames.size()];
        for (int i = 0; i < originNames.size(); i++) {
            result[i] = originNames.get(i);
        }
        System.out.println(Arrays.toString(result));
        this.names = result;
    }
    
    private ImportedResult readXlsx(String filePath, int listIndex) throws IOException, URISyntaxException{
        ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
        ArrayList<String> names = new ArrayList<String>();
        
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook bookNow = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheetNow = bookNow.getSheetAt(listIndex);
        
        int index = 0;
        Iterator<Row> rowIterator = sheetNow.iterator();
        while (rowIterator.hasNext()) {
            ArrayList<Double> values = new ArrayList<Double>();
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        values.add(cell.getNumericCellValue());
                        break;
                    case FORMULA:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        values.add(cell.getNumericCellValue());
                        break;
                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        if (index == 0) {
                            names.add(cell.getStringCellValue());
                        }
                        break;
                }
            }
            if (!values.isEmpty()) {
                result.add(values);
            }
            index++;
        }
        System.out.println("Reading File Completed.");
        fileInputStream.close();

        return new ImportedResult(names, result);
    }
    
    public double[][] getImportedDataMatrix() {
        return importedDataMatrix;
    }
    
    public String[] getNames() {
        return names;
    }
}
