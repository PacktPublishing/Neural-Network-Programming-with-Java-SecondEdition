/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.data;

import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.math.ArrayOperations;

import java.util.ArrayList;

/**
 *
 * @author fab
 */
public class NeuralDataSet {
    public NeuralInputData inputData;
    public NeuralOutputData outputData;
    
    public NeuralNet neuralNet;
    
    public int numberOfInputs;
    public int numberOfOutputs;
    
    public int numberOfRecords;
    
    public NeuralDataSet(ArrayList<ArrayList<Double>> _data,int[] inputColumns,int[] outputColumns){
        numberOfInputs=inputColumns.length;
        numberOfOutputs=outputColumns.length;
        numberOfRecords=_data.size();
        ArrayList<ArrayList<Double>> _inputData=new ArrayList<>();
        ArrayList<ArrayList<Double>> _outputData=new ArrayList<>();
        for(int i=0;i<numberOfInputs;i++){
            _inputData.add(_data.get(inputColumns[i]));
        }
        for(int i=0;i<numberOfOutputs;i++){
            _outputData.add(_data.get(outputColumns[i]));
        }
        inputData=new NeuralInputData(_inputData);
        outputData=new NeuralOutputData(_outputData);
    }
    
    public NeuralDataSet(double[][] _data,int[] inputColumns,int[] outputColumns){
        numberOfInputs=inputColumns.length;
        numberOfOutputs=outputColumns.length;
        numberOfRecords=_data.length;
        Double[][] _inputData=new Double[numberOfRecords][numberOfInputs];
        Double[][] _outputData=new Double[numberOfRecords][numberOfOutputs];
        for(int i=0;i<numberOfInputs;i++){
            for(int j=0;j<numberOfRecords;j++){
                _inputData[j][i]=_data[j][inputColumns[i]];
            }
        }
        for(int i=0;i<numberOfOutputs;i++){
            for(int j=0;j<numberOfRecords;j++){
                _outputData[j][i]=_data[j][outputColumns[i]];
            }
        }
        inputData=new NeuralInputData(_inputData);
        outputData=new NeuralOutputData(_outputData);
    }
    
    public NeuralDataSet(Double[][] _data,int numberOfOutputColumns){
        numberOfInputs=_data[0].length;
        numberOfOutputs=numberOfOutputColumns;
        numberOfRecords=_data.length;
        Double[][] _inputData=_data;
        inputData=new NeuralInputData(_inputData);        
        outputData=new NeuralOutputData(numberOfRecords,numberOfOutputs);
    }
    
    public NeuralDataSet(double[][] _data,int numberOfOutputColumns){
        numberOfInputs=_data[0].length;
        numberOfOutputs=numberOfOutputColumns;
        numberOfRecords=_data.length;
        Double[][] _inputData=new Double[_data.length][];
        for(int i=0;i<_data.length;i++){
            _inputData[i]=new Double[_data[i].length];
            for(int j=0;j<_data[i].length;j++){
                _inputData[i][j]=_data[i][j];
            }
        }
        inputData=new NeuralInputData(_inputData);        
        outputData=new NeuralOutputData(numberOfRecords,numberOfOutputs);
    }
    
    public ArrayList<ArrayList<Double>> getArrayInputData(){
        return inputData.data;
    }
    
    public ArrayList<ArrayList<Double>> getArrayTargetOutputData(){
        return outputData.getTargetDataArrayList();
    }
    
    public ArrayList<ArrayList<Double>> getArrayNeuralOutputData(){
        return outputData.getNeuralDataArrayList();
    }
    
    public ArrayList<Double> getArrayInputRecord(int i){
        return inputData.getRecordArrayList(i);
    }
    
    public double[] getInputRecord(int i){
        return inputData.getRecord(i);
    }
    
    public ArrayList<Double> getArrayTargetOutputRecord(int i){
        return outputData.getTargetRecordArrayList(i);
    }
    
    public double[] getTargetOutputRecord(int i){
        return outputData.getTargetRecord(i);
    }
    
    public ArrayList<Double> getArrayNeuralOutputRecord(int i){
        return outputData.getRecordArrayList(i);
    }
    
    public double[] getNeuralOutputRecord(int i){
        return outputData.getRecord(i);
    }
    
    public void setNeuralOutput(int i,ArrayList<Double> _neuralData){
        this.outputData.setNeuralData(i, _neuralData);
    }
    
    public void setNeuralOutput(int i,double[] _neuralData){
        this.outputData.setNeuralData(i, _neuralData);
    }
    
    public ArrayList<Double> getIthInputArrayList(int i){
        return this.inputData.getColumnDataArrayList(i);
    }
    
    public double[] getIthInput(int i){
        return this.inputData.getColumn(i);
    }
    
    public ArrayList<Double> getIthTargetOutputArrayList(int i){
        return this.outputData.getTargetColumnArrayList(i);
    }
    
    public double[] getIthTargetOutput(int i){
        return this.outputData.getTargetColumn(i);
    }
    
    public ArrayList<Double> getIthNeuralOutputArrayList(int i){
        return this.outputData.getNeuralColumnArrayList(i);
    }
    
    public double[] getIthNeuralOutput(int i){
        return this.outputData.getNeuralColumn(i);
    }
    
    public void printInput(){
        this.inputData.print();
    }
    
    public void printTargetOutput(){
        this.outputData.printTarget();
    }
    
    public void printNeuralOutput(){
        this.outputData.printNeural();
    }
    
    public ArrayList<Double> getMeanInput(){
        return this.inputData.getMeanInputData();
    }
    
    public ArrayList<Double> getMeanNeuralOutput(){
        return this.outputData.getMeanNeuralData();
    }
    
    public double[][] getRealVsEstimated(int output) {
    	
    	return ArrayOperations.transposeMatrix( new double[][] { outputData.getTargetColumn(output), outputData.getNeuralColumn(output) } );
    	
    }
    
}
