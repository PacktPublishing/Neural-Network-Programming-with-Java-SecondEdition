package edu.packt.neuralnet.data;

import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.math.ArrayOperations;

import java.util.ArrayList;

/** 
 * This class keeps neural input parameters and operations 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class NeuralInputData {
    
    public int numberOfInputs=0;
    public int numberOfRecords=0;
    
    public NeuralNet neuralNet;
 
    public ArrayList<ArrayList<Double>> data;
    
    public ArrayList<ArrayList<Double>> normdata;
    
    public DataNormalization norm;
    
    public NeuralInputData(ArrayList<ArrayList<Double>> _data){
        this.numberOfRecords=_data.size();
        this.numberOfInputs=_data.get(0).size();
        this.data=_data;
    }
    
    public NeuralInputData(double[][] _data){
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
    
    public ArrayList<Double> getRecordArrayList(int i,boolean isNorm){
        if(isNorm)
            return this.normdata.get(i);
        else
            return this.data.get(i);
    }
    
    public double[] getRecord(int i){
        double[] result=new double[numberOfInputs];
        for(int j=0;j<numberOfInputs;j++){
            result[j]=this.data.get(i).get(j);
        }
        return result;
    }
    
    public double[] getRecord(int i,boolean isNorm){
        double[] result=new double[numberOfInputs];
        for(int j=0;j<numberOfInputs;j++){
            if(isNorm)
                result[j]=this.normdata.get(i).get(j);
            else
                result[j]=this.data.get(i).get(j);
        }
        return result;
    }
    
    public ArrayList<ArrayList<Double>> getDataArrayList(){
        return this.data;
    }
    
    public ArrayList<ArrayList<Double>> getDataArrayList(boolean isNorm){
        if(isNorm)
            return this.normdata;
        else
            return this.data;
    }
    
    public double[][] getData(){
        double[][] result = new double[numberOfRecords][numberOfInputs];
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfInputs;j++){
                result[i][j]=this.data.get(i).get(j);
            }
        }
        return result;
    }
    
    public double[][] getData(boolean isNorm){
        double[][] result = new double[numberOfRecords][numberOfInputs];
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfInputs;j++){
                if(isNorm)
                    result[i][j]=this.normdata.get(i).get(j);
                else
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
    
    public ArrayList<Double> getColumnDataArrayList(int i,boolean isNorm){
        ArrayList<Double> result=new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            double value;
            if(isNorm)
                value=normdata.get(j).get(i);
            else
                value=data.get(j).get(i);
            result.add(value);
        }
        return result;
    }
    
    public double[] getColumn(int i){
    	return ArrayOperations.getColumn(ArrayOperations.arrayListToDoubleMatrix(data), i);
    }
    
    public double[] getColumn(int i,boolean isNorm){
        double[][] vdata;
        if(isNorm)
            vdata=ArrayOperations.arrayListToDoubleMatrix(normdata);
        else
            vdata=ArrayOperations.arrayListToDoubleMatrix(data);
        return ArrayOperations.getColumn(vdata, i);
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
    
    public void setNormalization(DataNormalization dn){
        double[][] origData = ArrayOperations.arrayListToDoubleMatrix(data);
        double[][] normData = dn.normalize(origData);
        normdata=new ArrayList<>();
        for(int i=0;i<normData.length;i++){
            normdata.add(new ArrayList<Double>());
            for(int j=0;j<normData[0].length;j++){
                normdata.get(i).add(normData[i][j]);
            }
        }
        
    }
    
}
