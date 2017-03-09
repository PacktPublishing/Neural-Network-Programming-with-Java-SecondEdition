/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.data;

import edu.packt.neuralnet.NeuralNet;
import java.util.ArrayList;

/**
 *
 * @author fab
 */
public class NeuralInputData {
    
    public int numberOfInputs=0;
    public int numberOfRecords=0;
    
    public NeuralNet neuralNet;
 
    public ArrayList<ArrayList<Double>> data;
    
    public NeuralInputData(ArrayList<ArrayList<Double>> _data){
        this.numberOfRecords=_data.size();
        this.numberOfInputs=_data.get(0).size();
        this.data=_data;
    }
    
    public NeuralInputData(Double[][] _data){
        this.numberOfRecords=_data.length;
        this.data=new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            this.data.add(new ArrayList<Double>());
            if(this.numberOfInputs==0){
                this.numberOfInputs=_data[i].length;
            }
            for(int j=0;j<numberOfInputs;j++){
                this.data.get(i).add(_data[i][j]);
            }
        }
    }
    
    public ArrayList<Double> getRecordArrayList(int i){
        return this.data.get(i);
    }
    
    public double[] getRecord(int i){
        double[] result=new double[numberOfInputs];
        for(int j=0;j<numberOfInputs;j++){
            result[j]=this.data.get(i).get(j);
        }
        return result;
    }
    
    public ArrayList<ArrayList<Double>> getDataArrayList(){
        return this.data;
    }
    
    public Double[][] getData(){
        Double[][] result=new Double[numberOfRecords][numberOfInputs];
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfInputs;j++){
                result[i][j]=this.data.get(i).get(j);
            }
        }
        return result;
    }
    
    public ArrayList<Double> getColumnDataArrayList(int i){
        ArrayList<Double> result=new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            result.add(data.get(j).get(i));
        }
        return result;
    }
    
    public double[] getColumn(int i){
        double[] result=new double[numberOfRecords];
        for(int j=0;j<numberOfRecords;j++){
            result[j]=data.get(j).get(i);
        }
        return result;
    }
    
    public void print(){
        System.out.println("Inputs:");
        for(int k=0;k<numberOfRecords;k++){
            System.out.print("Input["+String.valueOf(k)+"]={ ");
            for(int i=0;i<numberOfInputs;i++){
                if(i==numberOfInputs-1){
                    System.out.print(String.valueOf(this.data.get(k).get(i))+"}\n");
                }
                else{
                    System.out.print(String.valueOf(this.data.get(k).get(i))+"\t");
                }
            }
        }
    }
    
    public ArrayList<Double> getMeanInputData(){
        ArrayList<Double> result=new ArrayList<>();
        for(int i=0;i<numberOfInputs;i++){
            Double r=0.0;
            result.add(r);
            for(int k=0;k<numberOfRecords;k++){
                r+=data.get(k).get(i);
            }
            result.set(i, r/((double)numberOfRecords));
        }
        return result;
    }
    
}
