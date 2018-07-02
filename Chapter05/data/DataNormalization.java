package edu.packt.neuralnet.data;

import edu.packt.neuralnet.math.ArrayOperations;


/**
 * 
 * DataNormalization
 * This abstract class allows normalize and denormalize data via 
 * MIN_MAX, AVG_STDDEV, MINUSONE_PLUSONE, MINUSTWO_PLUSTWO techniques
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */

public class DataNormalization {

	/** 
	 * ENUM normalization values
	 */
	public enum NormalizationTypes {
		MIN_MAX, ZSCORE
	}
	
	/** 
	 * Constant to store statically normalization type
	 */
	public NormalizationTypes TYPE;
        
        private double[] minValues;
        private double[] maxValues;
        private double[] meanValues;
        private double[] stdValues;
        private double scaleNorm=1.0;        
        private double minNorm=-1.0;

	
        public DataNormalization(){
            
        }
        
        public DataNormalization(NormalizationTypes nType){
            this.TYPE=nType;
        }
        
        public DataNormalization(double _scaleNorm){
            this.TYPE=NormalizationTypes.ZSCORE;
            this.scaleNorm=_scaleNorm;
        }
        
        public DataNormalization(double _minNorm,double _maxNorm){
            this.TYPE=NormalizationTypes.MIN_MAX;
            this.minNorm=_minNorm;
            this.scaleNorm=_maxNorm-_minNorm;            
        }
        
        public DataNormalization(NormalizationTypes nType,double[][] data){
            this.TYPE=nType;
            calculateReference(data);
        }
        
        public DataNormalization(NormalizationTypes nType,double[] data){
            this.TYPE=nType;
            calculateReference(data);
        }

        
        public DataNormalization(double[][] data,double _minNorm,double _maxNorm){
            this.TYPE=NormalizationTypes.MIN_MAX;
            this.minNorm=_minNorm;
            this.scaleNorm=_maxNorm-_minNorm;
            calculateReference(data);
        }
        
        public DataNormalization(double[][] data,double _zscale){
            this.TYPE=NormalizationTypes.ZSCORE;
            this.scaleNorm=_zscale;
            calculateReference(data);
        }
        
        private void calculateReference(double[][] data){
            minValues=ArrayOperations.min(data);
            maxValues=ArrayOperations.max(data);
            meanValues=ArrayOperations.mean(data);
            stdValues=ArrayOperations.stdev(data);
        }
        
        private void calculateReference(double[] data){
            minValues=new double[1];
            minValues[0]=ArrayOperations.min(data);
            maxValues=new double[1];
            maxValues[0]=ArrayOperations.max(data);
            meanValues=new double[1];
            meanValues[0]=ArrayOperations.mean(data);
            stdValues=new double[1];
            stdValues[0]=ArrayOperations.stdev(data);
        }
        
	/** 
	 * Normalizes data matrix
	 * @param data matrix to be normalized
	 * @return normalized matrix
	 */
	public double[][] normalize( double[][] data ) {

		int rows = data.length;
		int cols = data[0].length;
                
                if((minValues==null)&&(maxValues==null)&&(meanValues==null)&&(stdValues==null))
                    calculateReference(data);

		double[][] normalizedData = new double[rows][cols];
		
                for(int i=0;i<rows;i++){
                    for(int j=0;j<cols;j++){
                        switch (TYPE){
                            case MIN_MAX:
                                normalizedData[i][j]=(minNorm) + ((data[i][j] - minValues[j]) / ( maxValues[j] - minValues[j] )) * (scaleNorm);
                                break;
                            case ZSCORE:
                                normalizedData[i][j]=scaleNorm * (data[i][j] - meanValues[j]) / stdValues[j];
                                break;
                        }
                    }
                }
                return normalizedData;
	}
        
        public double[] normalize( double[] data ) {

		int rows = data.length;
                
                if((minValues==null)&&(maxValues==null)&&(meanValues==null)&&(stdValues==null))
                    calculateReference(data);

		double[] normalizedData = new double[rows];
		
                for(int i=0;i<rows;i++){
                        switch (TYPE){
                            case MIN_MAX:
                                normalizedData[i]=(minNorm) + ((data[i] - minValues[0]) / ( maxValues[0] - minValues[0] )) * (scaleNorm);
                                break;
                            case ZSCORE:
                                normalizedData[i]=scaleNorm * (data[i] - meanValues[0]) / stdValues[0];
                                break;
                        }
                }
                return normalizedData;
	}
	
	/** 
	 * Denormalizes data matrix 
	 * @param data matrix of raw real numbers
	 * @param data matrix of normalized real numbers
	 * @return denormalized matrix 
	 */
	public double[][] denormalize(double[][] normalizedData) {
		int rows = normalizedData.length;
		int cols = normalizedData[0].length;
		
		double[][] denormalizedData = new double[rows][cols];
                
                for(int i=0;i<rows;i++){
                    for(int j=0;j<cols;j++){
                        switch(TYPE){
                            case MIN_MAX:
                                denormalizedData[i][j]=minValues[j]+(((normalizedData[i][j]-minNorm)/scaleNorm) *(maxValues[j]-minValues[j]));
                                break;
                            case ZSCORE:
                                denormalizedData[i][j]=meanValues[j]+((normalizedData[i][j]*stdValues[j])/scaleNorm);
                                break;
                        }
                    }
                }
                return denormalizedData;
	}
	
	public double[] denormalize(double[] normalizedData,int column) {
		int rows = normalizedData.length;
		
		double[] denormalizedData = new double[rows];
                
                for(int i=0;i<rows;i++){
                        switch(TYPE){
                            case MIN_MAX:
                                denormalizedData[i]=minValues[column]+(((normalizedData[i]-minNorm)/scaleNorm)*(maxValues[column]-minValues[column]));
                                break;
                            case ZSCORE:
                                denormalizedData[i]=meanValues[column]+((normalizedData[i]*stdValues[0])/scaleNorm);
                                break;
                        }
                }
                return denormalizedData;
	}
        
        public static void setNormalization(NeuralDataSet[] nn,double _zscale){
            nn[0].setNormalization(_zscale);
            for(int i=1;i<nn.length;i++){
                nn[i].setInputOutputNorm(nn[0].getInputNorm(), nn[0].getOutputNorm());
            }
        }
        
        public static void setNormalization(NeuralDataSet[] nn,double _min,double _max){
            nn[0].setNormalization(_min,_max);
            for(int i=1;i<nn.length;i++){
                nn[i].setInputOutputNorm(nn[0].getInputNorm(), nn[0].getOutputNorm());
            }
        } 
        
		
}
