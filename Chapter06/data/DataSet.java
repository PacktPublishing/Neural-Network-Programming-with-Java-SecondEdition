/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.data;

import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.math.ArrayOperations;
import java.awt.Paint;
import java.util.ArrayList;
import org.jfree.chart.ChartFrame;

/**
 * DataSet This class performs several math calculation.
 * For example: correlation, standard devination, mean, append new data 
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 */
public class DataSet {
    
    public ArrayList<String> columns;
    
    public ArrayList<ArrayList<Double>> data;
    
    public int numberOfColumns;
    public int numberOfRecords;
    
    public DataSet(){
    	
    }
    
    public DataSet(double[][] _data){
        numberOfRecords=_data.length;
        numberOfColumns=_data[0].length;
        columns = new ArrayList<>();
        for(int i=0;i<numberOfColumns;i++){
            columns.add("Column"+String.valueOf(i));
        }
        data = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            data.add(new ArrayList<Double>());
            for(int j=0;j<numberOfColumns;j++){
                data.get(i).add(_data[i][j]);
            }
        }
    }
    
    public DataSet(double[][] _data,String[] _columns){
        numberOfRecords=_data.length;
        numberOfColumns=_data[0].length;
        columns = new ArrayList<>();
        for(int i=0;i<numberOfColumns;i++){
            try{
                columns.add(_columns[i]);
            }
            catch(Exception e){
                columns.add("Column"+String.valueOf(i));
            }
        }
        data = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            data.add(new ArrayList<Double>());
            for(int j=0;j<numberOfColumns;j++){
                data.get(i).add(_data[i][j]);
            }
        }        
    }
    
    public DataSet(String path, String filename){
        double[][] _data=new LoadCsv(path,filename).getData(path,filename);
        numberOfRecords=_data.length;
        numberOfColumns=_data[0].length;
        columns = new ArrayList<>();
        for(int i=0;i<numberOfColumns;i++){
            columns.add("Column"+String.valueOf(i));
        }
        data = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            data.add(new ArrayList<Double>());
            for(int j=0;j<numberOfColumns;j++){
                data.get(i).add(_data[i][j]);
            }
        } 
    }
    
    public DataSet(String filename){
        double[][] _data=LoadCsv.getData(filename,false,",");
        numberOfRecords=_data.length;
        numberOfColumns=_data[0].length;
        columns = new ArrayList<>();
        for(int i=0;i<numberOfColumns;i++){
            columns.add("Column"+String.valueOf(i));
        }
        data = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            data.add(new ArrayList<Double>());
            for(int j=0;j<numberOfColumns;j++){
                data.get(i).add(_data[i][j]);
            }
        } 
    }
    
    public DataSet(String filename,boolean columnsInFirstRow,String separator){
        LoadCsv lcsv = new LoadCsv(filename,columnsInFirstRow,separator);
        double[][] _data=lcsv.getDataMatrix(filename,columnsInFirstRow,separator);
        numberOfRecords=_data.length;
        numberOfColumns=_data[0].length;
        columns = new ArrayList<>();
        if(columnsInFirstRow){
            String[] columnNames = lcsv.getColumnNames();
            for(int i=0;i<numberOfColumns;i++){
                columns.add(columnNames[i]);
            }
        }
        else{
            for(int i=0;i<numberOfColumns;i++){
                columns.add("Column"+String.valueOf(i));
            }
        }
        data = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            data.add(new ArrayList<Double>());
            for(int j=0;j<numberOfColumns;j++){
                data.get(i).add(_data[i][j]);
            }
        } 
    }
    
    public DataSet(){
        
    }
    
    public void addColumn(double[] _data){
        addColumn(_data,null);
    }
    
    public void addColumn(double[] _data,String name){
        if(name==null)
            columns.add("Column"+String.valueOf(numberOfColumns));
        else
            columns.add(name);
        for(int i=0;i<numberOfRecords;i++){
            data.get(i).add(_data[i]);
        }
        numberOfColumns++;
    }
    
    public void setColumnValues(double[] _data,String name){
        setColumnValues(_data,getColumnIndex(name));
    }
    
    public void setColumnValues(double[] _data,int colIndex){
        for(int i=0;i<_data.length;i++){
            data.get(i).set(colIndex, _data[i]);
        }
    }
    
    public String[] getColumns(){
        String[] result = new String[numberOfColumns];
        for(int i=0;i<numberOfColumns;i++)
            result[i]=columns.get(i);
        return result;
    }
    
    public int getColumnIndex(String columnName){
        for(int i=0;i<numberOfColumns;i++){
            if(columnName == null ? columns.get(i) == null : columnName.equals(columns.get(i)))
                return i;
        }
        return -1;
    }
    
    public void appendData(double[][] _data){
        for(int i=0;i<_data.length;i++){
            data.add(new ArrayList<Double>());
            for(int j=0;j<numberOfColumns;j++){
                data.get(numberOfRecords+i).add(_data[i][j]);
            }
        }
    }
    
    
    public double[][] getData(){
        return ArrayOperations.arrayListToDoubleMatrix(data);
    }
    
    public double[][] getData(int[] columns){
        return ArrayOperations.getMultipleColumns(getData(), columns);
    }
    
    public double[] getData(int col){
        return ArrayOperations.getColumn(getData(), col);
    }
            
    
    
    public double correlation(int colx,int coly){
        double[] arrx = ArrayOperations.getColumn(data,colx);
        double[] arry = ArrayOperations.getColumn(data,coly);
        double[] arrxy = ArrayOperations.elementWiseProduct(arrx, arry);
        double meanxy = ArrayOperations.mean(arrxy);
        double meanx = ArrayOperations.mean(arrx);
        double meany = ArrayOperations.mean(arry);
        double stdx = ArrayOperations.stdev(arrx);
        double stdy = ArrayOperations.stdev(arry);
        return (meanxy-meanx*meany)/(stdx*stdy);
    }
    
    public double mean(int col){
        double[] arrcol = ArrayOperations.getColumn(data, col);
        return ArrayOperations.mean(arrcol);
    }
    
    public double stdev(int col){
        double[] arrcol = ArrayOperations.getColumn(data, col);
        return ArrayOperations.stdev(arrcol);
    }
    
    public void save(String filename,String separator){
        LoadCsv.dataMatrix2csv(getData(), getColumns(), filename, separator);
    }
    
    public void save(String path,String filename,String separator){
        LoadCsv.dataMatrix2csv(getData(), getColumns(), path, filename, separator);
    }
    
    public void dropNaN(double substvalue){
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfColumns;j++){
                double value = data.get(i).get(j);
                if(Double.isNaN(value))
                    data.get(i).set(j,substvalue);
            }
        }
    }
    
    public void dropNaN(){
        ArrayList<ArrayList<Double>> newData = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            boolean hasNaN = false;
            for(int j=0;j<numberOfColumns;j++){
                if(Double.isNaN(data.get(i).get(j))){
                    hasNaN=true;
                    break;
                }
            }
            if(!hasNaN){
                ArrayList<Double> newLine = new ArrayList<>();
                for(int j=0;j<numberOfColumns;j++){
                    double value = data.get(i).get(j);
                    newLine.add(value);
                }
                newData.add(newLine);
            }
        }
        this.data=newData;
        this.numberOfRecords=newData.size();
    }
    
    public ChartFrame getScatterChart(String title,int colx,int coly,Paint color){
        int[] cols = {colx,coly};
        String[] sns = {"Records"};
        Paint[] scl = {color};
        double[][] chartdata = this.getData(cols);
        Chart chart = new Chart(title,chartdata,sns,0,scl,Chart.SeriesType.DOTS);
        ChartFrame frame = new ChartFrame(title, chart.scatterPlot(columns.get(colx), columns.get(coly)));
        frame.pack();
        return frame;
    }
    
    public ChartFrame getScatterChart(String title,String colx,String coly,Paint color){
        return getScatterChart(title,getColumnIndex(colx),getColumnIndex(coly),color);
    }
    
    public void sortBy(int col){
        double[] value = this.getData(col);
        double[] ordered = ArrayOperations.quickSort(value);
        int[] ordIndexes = ArrayOperations.getIndexes(value, ordered);
        ArrayList<ArrayList<Double>> newData = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            ArrayList<Double> newLine = new ArrayList<>();            
            for(int j=0;j<numberOfColumns;j++){
                double v = data.get(ordIndexes[i]).get(j);
                newLine.add(v);
            }
            newData.add(newLine);
        }
        this.data=newData;

    }

}
