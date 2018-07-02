package edu.packt.neuralnet.data;

import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.math.ArrayOperations;
import edu.packt.neuralnet.math.RandomNumberGenerator;

import java.util.ArrayList;

/** 
 * This class keeps neural data set parameters and operations 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class NeuralDataSet {
    public NeuralInputData inputData;
    public NeuralOutputData outputData;
    
    public NeuralNet neuralNet;
    
    public int numberOfInputs;
    public int numberOfOutputs;
    
    public int numberOfRecords;
    
    public DataNormalization inputNorm;
    public DataNormalization outputNorm;
    
    public String[] inputNames;
    public String[] outputNames;
    public String[] targetNames;
    
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
        double[][] _inputData  = new double[numberOfRecords][numberOfInputs];
        double[][] _outputData = new double[numberOfRecords][numberOfOutputs];
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
    
    public NeuralDataSet(double[][] _data,int numberOfOutputColumns){
        numberOfInputs=_data[0].length;
        numberOfOutputs=numberOfOutputColumns;
        numberOfRecords=_data.length;
        double[][] _inputData=new double[_data.length][];
        for(int i=0;i<_data.length;i++){
            _inputData[i]=new double[_data[i].length];
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

    public ArrayList<ArrayList<Double>> getArrayInputData(boolean isNorm){
        if(isNorm)
            return inputData.normdata;
        else
            return inputData.data;
    }
    
    public ArrayList<ArrayList<Double>> getArrayTargetOutputData(){
        return outputData.getTargetDataArrayList();
    }

    public ArrayList<ArrayList<Double>> getArrayTargetOutputData(boolean isNorm){
        return outputData.getTargetDataArrayList(isNorm);
    }
    
    public ArrayList<ArrayList<Double>> getArrayNeuralOutputData(){
        return outputData.getNeuralDataArrayList();
    }

    public ArrayList<ArrayList<Double>> getArrayNeuralOutputData(boolean isNorm){
        return outputData.getNeuralDataArrayList(isNorm);
    }
    
    public ArrayList<Double> getArrayInputRecord(int i){
        return inputData.getRecordArrayList(i);
    }

    public ArrayList<Double> getArrayInputRecord(int i,boolean isNorm){
        return inputData.getRecordArrayList(i,isNorm);
    }
    
    public double[] getInputRecord(int i){
        return inputData.getRecord(i);
    }
    
    public double[] getInputRecord(int i,boolean isNorm){
        return inputData.getRecord(i,isNorm);
    }    
    
    public ArrayList<Double> getArrayTargetOutputRecord(int i){
        return outputData.getTargetRecordArrayList(i);
    }

    public ArrayList<Double> getArrayTargetOutputRecord(int i,boolean isNorm){
        return outputData.getTargetRecordArrayList(i,isNorm);
    }
    
    public double[] getTargetOutputRecord(int i){
        return outputData.getTargetRecord(i);
    }

    public double[] getTargetOutputRecord(int i,boolean isNorm){
        return outputData.getTargetRecord(i,isNorm);
    }
    
    public ArrayList<Double> getArrayNeuralOutputRecord(int i){
        return outputData.getRecordArrayList(i);
    }

    public ArrayList<Double> getArrayNeuralOutputRecord(int i,boolean isNorm){
        return outputData.getRecordArrayList(i,isNorm);
    }
    
    public double[] getNeuralOutputRecord(int i){
        return outputData.getRecord(i);
    }
    
    public double[] getNeuralOutputRecord(int i,boolean isNorm){
        return outputData.getRecord(i,isNorm);
    }    
    
    public void setNeuralOutput(int i,ArrayList<Double> _neuralData){
        this.outputData.setNeuralData(i, _neuralData);
    }

    public void setNeuralOutput(int i,ArrayList<Double> _neuralData,boolean isNorm){
        this.outputData.setNeuralData(i, _neuralData,isNorm);
    }
    
    public void setNeuralOutput(int i,double[] _neuralData){
        this.outputData.setNeuralData(i, _neuralData);
    }

    public void setNeuralOutput(int i,double[] _neuralData,boolean isNorm){
        this.outputData.setNeuralData(i, _neuralData,isNorm);
    }
    
    public ArrayList<Double> getIthInputArrayList(int i){
        return this.inputData.getColumnDataArrayList(i);
    }

    public ArrayList<Double> getIthInputArrayList(int i,boolean isNorm){
        return this.inputData.getColumnDataArrayList(i,isNorm);
    }
    
    public double[] getIthInput(int i){
        return this.inputData.getColumn(i);
    }

    public double[] getIthInput(int i,boolean isNorm){
        return this.inputData.getColumn(i,isNorm);
    }
    
    public ArrayList<Double> getIthTargetOutputArrayList(int i){
        return this.outputData.getTargetColumnArrayList(i);
    }

    public ArrayList<Double> getIthTargetOutputArrayList(int i,boolean isNorm){
        return this.outputData.getTargetColumnArrayList(i,isNorm);
    }
    
    public double[] getIthTargetOutput(int i){
        return this.outputData.getTargetColumn(i);
    }

    public double[] getIthTargetOutput(int i,boolean isNorm){
        return this.outputData.getTargetColumn(i,isNorm);
    }
    
    public ArrayList<Double> getIthNeuralOutputArrayList(int i){
        return this.outputData.getNeuralColumnArrayList(i);
    }
    
    public ArrayList<Double> getIthNeuralOutputArrayList(int i, boolean isNorm){
        return this.outputData.getNeuralColumnArrayList(i,isNorm);
    }
    
    public double[] getIthNeuralOutput(int i){
        return this.outputData.getNeuralColumn(i);
    }
    
    public double[] getIthNeuralOutput(int i,boolean isNorm){
        return this.outputData.getNeuralColumn(i,isNorm);
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
    
    public double[][] getTargetVsEstimated(int output) {
    	return ArrayOperations.transposeMatrix( new double[][] { outputData.getTargetColumn(output), outputData.getNeuralColumn(output) } );
    }
    
    public void setNormalization(DataNormalization.NormalizationTypes nType){
        inputNorm = new DataNormalization(nType);
        outputNorm = new DataNormalization(nType);
    }
    
    public void setNormalization(double _scaleNorm){
        inputNorm = new DataNormalization(_scaleNorm);
        inputData.setNormalization(inputNorm);
        outputNorm = new DataNormalization(_scaleNorm);
        outputData.setNormalization(outputNorm);
    }
    
    public void setNormalization(double _minNorm,double _maxNorm){
        inputNorm = new DataNormalization(_minNorm,_maxNorm);
        inputData.setNormalization(inputNorm);
        outputNorm = new DataNormalization(_minNorm,_maxNorm);
        outputData.setNormalization(outputNorm);
    }
    
    public void setInputOutputNorm(DataNormalization in,DataNormalization out){
        this.setInputNorm(in);
        this.setOutputNorm(out);
    }
    
    public void setNormalizationFrom(NeuralDataSet nds){
        this.setInputOutputNorm(nds.getInputNorm(), nds.getOutputNorm());
    }
    
    public void setInputNorm(DataNormalization dn){
        inputNorm=dn;
        inputData.setNormalization(dn);
    }
    
    public void setOutputNorm(DataNormalization dn){
        outputNorm=dn;
        outputData.setNormalization(dn);
    }
    
    public DataNormalization getInputNorm(){
        return inputNorm;
    }
    
    public DataNormalization getOutputNorm(){
        return outputNorm;
    }
    
    public static NeuralDataSet[] randomSeparateTrainTest(DataSet ds,int[] inputColumns,int[] outputColumns,double percTrain){
        int numTrain=new Double(Math.ceil(percTrain*ds.numberOfRecords)).intValue();
        int numTest=ds.numberOfRecords-numTrain;
        int[] hashedIndexes = RandomNumberGenerator.hashInt(0, ds.numberOfRecords-1);
        int ic = inputColumns.length;
        int nc = ic+outputColumns.length;
        int[] inpcol = new int[ic];
        int[] outcol = new int[nc-ic];
        for(int i=0;i<ic;i++)
            inpcol[i]=i;
        for(int i=ic;i<nc;i++)
            outcol[i-ic]=i;
        double[][] traindata = new double[numTrain][nc];
        double[][] testdata = new double[numTest][nc];
        double[][] alldata = ds.getData();
        for(int i=0;i<ds.numberOfRecords;i++){
            for(int j=0;j<nc;j++){
                int jj;
                if(j<ic)
                    jj=inputColumns[j];
                else
                    jj=outputColumns[j-ic];
                if(i<numTrain)
                    traindata[i][j]=alldata[hashedIndexes[i]][jj];
                else
                    testdata[i-numTrain][j]=alldata[hashedIndexes[i]][jj];
            }
        }
        NeuralDataSet train = new NeuralDataSet(traindata, inpcol, outcol);
        NeuralDataSet test = new NeuralDataSet(testdata,inpcol,outcol);
        NeuralDataSet[] result = new NeuralDataSet[2];
        result[0]=train;
        result[1]=test;
        return result;
    }
    
    public void simulate(){
        boolean normalization=(inputNorm!=null);
        for(int i=0;i<numberOfRecords;i++){
            neuralNet.setInputs(this.getInputRecord(i,normalization));
            neuralNet.calc();
            this.setNeuralOutput(i, neuralNet.getOutputs(),normalization);
        }
    }
    
}
