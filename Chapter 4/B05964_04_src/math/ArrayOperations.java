/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.math;

import java.util.ArrayList;

/**
 *
 * @author fab
 */
public class ArrayOperations {
    
    private ArrayOperations(){
        
    }
    
    public static double max(double[] array){
        double result=Double.MIN_VALUE;
        for(int i=0;i<array.length;i++){
            if(i==0)
                result=array[i];
            else{
                result=Math.max(array[i],result);
            }
        }
        return result;
    }
    
    public static int indexmax(double[] array){
        int result=0;
        double maxValue=array[0];
        for(int i=1;i<array.length;i++){
            if(maxValue<array[i]){
                maxValue=array[i];
                result=i;
            }
        }
        return result;
    }
    
    public static double min(double[] array){
        double result=Double.MAX_VALUE;
        for(int i=0;i<array.length;i++){
            if(i==0)
                result=array[i];
            else{
                result=Math.min(array[i],result);
            }
        }
        return result;
    }
    
    public static int indexmin(double[] array){
        int result=0;
        double minValue=array[0];
        for(int i=1;i<array.length;i++){
            if(minValue>array[i]){
                minValue=array[i];
                result=i;
            }
        }
        return result;
    }
    
    
    public static float[][] convertToFloat(double[][] data){
        float[][] result = new float[data.length][];
        for(int i=0;i<data.length;i++){
            result[i]=new float[data[i].length];
            for(int j=0;j<data[i].length;j++){
                result[i][j]=(float)data[i][j];
            }
        }
        return result;
    }
    
    public static double[][] transposeMatrix(double[][] data) {
    	double[][] result = new double[data[0].length][data.length];
    	
    	for(int i=0; i < data.length; i++){
            for(int j=0; j < data[i].length; j++){
                result[j][i] = data[i][j]; 
            }
        }
    	
    	return result;
    }
    
    public static double[][] arrayListToDoubleMatrix(ArrayList<ArrayList<Double>> al){
        int numberOfRows = al.size();
        int numberOfColumns = al.get(0).size();
        double[][] data= new double[numberOfRows][numberOfColumns];
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++){
                data[i][j]= al.get(i).get(j);
            }
        }
        return data;
    }
    
    public static double[] getElements(double[] array,int posStart,int posEnd){
        double[] result = new double[posEnd-posStart+1];
        for(int i=posStart;i<=posEnd;i++){
            result[i-posStart]=array[i];
        }
        return result;
    }
    
    public static double[][] getElements(double[][] array,int posStartRow,int posEndRow,int posStartColumn,int posEndColumn){
        double[][] result = new double[posEndRow-posStartRow+1][posEndColumn-posStartColumn];
        for(int i=posStartRow;i<=posEndRow;i++){
            for(int j=posStartColumn;j<=posEndColumn;j++){
                result[i-posStartRow][j-posStartColumn]=array[i][j];
            }
        }
        return result;
    }
    
    public static double[] getColumn(double[][] array, int colIndex) {
		double[] column = new double[array.length];

		for (int i = 0; i < column.length; i++) {
			column[i] = array[i][colIndex];
		}

		return column;

	}
    
}

