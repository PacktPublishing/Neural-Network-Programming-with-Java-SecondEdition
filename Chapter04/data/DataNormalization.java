package edu.packt.neuralnet.data;

import java.util.Arrays;

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
		MIN_MAX, AVG_STDDEV, MINUSONE_PLUSONE, MINUSTWO_PLUSTWO
	}
	
	/** 
	 * Constant to store statically normalization type
	 */
	public NormalizationTypes TYPE;
	
	/** 
	 * Normalizes data matrix
	 * @param data matrix to be normalized
	 */
	public double[][] normalize( double[][] data ) {

		int rows = data.length;
		int cols = data[0].length;
		double colAverage = 0.0;
		double colStdDev  = 0.0;

		double[][] normalizedData = new double[rows][cols];
		
		switch (TYPE) {
			case MINUSONE_PLUSONE:
				return customNormalize(data, -1.0, 1.0);
			case MINUSTWO_PLUSTWO:
				return customNormalize(data, -2.0, 2.0);
			default:
		}

		for (int cols_i = 0; cols_i < cols; cols_i++) {
			double[] column = ArrayOperations.getColumn(data, cols_i);
			
			double maxValue = getMaxValueArray(column);
			
			if(TYPE == NormalizationTypes.AVG_STDDEV) {
				colAverage = getAverageArray(column);
				colStdDev  = getStdDevArray(column, colAverage);
			}

			for (int rows_j = 0; rows_j < column.length; rows_j++) {
				switch (TYPE) {
					case MIN_MAX:
						normalizedData[rows_j][cols_i] = data[rows_j][cols_i] / Math.abs( maxValue );
					break;
					case AVG_STDDEV:
						normalizedData[rows_j][cols_i] = (data[rows_j][cols_i] - colAverage) / colStdDev;
					break;
					default:
						throw new IllegalArgumentException(TYPE + " does not exist in NormalizationTypesENUM");
				}
			}

		}

		return normalizedData;

	}
	
	/** 
	 * Gets the average value of an array of real numbers
	 * @param data matrix to be normalized
	 * @return
	 */
	private double getAverageArray(double[] column) {
		double sum = 0.0;
		for (Double value : column) {
			sum += value;
		}
		
		return (sum / column.length);
		
	}

	/** 
	 * Gets the standard deviation value of an array of real numbers
	 * @param data matrix to be normalized
	 * @param average value of array
	 * @return  
	 */
	private double getStdDevArray(double[] column, double average) {
		double sum = 0.0;
		for (Double value : column) {
			sum += Math.pow(value - average, 2);
		}
		
		return Math.sqrt( (sum / (column.length-1)) );
		
	}

	/** 
	 * Gets the maximum value of an array of real numbers
	 * @param data matrix of real numbers
	 * @return  
	 */
	public double getMaxValueArray(double[] data) {
		double[] copy = Arrays.copyOf(data, data.length);
		Arrays.sort(copy);
		return copy[data.length - 1];
	}

	/** 
	 * Gets the minimum value of an array of real numbers
	 * @param data matrix of real numbers
	 * @return  
	 */
	public double getMinValueArray(double[] data) {
		double[] copy = Arrays.copyOf(data, data.length);
		Arrays.sort(copy);
		return copy[0];
	}

	/** 
	 * Normalizes data matrix as custom range
	 * @param data matrix of real numbers
	 * @param minimum normalized value
	 * @param maximum normalized value 
	 * @return  
	 */
	public double[][] customNormalize(double[][] data, double minNormValue, double maxNormValue) {

		int rows = data.length;
		int cols = data[0].length;

		double[][] normalizedData = new double[rows][cols];

		for (int cols_i = 0; cols_i < cols; cols_i++) {
			
			double[] column = ArrayOperations.getColumn(data, cols_i);

			double minValue = getMinValueArray(column);
			double maxValue = getMaxValueArray(column);
			
			for (int rows_j = 0; rows_j < column.length; rows_j++) {
				
				normalizedData[rows_j][cols_i] = (minNormValue) + ((data[rows_j][cols_i] - minValue) / ( maxValue - minValue )) * (maxNormValue - minNormValue);
				
			}

		}

		return normalizedData;

	}
	
	/** 
	 * Denormalizes data matrix 
	 * @param data matrix of raw real numbers
	 * @param data matrix of normalized real numbers
	 * @return  
	 */
	public double[][] denormalize(double[][] data, double[][] dataNormalized) {
		int rows = data.length;
		int cols = data[0].length;
		double colAverage = 0.0;
		double colStdDev  = 0.0;
		
		double[][] denormalizedData = new double[rows][cols];
		
		if(TYPE == NormalizationTypes.MINUSONE_PLUSONE || TYPE == NormalizationTypes.MINUSTWO_PLUSTWO) {
			return customDenormalize(data, dataNormalized);			
		} else {

			for (int cols_i = 0; cols_i < cols; cols_i++) {
				double[] column = ArrayOperations.getColumn(data, cols_i);
				
				double maxValue = getMaxValueArray(column);
				
				if(TYPE == NormalizationTypes.AVG_STDDEV) {
					colAverage = getAverageArray(column);
					colStdDev  = getStdDevArray(column, colAverage);
				}

				for (int rows_j = 0; rows_j < column.length; rows_j++) {
					switch (TYPE) {
						case MIN_MAX:
							denormalizedData[rows_j][cols_i] = dataNormalized[rows_j][cols_i] * Math.abs( maxValue );
						break;
						case AVG_STDDEV:
							denormalizedData[rows_j][cols_i] = (dataNormalized[rows_j][cols_i] * colStdDev) + colAverage;
						break;
						default:
							throw new IllegalArgumentException(TYPE + " does not exist in NormalizationTypesENUM");
					}
				}

			} 
		}
		return denormalizedData;
	}
	
	/** 
	 * Denormalizes data matrix as custom range
	 * @param data matrix of raw real numbers
	 * @param data matrix of normalized real numbers
	 * @return  
	 */
	private double[][] customDenormalize(double[][] data, double[][] dataNormalized) {

		int rows = data.length;
		int cols = data[0].length;

		double[][] denormalizedData = new double[rows][cols];

		for (int cols_i = 0; cols_i < cols; cols_i++) {
			
			double[] columnRaw  = ArrayOperations.getColumn(data, cols_i);
			double[] columnNorm = ArrayOperations.getColumn(dataNormalized, cols_i);

			double smallerValue  = getMinValueArray(columnRaw);
			double largerValue  = getMaxValueArray(columnRaw);
			double minValue = getMinValueArray(columnNorm);
			double maxValue = getMaxValueArray(columnNorm);
			
			for (int rows_j = 0; rows_j < columnRaw.length; rows_j++) {
				
				denormalizedData[rows_j][cols_i] = (smallerValue) + ((dataNormalized[rows_j][cols_i] - minValue) * ( largerValue - smallerValue )) / (maxValue - minValue);
				
			}

		}

		return denormalizedData;

	}

	
	/** 
	 * Denormalizes data array 
	 * @param data array of raw real numbers
	 * @param data array of normalized real numbers
	 * @return  
	 */
	public double[] denormalize(double[] data, double[] dataNormalized) {
		int rows = data.length;
		double colAverage = 0.0;
		double colStdDev  = 0.0;
		
		double[] denormalizedData = new double[rows];
		
		if(TYPE == NormalizationTypes.MINUSONE_PLUSONE || TYPE == NormalizationTypes.MINUSTWO_PLUSTWO) {
			return customDenormalize(data, dataNormalized);			
		} else {
			
			double maxValue = getMaxValueArray(data);
			
			if(TYPE == NormalizationTypes.AVG_STDDEV) {
				colAverage = getAverageArray(data);
				colStdDev  = getStdDevArray(data, colAverage);
			}

			for (int rows_j = 0; rows_j < data.length; rows_j++) {
				switch (TYPE) {
					case MIN_MAX:
						denormalizedData[rows_j] = dataNormalized[rows_j] * Math.abs( maxValue );
					break;
					case AVG_STDDEV:
						denormalizedData[rows_j] = (dataNormalized[rows_j] * colStdDev) + colAverage;
					break;
					default:
						throw new IllegalArgumentException(TYPE + " does not exist in NormalizationTypesENUM");
				}
			}

		}
		return denormalizedData;
	}
	
	/** 
	 * Denormalizes data array as custom range
	 * @param data array of raw real numbers
	 * @param data array of normalized real numbers
	 * @return  
	 */
	private double[] customDenormalize(double[] data, double[] dataNormalized) {

		int rows = data.length;

		double[] denormalizedData = new double[rows];
			
		double smallerValue  = getMinValueArray(data);
		double largerValue  = getMaxValueArray(data);
		double minValue = getMinValueArray(dataNormalized);
		double maxValue = getMaxValueArray(dataNormalized);
		
		for (int rows_j = 0; rows_j < data.length; rows_j++) {
			
			denormalizedData[rows_j] = (smallerValue) + ((dataNormalized[rows_j] - minValue) * ( largerValue - smallerValue )) / (maxValue - minValue);
			
		}

		return denormalizedData;

	}

	
		
}
