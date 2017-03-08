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
public class NeuralOutputData {
    
    public int numberOfOutputs=0;
    public int numberOfRecords=0;
    
    public NeuralNet neuralNet;
 
    public ArrayList<ArrayList<Double>> targetData;
    
    public ArrayList<ArrayList<Double>> neuralData;
    
    public NeuralOutputData(int _numberOfOutputs){
        this.numberOfOutputs=_numberOfOutputs;
    }
    
    public NeuralOutputData(ArrayList<ArrayList<Double>> _data){
        this.numberOfRecords=_data.size();
        this.numberOfOutputs=_data.get(0).size();
        this.targetData=_data;
        this.neuralData=new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            this.neuralData.add(new ArrayList());
            for(int j=0;j<numberOfOutputs;j++){
                this.neuralData.get(i).add(null);
            }
        }
    }
    
    public NeuralOutputData(Double[][] _data){
        this.numberOfRecords=_data.length;
        this.targetData=new ArrayList<>();
        this.neuralData=new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            this.targetData.add(new ArrayList<Double>());
            this.neuralData.add(new ArrayList<Double>());
            if(this.numberOfOutputs==0){
                this.numberOfOutputs=_data[i].length;
            }
            for(int j=0;j<numberOfOutputs;j++){
                this.targetData.get(i).add(_data[i][j]);
                this.neuralData.get(i).add(null);
            }
        }        
    }
    
    public NeuralOutputData(int _numberOfRecords, int _numberOfOutputs){
        this.numberOfRecords=_numberOfRecords;
        this.numberOfOutputs=_numberOfOutputs;
        this.targetData=null;
        this.neuralData=new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            this.neuralData.add(new ArrayList<Double>());
            for(int j=0;j<numberOfOutputs;j++){
                this.neuralData.get(i).add(null);
            }
        }         
    }
    
    public ArrayList<ArrayList<Double>> getTargetDataArrayList(){
        return this.targetData;
    }
    
    public Double[][] getTargetData(){
        Double[][] result=new Double[numberOfRecords][numberOfOutputs];
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfOutputs;j++){
                result[i][j]=this.targetData.get(i).get(j);
            }
        }
        return result;
    }
    
    public ArrayList<ArrayList<Double>> getNeuralDataArrayList(){
        return this.neuralData;
    }
    
    public Double[][] getNeuralData(){
        Double[][] result=new Double[numberOfRecords][numberOfOutputs];
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfOutputs;j++){
                result[i][j]=this.neuralData.get(i).get(j);
            }
        }
        return result;
    }
    
    public void setNeuralData(double[][] _data){
        this.neuralData=new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            this.neuralData.add(new ArrayList<Double>());
            if(this.numberOfOutputs==0){
                this.numberOfOutputs=_data[i].length;
            }
            for(int j=0;j<numberOfOutputs;j++){
                this.neuralData.get(i).add(_data[i][j]);
            }
        }
    }
    
    public void setNeuralData(ArrayList<ArrayList<Double>> _data){
        this.neuralData=_data;
    }
    
    public void setNeuralData(int i,ArrayList<Double> _data){
        this.neuralData.set(i,_data);
    }
    
    public void setNeuralData(int i,double[] _data){
        for(int j=0;j<numberOfOutputs;j++){
            this.neuralData.get(i).set(j, _data[j]);
        }
    }
    
    public ArrayList<Double> getTargetRecordArrayList(int i){
        return this.targetData.get(i);
    }
    
    public double[] getTargetRecord(int i){
        double[] result=new double[numberOfOutputs];
        for(int j=0;j<numberOfOutputs;j++){
            result[j]=this.targetData.get(i).get(j);
        }
        return result;
    }

    public ArrayList<Double> getRecordArrayList(int i){
        return this.neuralData.get(i);
    }
    
    public double[] getRecord(int i){
        double[] result = new double[numberOfOutputs];
        for(int j=0;j<numberOfOutputs;j++){
            result[j]=this.neuralData.get(i).get(j);
        }
        return result;
    }
    
    public ArrayList<Double> getTargetColumnArrayList(int i){
        ArrayList<Double> result = new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            result.add(targetData.get(j).get(i));
        }
        return result;
    }
    
    public double[] getTargetColumn(int i){
        double[] result=new double[numberOfRecords];
        for(int j=0;j<numberOfRecords;j++){
            result[j]=targetData.get(j).get(i);
        }
        return result;
    }
    
    public ArrayList<Double> getNeuralColumnArrayList(int i){
        ArrayList<Double> result = new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            result.add(neuralData.get(j).get(i));
        }
        return result;
    }
    
    public double[] getNeuralColumn(int i){
        double[] result=new double[numberOfRecords];
        for(int j=0;j<numberOfRecords;j++){
            result[j]=neuralData.get(j).get(i);
        }
        return result;
    }
    
    public void printTarget(){
        System.out.println("Targets:");
        for(int k=0;k<numberOfRecords;k++){
            System.out.print("Target Output["+String.valueOf(k)+"]={ ");
            for(int i=0;i<numberOfOutputs;i++){
                if(i==numberOfOutputs-1){
                    System.out.print(String.valueOf(this.targetData.get(k).get(i))+"}\n");
                }
                else{
                    System.out.print(String.valueOf(this.targetData.get(k).get(i))+"\t");
                }
            }
        }
    }
    
    public void printNeural(){
        System.out.println("Neural:");
        for(int k=0;k<numberOfRecords;k++){
            System.out.print("Neural Output["+String.valueOf(k)+"]={ ");
            for(int i=0;i<numberOfOutputs;i++){
                if(i==numberOfOutputs-1){
                    System.out.print(String.valueOf(this.neuralData.get(k).get(i))+"}\n");
                }
                else{
                    System.out.print(String.valueOf(this.neuralData.get(k).get(i))+"\t");
                }
            }
        }
    }
    
    public ArrayList<Double> getMeanNeuralData(){
        ArrayList<Double> result=new ArrayList<>();
        for(int j=0;j<numberOfOutputs;j++){
            Double r=0.0;
            result.add(r);
            for(int k=0;k<numberOfRecords;k++){
                r+=neuralData.get(k).get(j);
            }
            result.set(j, r/((double)numberOfRecords));
        }
        return result;
    }
    
    public ArrayList<Double> meanTargetData;    
    
}
