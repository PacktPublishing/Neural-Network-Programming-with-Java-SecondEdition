
package edu.packt.neuralnet.math;

import java.util.ArrayList;

/**
 * 
 * This class has static functions that are used to make array
 * operations, i.e., get max and min value of an array, calculate average and
 * std dev of an array, etc
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class ArrayOperations {

	private ArrayOperations() {

	}

	/**
	 * Gets the maximum value of an array of real numbers
	 * 
	 * @param data matrix of real numbers
	 * @return maximum value of array
	 */
	public static double max(double[] array) {
		double result = Double.MIN_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (i == 0)
				result = array[i];
			else {
				result = Math.max(array[i], result);
			}
		}
		return result;
	}
        
        public static double[] max(double[][] array){
            double[][] transp = ArrayOperations.transposeMatrix(array);
            double[] result = new double[transp.length];
            for(int i=0;i<transp.length;i++){
                result[i]=ArrayOperations.max(transp[i]);
            }
            return result;
        }

	/**
	 * Gets the maximum value index of an array of real numbers
	 * 
	 * @param data array of real numbers
	 * @return index of maximum value 
	 */
	public static int indexmax(double[] array) {
		int result = 0;
		double maxValue = array[0];
		for (int i = 1; i < array.length; i++) {
			if (maxValue < array[i]) {
				maxValue = array[i];
				result = i;
			}
		}
		return result;
	}

	/**
	 * Gets the minimum value of an array of real numbers
	 * 
	 * @param data matrix of real numbers
	 * @return minimum value of array
	 */
	public static double min(double[] array) {
		double result = Double.MAX_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (i == 0)
				result = array[i];
			else {
				result = Math.min(array[i], result);
			}
		}
		return result;
	}
        
        public static double[] min(double[][] array){
            double[][] transp = ArrayOperations.transposeMatrix(array);
            double[] result = new double[transp.length];
            for(int i=0;i<transp.length;i++){
                result[i]=ArrayOperations.min(transp[i]);
            }
            return result;
        }

	/**
	 * Gets the minimum value index of an array of real numbers
	 * 
	 * @param data array of real numbers
	 * @return index of minimum value
	 */
	public static int indexmin(double[] array) {
		int result = 0;
		double minValue = array[0];
		for (int i = 1; i < array.length; i++) {
			if (minValue > array[i]) {
				minValue = array[i];
				result = i;
			}
		}
		return result;
	}

	/**
	 * Gets the sum of an array of real numbers
	 * 
	 * @param data matrix to be normalized
	 * @return sum of array
	 */
	public static double sum(double[] array) {
		double result = 0.0;
		for (double x : array)
			result += x;
		return result;
	}
        
        public static double[] sum(double[][] array){
            double[][] transp = ArrayOperations.transposeMatrix(array);
            double[] result = new double[transp.length];
            for(int i=0;i<transp.length;i++){
                result[i]=ArrayOperations.sum(transp[i]);
            }
            return result;
        }

	/**
	 * Gets the mean of an array of real numbers
	 * 
	 * @param data matrix to be normalized
	 * @return mean of array
	 */
	public static double mean(double[] array) {
		return ArrayOperations.sum(array) / (double) array.length;
	}
        
        public static double[] mean(double[][] array){
            double[][] transp = ArrayOperations.transposeMatrix(array);
            double[] result = new double[transp.length];
            for(int i=0;i<transp.length;i++){
                result[i]=ArrayOperations.mean(transp[i]);
            }
            return result;
        }

	/**
	 * Gets the variance value of an array of real numbers
	 * 
	 * @param array with values
	 * @return variance of array
	 */
	public static double variance(double[] array) {
		double result = 0.0;
		double arrayMean = ArrayOperations.mean(array);
		for (int i = 0; i < array.length; i++) {
			result += Math.pow((array[i] - arrayMean), 2.0);
		}
		return result / (double) array.length;
	}
        
        public static double[] variance(double[][] array){
            double[][] transp = ArrayOperations.transposeMatrix(array);
            double[] result = new double[transp.length];
            for(int i=0;i<transp.length;i++){
                result[i]=ArrayOperations.variance(transp[i]);
            }
            return result;
        }

	/**
	 * Gets the standard deviation value of an array of real numbers
	 * 
	 * @param array with values
	 * @return standard deviation of array
	 */
	public static double stdev(double[] array) {
		double varArray = ArrayOperations.variance(array);
		return Math.sqrt(varArray);
	}
        
        public static double[] stdev(double[][] array){
            double[][] transp = ArrayOperations.transposeMatrix(array);
            double[] result = new double[transp.length];
            for(int i=0;i<transp.length;i++){
                result[i]=ArrayOperations.stdev(transp[i]);
            }
            return result;
        }

	/**
	 * Converts a double matrix to a float matrix
	 * 
	 * @param double matrix to be converted
	 * @return float matrix
	 */
	public static float[][] convertToFloat(double[][] data) {
		float[][] result = new float[data.length][];
		for (int i = 0; i < data.length; i++) {
			result[i] = new float[data[i].length];
			for (int j = 0; j < data[i].length; j++) {
				result[i][j] = (float) data[i][j];
			}
		}
		return result;
	}

	/**
	 * Transposes a double matrix
	 * 
	 * @param matrix to be transposed
	 * @return matrix transposed
	 */
	public static double[][] transposeMatrix(double[][] data) {
		double[][] result = new double[data[0].length][data.length];

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				result[j][i] = data[i][j];
			}
		}

		return result;
	}

	/**
	 * Converts a double ArrayList to a double matrix
	 * 
	 * @param ArrayList to be converted
	 * @return matrix converted to double[][]
	 */
	public static double[][] arrayListToDoubleMatrix(ArrayList<ArrayList<Double>> al) {
		int numberOfRows = al.size();
		int numberOfColumns = al.get(0).size();
		double[][] data = new double[numberOfRows][numberOfColumns];
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				data[i][j] = al.get(i).get(j);
			}
		}
		return data;
	}

	/**
	 * Gets elements of an double array
	 * 
	 * @param array with elements
	 * @param initial position
	 * @param final position
	 * @return array with elements within posStart and posEnd
	 */
	public static double[] getElements(double[] array, int posStart, int posEnd) {
		double[] result = new double[posEnd - posStart + 1];
		for (int i = posStart; i <= posEnd; i++) {
			result[i - posStart] = array[i];
		}
		return result;
	}

	/**
	 * Gets elements of an double matrix
	 * 
	 * @param matrix with elements
	 * @param initial row position
	 * @param final row position
	 * @param initial column position
	 * @param final column position
	 * @return matrix with elements within rows and cols positions
	 */
	public static double[][] getElements(double[][] array, int posStartRow, int posEndRow, int posStartColumn,
			int posEndColumn) {
		double[][] result = new double[posEndRow - posStartRow + 1][posEndColumn - posStartColumn];
		for (int i = posStartRow; i <= posEndRow; i++) {
			for (int j = posStartColumn; j <= posEndColumn; j++) {
				result[i - posStartRow][j - posStartColumn] = array[i][j];
			}
		}
		return result;
	}

	/**
	 * Gets column of an double matrix
	 * 
	 * @param matrix with elements
	 * @param columun index
	 * @return array with elements of column index
	 */
	public static double[] getColumn(double[][] array, int colIndex) {
		double[] column = new double[array.length];
		for (int i = 0; i < column.length; i++) {
			column[i] = array[i][colIndex];
		}

		return column;
	}
        
        public static double[] getRow(double[][] array, int rowIndex){
            double[] row = new double[array[rowIndex].length];
            for(int i=0;i<row.length;i++){
                row[i]=array[rowIndex][i];
            }
            return row;
        }

	/**
	 * Gets column of an ArrayList of ArrayList<Double>
	 * 
	 * @param ArrayList of ArrayList<Double>
	 * @param columun index
	 * @return array with elements of column index
	 */
	public static double[] getColumn(ArrayList<ArrayList<Double>> data, int colIndex) {
		double[] result = new double[data.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = data.get(i).get(colIndex);
		}
		return result;
	}
        
        public static double[] getRow(ArrayList<ArrayList<Double>> data, int rowIndex){
            double[] result = new double[data.get(rowIndex).size()];
            for(int i=0;i<result.length;i++){
                result[i]=data.get(rowIndex).get(i);
            }
            return result;
        }

	/**
	 * Gets multiple columns of a matrix
	 * 
	 * @param double matrix
	 * @param vector of columns indexes
	 * @return double matrix with columns of column indexes
	 */
	public static double[][] getMultipleColumns(double[][] array, int[] colIndexes) {
		double[][] result = new double[array.length][colIndexes.length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < colIndexes.length; j++) {
				result[i][j] = array[i][colIndexes[j]];
			}
		}
		return result;
	}

	/**
	 * Gets multiple columns of a matrix
	 * 
	 * @param ArrayList of ArrayList<Double>
	 * @param vector of columns indexes
	 * @return double matrix with columns of column indexes
	 */
	public static double[][] getMultipleColumns(ArrayList<ArrayList<Double>> data, int[] colIndexes) {
		double[][] result = new double[data.size()][colIndexes.length];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < colIndexes.length; j++) {
				result[i][j] = data.get(i).get(colIndexes[j]);
			}
		}
		return result;
	}

	/**
	 * Gets rows item
	 * 
	 * @param double matrix
	 * @param item
	 * @param column index
	 * @return matrix with rows item
	 */
	public static double[][] selectRowsItem(double[][] array, double item, int column) {
		int numberOfColumns = array[0].length;
		int numberOfRecords = array.length;
		int numberOfSelected = 0;
		ArrayList<ArrayList<Double>> arrayResult = new ArrayList<>();
		for (int i = 0; i < numberOfRecords; i++) {
			if (array[i][column] == item) {
				arrayResult.add(new ArrayList<Double>());
				for (int j = 0; j < numberOfColumns; j++) {
					arrayResult.get(numberOfSelected).add(array[i][j]);
				}
				numberOfSelected++;
			}
		}
		return ArrayOperations.arrayListToDoubleMatrix(arrayResult);
	}

	/**
	 * Shifts matrix values 
	 * 
	 * @param double matrix
	 * @param index column
	 * @param shift value
	 * @return matrix shifted
	 */
	public static double[][] shiftValues(double[][] array, int indexColumn, int shiftValue) {
		double[] valuesColumn = ArrayOperations.getColumn(array, indexColumn);
		double[] shiftedValues = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			shiftedValues[i] = valuesColumn[i] - shiftValue;
		}
		int[] indexShifted = ArrayOperations.getIndexes(valuesColumn, shiftedValues);
		double[][] result = new double[array.length][array[0].length];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				if (indexShifted[i] == -1)
					result[i][j] = Double.NaN;
				else
					result[i][j] = array[indexShifted[i]][j];
			}
		}
		return result;
	}

	/**
	 * Shifts column (vector) values 
	 * 
	 * @param double matrix
	 * @param index column
	 * @param shift value
	 * @param return column
	 * @return vector shifted
	 */
	public static double[] shiftColumn(double[][] array, int indexColumn, int shiftValue, int returnColumn) {
		double[] valuesIndexColumn = ArrayOperations.getColumn(array, indexColumn);
		double[] valuesReturnColumn = ArrayOperations.getColumn(array, returnColumn);
		double[] shiftedValuesIndexColumn = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			shiftedValuesIndexColumn[i] = valuesIndexColumn[i] + shiftValue;
		}
		int[] indexShifted = ArrayOperations.getIndexes(valuesIndexColumn, shiftedValuesIndexColumn);
		double[] result = new double[array.length];
		for (int i = 0; i < result.length; i++) {
			if (indexShifted[i] == -1)
				result[i] = Double.NaN;
			else
				result[i] = valuesReturnColumn[indexShifted[i]];
		}
		return result;
	}

	/**
	 * Gets indexes of an array
	 * 
	 * @param double array
	 * @param double array with values to get indexes 
	 * @return integer vector with indexes
	 */
	public static int[] getIndexes(double[] array, double[] values) {
		int[] result = new int[values.length];
		for (int i = 0; i < result.length; i++)
			result[i] = -1;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if ((array[j] == values[i]) && (!ArrayOperations.isInIntArray(result, j))) {
					result[i] = j;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Checks if value is contained into integer array
	 * 
	 * @param int array
	 * @param target value 
	 * @return true if contains, false if not
	 */
	public static boolean isInIntArray(int[] array, int value) {
		for (int i = 0; i < array.length; i++)
			if (value == array[i])
				return true;
		return false;
	}

	/**
	 * Performs an array quick sort
	 * 
	 * @param double array
	 * @return double array quick sorted
	 */
	public static double[] quickSort(double[] array) {
		if (array.length > 1) {
			int pivotpos = array.length / 2;
			double[] pivot = { array[pivotpos] };
			ArrayList<Double> greater = new ArrayList<>();
			ArrayList<Double> equal = new ArrayList<>();
			ArrayList<Double> less = new ArrayList<>();
			equal.add(pivot[0]);
			for (int i = 0; i < array.length; i++) {
				if (i != pivotpos)
					if (array[i] > pivot[0])
						greater.add(array[i]);
					else if (array[i] < pivot[0])
						less.add(array[i]);
					else
						equal.add(array[i]);
			}
			double[] lessThanPivot = ArrayOperations.quickSort(ArrayOperations.arrayListToVector(less));
			double[] equalPivot = ArrayOperations.arrayListToVector(equal);
			double[] greaterThanPivot = ArrayOperations.quickSort(ArrayOperations.arrayListToVector(greater));
			double[] lessPivot = ArrayOperations.mergeArrays(lessThanPivot, equalPivot);
			return ArrayOperations.mergeArrays(lessPivot, greaterThanPivot);
		} else {
			return array;
		}
	}

	/**
	 * Converts a double ArrayList to a double vector
	 * 
	 * @param ArrayList to be converted
	 * @return vector converted to double[]
	 */
	public static double[] arrayListToVector(ArrayList<Double> al) {
		double[] result = new double[al.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = al.get(i);
		return result;
	}

	/**
	 * Converts a integer ArrayList to a double vector
	 * 
	 * @param ArrayList to be converted
	 * @return vector converted to double[]
	 */
	public static int[] arrayListToIntVector(ArrayList<Integer> al) {
		int[] result = new int[al.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = al.get(i);
		}
		return result;
	}

	/**
	 * Merges two vectors in one
	 * 
	 * @param vector 1 to be merged
	 * @param vector 2 to be merged
	 * @return vector merged
	 */
	public static double[] mergeArrays(double[] a, double[] b) {
		double[] result = new double[a.length + b.length];
		for (int i = 0; i < a.length; i++)
			result[i] = a[i];
		for (int i = 0; i < b.length; i++)
			result[a.length + i] = b[i];
		return result;
	}

	/**
	 * Performs wise product
	 * 
	 * @param vector 1
	 * @param vector 2
	 * @return vector resulting
	 */
	public static double[] elementWiseProduct(double[] arrx, double[] arry) {
		int longest = Math.max(arrx.length, arry.length);
		double[] result = new double[longest];
		for (int i = 0; i < result.length; i++) {
			double vx, vy;
			try {
				vx = arrx[i];
			} catch (IndexOutOfBoundsException ioobe) {
				vx = 0.0;
			}
			try {
				vy = arry[i];
			} catch (IndexOutOfBoundsException ioobe) {
				vy = 0.0;
			}
			result[i] = vx * vy;
		}
		return result;
	}
        
        public static double[][] convertToRowMatrix(double[] data){
            return ArrayOperations.convertToMatrix(data, true);
        }
        
        public static double[][] convertToColumnMatrix(double[] data){
            return ArrayOperations.convertToMatrix(data, false);
        }
        
        public static double[][] convertToMatrix(double[] data,boolean isRow){
            double[][] result;
            if (isRow)
                result = new double[1][data.length];
            else
                result = new double[data.length][1];
            for(int i=0;i<data.length;i++){
                if(isRow)
                    result[0][i]=data[i];
                else
                    result[i][0]=data[i];
            }
            return result;
        }

}
