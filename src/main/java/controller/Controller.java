/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import model.Model;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.Frame;

public class Controller {
    private Frame frame;
    private Model model;
    private ArrayList<ArrayList<Double>> resultMatrix = new ArrayList<>();
    private double[][] covMatrix = new double[0][];
    
    private String[] resultListNames = new String[] {
        "Ср геом",
        "Ср ариф",
        "Ст откл",
        "Размах",
        "Кол-во эл",
        "Вариация",
        "Дов инт",
        "Дисперсия",
        "Макс",
        "Мин",
    };
    
    public Controller(Frame userFrame, Model model) {
        this.frame = userFrame;
        this.model = model;
        this.frame.setImportHandler((this::handleImport));
        this.frame.setCalculateHandler((this::handleCalculate));
        this.frame.setExportHandler((this::handleExport));
    }
    
    private void handleImport(String filePath, int listIndex) {
        try {
            this.model.importFromXlsxFile(filePath, listIndex);
        } catch (Exception e) {
            e.printStackTrace();
            String message = "Не получилось загрузить из файла " + e.getMessage();
            frame.showMessage(message);
        }
    }
    
    private void handleCalculate() {
        prepareResultMatrix();
        calculateGeometricMean();
        calculateArithmeticAverage();
        calculateStandardDeviation();
        calculateRange();
        calculateLength();
        calculateVariationCoefficient();
        calculateConfidenceInterval();
        calculateVariance();
        calculateMax();
        calculateMin();
        
        calculateCovariance();
        System.out.println(resultMatrix);
    }

    private void handleExport(String filePath) {
        try {
            this.writeIntoExcel(filePath);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            String message = "Не получилось выгрузить в файл " + e.getMessage();
            frame.showMessage(message);
        }
    }
    
    public void writeIntoExcel(String file) throws IOException {
        if (resultMatrix == null || resultMatrix.isEmpty()){
            return;
        }
        Workbook xlsx = new XSSFWorkbook();
        Sheet sheet = xlsx.createSheet("List 1");
        
        Row rowHeader = sheet.createRow(0);
                Cell firstCell = rowHeader.createCell(0);
        firstCell.setCellValue("");
        for (int n = 0; n < Math.min(this.resultMatrix.get(0).size(), resultListNames.length); n++) {
            String title = resultListNames[n];
            Cell rowHeaderCell = rowHeader.createCell(n + 1);
            rowHeaderCell.setCellValue(title);
        }
        for (int i = 0; i < this.resultMatrix.size(); i++) {
            ArrayList<Double> resultRow = this.resultMatrix.get(i);
            Row row = sheet.createRow(i + 1);
            Cell titleCell = row.createCell(0);
            
            String[] headers = this.model.getNames();
                if (i < headers.length) {
                titleCell.setCellValue(headers[i]);
            } else {
                titleCell.setCellValue("Нет данных");
            }
            for (int j = 0; j < resultRow.size(); j++) {
                double resultItem = resultRow.get(j);
                Cell resultCell = row.createCell(j + 1);
                resultCell.setCellValue(resultItem);
            }
        }
        this.addCovMatrixXlssList(xlsx);
        xlsx.write(new FileOutputStream(file));
        xlsx.close();
        System.exit(0);
    }
    
    private void addCovMatrixXlssList(Workbook xlsx) {
        if (covMatrix == null || covMatrix.length == 0){
            return;
        }
        Sheet sheet = xlsx.createSheet("covariance");
        Row rowHeader = sheet.createRow(0);
        Cell firstCell = rowHeader.createCell(0);
        firstCell.setCellValue("");
        for (int n = 0; n < this.covMatrix[0].length; n++) {
            Cell rowHeaderCell = rowHeader.createCell(n + 1);
            rowHeaderCell.setCellValue(this.model.getNames()[n]);
        }
        for (int i = 0; i < this.covMatrix.length; i++) {
            double[] resultRow = this.covMatrix[i];
            Row row = sheet.createRow(i + 1);
            Cell titleCell = row.createCell(0);
            titleCell.setCellValue(this.model.getNames()[i]);
            for (int j = 0; j < resultRow.length; j++) {
                double resultItem = resultRow[j];
                Cell resultCell = row.createCell(j + 1);
                resultCell.setCellValue(resultItem);
            }
        }
    }
    
    private double[] getArray(ArrayList<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i).doubleValue();
        }
        return arr;
    }

    private void prepareResultMatrix() {
        resultMatrix = new ArrayList<>();
        for (int i = 0; i < this.model.getImportedDataMatrix().length; i++) {
            resultMatrix.add(new ArrayList<>());
        }
    }

    private void calculateArithmeticAverage() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double result = StatUtils.mean(row);
            resultMatrix.get(i).add(result);
      }
    }
    
    private void calculateGeometricMean() {

        double[][] importedMatrix = this.model.getImportedDataMatrix();
        
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double result = StatUtils.geometricMean(row);
            resultMatrix.get(i).add(result);
//            if (result == null) {
//                Cell resultCell = resultMatrix.get(i);
//                resultMatrix.get(i).setCellValue("Нет данных");
//            }
        }
    }

    private void calculateStandardDeviation() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        StandardDeviation standardDeviation = new StandardDeviation();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double result = standardDeviation.evaluate(row);
            resultMatrix.get(i).add(result);
        }
    }
    
    private void calculateRange() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double min = StatUtils.min(row);
            double max = StatUtils.max(row);
            double result = max - min;
            resultMatrix.get(i).add(result);
        }
    }
    

    private void calculateCovariance() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        Covariance covarianceCalc = new Covariance();
        int importedMatrixLength = importedMatrix.length;
        this.covMatrix = new double[importedMatrixLength][importedMatrixLength];
        for (int i = 0; i < importedMatrixLength; i++) {
            for (int j = i; j < importedMatrixLength; j++) {
               double result = covarianceCalc.covariance(importedMatrix[i], importedMatrix[j]);
               this.covMatrix[i][j] = result;
               this.covMatrix[j][i] = result;
            }
        }
        System.out.println(Arrays.deepToString(this.covMatrix));
    }

    
    private void calculateLength() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double result = importedMatrix[i].length;
            resultMatrix.get(i).add(result);
        }
    }
    
    private void calculateVariationCoefficient() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double mean = StatUtils.mean(importedMatrix[i]);
            double stdDev = new StandardDeviation().evaluate(importedMatrix[i]);
            double variationCoefficient = stdDev / mean;
            resultMatrix.get(i).add(variationCoefficient);
        }
    }
    
    private void calculateConfidenceInterval() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] data = importedMatrix[i];
            double mean = StatUtils.mean(data);
            double stdDev = new StandardDeviation().evaluate(data);
            int n = data.length;
            double marginOfError = 1.96 * (stdDev / Math.sqrt(n));
            resultMatrix.get(i).add(mean - marginOfError);
            resultMatrix.get(i).add(mean + marginOfError);
        }
    }

    private void calculateVariance() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double result = StatUtils.variance(row);
            resultMatrix.get(i).add(result);
        }  
    }
        
    private void calculateMax() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double result = StatUtils.max(row);
            resultMatrix.get(i).add(result);
        }
    }

    private void calculateMin() {
        double[][] importedMatrix = this.model.getImportedDataMatrix();
        for (int i = 0; i < importedMatrix.length; i++) {
            double[] row = importedMatrix[i];
            double result = StatUtils.min(row);
            resultMatrix.get(i).add(result);
        }
    } 
}
