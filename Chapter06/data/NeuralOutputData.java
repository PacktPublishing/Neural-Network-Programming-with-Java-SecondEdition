package edu.packt.neuralnet.data;

import java.util.ArrayList;

import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.math.ArrayOperations;

/** 
 * This class keeps neural output parameters and operations 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 */
public class NeuralOutputData {
    
    public int numberOfOutputs=0;
    public int numberOfRecords=0;
    
    public NeuralNet neuralNet;
 
    public ArrayList<ArrayList<Double>> targetData;
    
    public ArrayList<ArrayList<Double>> neuralData;
    
    public ArrayList<ArrayList<Double>> normTargetData;
    
    public ArrayList<ArrayList<Double>> normNeuralData;
    
    private DataNormalization norm;
    
    public NeuralOutputData(){
    	
    }
    
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
    
    public NeuralOutputData(double[][] _data){
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

    public ArrayList<ArrayList<Double>> getTargetDataArrayList(boolean isNorm){
        if(isNorm)
            return this.normTargetData;
        else
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
    
    public double[][] getTargetData(boolean isNorm){
        if(isNorm)
            return ArrayOperations.arrayListToDoubleMatrix(normTargetData);
        else
            return ArrayOperations.arrayListToDoubleMatrix(targetData);
    }
    
    public ArrayList<ArrayList<Double>> getNeuralDataArrayList(){
        return this.neuralData;
    }
    
    public ArrayList<ArrayList<Double>> getNeuralDataArrayList(boolean isNorm){
        if(isNorm)
            return this.normNeuralData;
        else
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
    
    public double[][] getNeuralData(boolean isNorm){
        double[][] result = new double[numberOfRecords][numberOfOutputs];
        for(int i=0;i<numberOfRecords;i++){
            for(int j=0;j<numberOfOutputs;j++){
                double value;
                if(isNorm)
                    value=this.normNeuralData.get(i).get(j);
                else
                    value=this.neuralData.get(i).get(j);
                result[i][j]=value;
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
    
    public void setNeuralData(double[][] _data,boolean isNorm){
        if(isNorm){
            this.normNeuralData=new ArrayList<>();
            for(int i=0;i<numberOfRecords;i++){
                this.normNeuralData.add(new ArrayList<Double>());
                if(this.numberOfOutputs==0){
                    this.numberOfOutputs=_data[i].length;
                }
                for(int j=0;j<numberOfOutputs;j++){
                    this.normNeuralData.get(i).add(_data[i][j]);
                }
            }
            double[][] deNorm = norm.denormalize(_data);
            for(int i=0;i<numberOfRecords;i++)
                for(int j=0;j<numberOfOutputs;j++)
                    this.neuralData.get(i).set(j,deNorm[i][j]);
        }
        else
            setNeuralData(_data);
    }
    
    public void setNeuralData(ArrayList<ArrayList<Double>> _data){
        this.neuralData=_data;
    }
    
    public void setNeuralData(ArrayList<ArrayList<Double>> _data,boolean isNorm){
        if(isNorm){
            this.normNeuralData=_data;
            double[][] deNorm = norm.denormalize(ArrayOperations.arrayListToDoubleMatrix(_data));
            for(int i=0;i<numberOfRecords;i++)
                for(int j=0;j<numberOfOutputs;j++)
                    this.neuralData.get(i).set(j,deNorm[i][j]);
        }
        else
            this.neuralData=_data;
    }
    
    public void setNeuralData(int i,ArrayList<Double> _data){
        this.neuralData.set(i,_data);
    }
    
    public void setNeuralData(int i,ArrayList<Double> _data,boolean isNorm){
        if(isNorm){
            this.normNeuralData.set(i,_data);
            double[][] _mdata = ArrayOperations.convertToRowMatrix(ArrayOperations.arrayListToVector(_data));
            double[][] deNorm = norm.denormalize(_mdata);
            for(int j=0;j<numberOfOutputs;j++)
                this.neuralData.get(i).set(j,deNorm[0][j]);
        }
        else
            setNeuralData(i, _data);
    }
    
    public void setNeuralData(int i,double[] _data){
        for(int j=0;j<numberOfOutputs;j++){
            this.neuralData.get(i).set(j, _data[j]);
        }
    }
    
    public void setNeuralData(int i,double[] _data,boolean isNorm){
        if(isNorm){
            double[][] _mdata = new double[1][_data.length];
            for(int j=0;j<numberOfOutputs;j++){
                this.normNeuralData.get(i).set(j, _data[j]);
                _mdata[0][j]=_data[j];
            }
            double[][] deNorm = norm.denormalize(_mdata);
            for(int j=0;j<numberOfOutputs;j++)
                this.neuralData.get(i).set(j,deNorm[0][j]);
        }
        else
            setNeuralData(i,_data);
    }
    
    public ArrayList<Double> getTargetRecordArrayList(int i){
        return this.targetData.get(i);
    }
    
    public ArrayList<Double> getTargetRecordArrayList(int i,boolean isNorm){
        if(isNorm)
            return this.normTargetData.get(i);
        else
            return this.targetData.get(i);
    }
    
    public double[] getTargetRecord(int i){
        double[] result=new double[numberOfOutputs];
        for(int j=0;j<numberOfOutputs;j++){
            result[j]=this.targetData.get(i).get(j);
        }
        return result;
    }
    
    public double[] getTargetRecord(int i,boolean isNorm){
        if(isNorm)
            return ArrayOperations.getRow(normTargetData, i);
        else
            return ArrayOperations.getRow(targetData, i);
    }

    public ArrayList<Double> getRecordArrayList(int i){
        return this.neuralData.get(i);
    }
    
    public ArrayList<Double> getRecordArrayList(int i,boolean isNorm){
        if(isNorm)
            return this.normNeuralData.get(i);
        else
            return this.neuralData.get(i);
    }
    
    public double[] getRecord(int i){
        double[] result = new double[numberOfOutputs];
        for(int j=0;j<numberOfOutputs;j++){
            result[j]=this.neuralData.get(i).get(j);
        }
        return result;
    }
    
    public double[] getRecord(int i,boolean isNorm){
        if(isNorm)
            return ArrayOperations.getRow(normNeuralData, i);
        else
            return ArrayOperations.getRow(neuralData,i);
    }
    
    public ArrayList<Double> getTargetColumnArrayList(int i){
        ArrayList<Double> result = new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            double value = targetData.get(j).get(i);
            result.add(value);
        }
        return result;
    }
    
    public ArrayList<Double> getTargetColumnArrayList(int i,boolean isNorm){
        ArrayList<Double> result = new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            double value;
            if(isNorm)
                value = normTargetData.get(j).get(i);
            else
                value = targetData.get(j).get(i);
            result.add(value);
        }
        return result;
    }
    
    public double[] getTargetColumn(int i){
        double[] result = new double[numberOfRecords];
        for(int j=0;j<numberOfRecords;j++){
            result[j]=targetData.get(j).get(i);
        }
        return result;
    }
    
    public double[] getTargetColumn(int i,boolean isNorm){
        if(isNorm)
            return ArrayOperations.getColumn(normTargetData, i);
        else
            return ArrayOperations.getColumn(targetData, i);
    }
    
    public ArrayList<Double> getNeuralColumnArrayList(int i){
        ArrayList<Double> result = new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            result.add(neuralData.get(j).get(i));
        }
        return result;
    }
    
    public ArrayList<Double> getNeuralColumnArrayList(int i,boolean isNorm){
        ArrayList<Double> result = new ArrayList<>();
        for(int j=0;j<numberOfRecords;j++){
            double value;
            if(isNorm)
                value=normNeuralData.get(j).get(i);
            else
                value=neuralData.get(j).get(i);
            result.add(value);
        }
        return result;
    }
    
    public double[] getNeuralColumn(int i){
        double[] result= new double[numberOfRecords];
        for(int j=0;j<numberOfRecords;j++){
            result[j]=neuralData.get(j).get(i);
        }
        return result;
    }
    
    public double[] getNeuralColumn(int i,boolean isNorm){
        double[] result= new double[numberOfRecords];
        for(int j=0;j<numberOfRecords;j++){
            if(isNorm)
                result[j]=normNeuralData.get(j).get(i);
            else
                result[j]=neuralData.get(j).get(i);
        }
        return result;
    }    
    
    public void printTarget(){
        System.out.println("Targets:");
        //numberOfRecords = 10;
        for(int k=0;k<numberOfRecords;k++){
            //System.out.print("Target Output["+String.valueOf(k)+"]={ ");
            for(int i=0;i<numberOfOutputs;i++){
                if(i==numberOfOutputs-1){
                    System.out.print(String.valueOf(this.targetData.get(k).get(i))+"\n");
                }
                else{
                    System.out.print(String.valueOf(this.targetData.get(k).get(i))+"\t");
                }
            }
        }
    }
    
    public void printNeural(){
        System.out.println("Neural:");
        //numberOfRecords = 10;
        for(int k=0;k<numberOfRecords;k++){
            //System.out.print("Neural Output["+String.valueOf(k)+"]={ ");
            for(int i=0;i<numberOfOutputs;i++){
                if(i==numberOfOutputs-1){
                    System.out.print(String.valueOf(this.neuralData.get(k).get(i))+"\n");
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
    
    public void setNormalization(DataNormalization dn){
        this.norm=dn;
        double[][] origData = ArrayOperations.arrayListToDoubleMatrix(targetData);
        double[][] normData = dn.normalize(origData);
        normTargetData=new ArrayList<>();
        normNeuralData=new ArrayList<>();
        for(int i=0;i<normData.length;i++){
            normTargetData.add(new ArrayList<Double>());
            normNeuralData.add(new ArrayList<Double>());
            for(int j=0;j<normData[0].length;j++){
                normTargetData.get(i).add(normData[i][j]);
                normNeuralData.get(i).add(0.0);
            }
        }
    }
    
    public double[][] calculateConfusionMatrix(int[] estimatedData, double[] realData) {
    	double TP = 0.0;
		double TN = 0.0;
		double FP = 0.0;
		double FN = 0.0;
		for (int m = 0; m < estimatedData.length; m++) {
			if ( ( realData[m] == 1.0 && estimatedData[m] == 1.0 ) ) {
				TP++;
			} else if ( realData[m] == 0.0 && estimatedData[m] == 0.0 ) {
				TN++;						
			} else if ( realData[m] == 0.0 && estimatedData[m] == 1.0 ) {
				FP++;
			} else if ( realData[m] == 1.0 && estimatedData[m] == 0.0 ) {
				FN++;
			}
		}
		
		return new double[][] {{TP,FN},{FP,TN}};
		
	}
    
    public double[][] calculateConfusionMatrix(double[][] estimatedData, double[][] realData) {
    	double TP = 0;
    	double TN = 0;
    	double FP = 0;
    	double FN = 0;
		for (int m = 0; m < getTargetData().length; m++) {
			if ( ( realData[m][0] == 1.0 && realData[m][1] == 0.0 )
					&& ( estimatedData[m][0] == 1.0 && estimatedData[m][1] == 0.0 ) ) {
				TP++;
			} else if ( ( realData[m][0] == 0.0 && realData[m][1] == 1.0 )
					&& (  estimatedData[m][0] == 0.0 && estimatedData[m][1] == 1.0 ) ) {
				TN++;						
			} else if ( ( realData[m][0] == 1.0 && realData[m][1] == 0.0 )
					&& (  estimatedData[m][0] == 0.0 && estimatedData[m][1] == 1.0 ) ) {
				FP++;
			} else if ( ( realData[m][0] == 0.0 && realData[m][1] == 1.0 )
					&& (  estimatedData[m][0] == 1.0 && estimatedData[m][1] == 0.0 ) ) {
				FN++;
			}
		}
		
		return new double[][] {{TP,FN},{FP,TN}};
		
	}
    
    public void calculatePerformanceMeasures(double[][] confMat) {
		// positive class error rate
		double errorRatePositive = confMat[0][1] / (confMat[0][0]+confMat[0][1]);
		// negative class error rate
		double errorRateNegative = confMat[1][0] / (confMat[1][0]+confMat[1][1]);
		// total error rate
		double totalErrorRate = (confMat[0][1] + confMat[1][0]) / (confMat[0][0] + confMat[0][1] + confMat[1][0] + confMat[1][1]);
		// total accuracy
		double totalAccuracy  = (confMat[0][0] + confMat[1][1]) / (confMat[0][0] + confMat[0][1] + confMat[1][0] + confMat[1][1]);
		// precision
		double precision = confMat[0][0] / (confMat[0][0]+confMat[1][0]);
		// sensibility
		double sensibility = confMat[0][0] / (confMat[0][0]+confMat[0][1]);
		// specificity
		double specificity = confMat[1][1] / (confMat[1][0]+confMat[1][1]);
		
		System.out.println("### PERFORMANCE MEASURES ###");
		System.out.println("positive class error rate: "+(errorRatePositive*100.0)+"%");
		System.out.println("negative class error rate: "+(errorRateNegative*100.0)+"%");
		System.out.println("total error rate: "+(totalErrorRate*100.0)+"%");
		System.out.println("total accuracy: "+(totalAccuracy*100.0)+"%");
		System.out.println("precision: "+(precision*100.0)+"%");
		System.out.println("sensibility: "+(sensibility*100.0)+"%");
		System.out.println("specificity: "+(specificity*100.0)+"%");
		
	}
}
