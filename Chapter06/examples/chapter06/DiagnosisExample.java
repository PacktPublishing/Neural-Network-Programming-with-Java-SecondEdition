package edu.packt.neuralnet.examples.chapter06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.DataNormalization;
import edu.packt.neuralnet.data.DataSet;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.ArrayOperations;
import edu.packt.neuralnet.math.HyperTan;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
 *
 * DiagnosisExample This class performs use cases described in
 * chapter 6 of the book: breast cancer and diabetes data. Data are loaded, data
 * normalization is done, neural net is created using parameters defined,
 * Backpropagation algorithm is used to make neural net learn and charts are
 * plotted
 * 
 * @authors Alan de Souza, Fabio Soares
 * @version 0.1
 * 
 */
public class DiagnosisExample {

	public static void main(String[] args) {

		RandomNumberGenerator.setSeed(7);
		
		String CHOSEN_OPTION = "";
		int numberOfInputs  = 0;
		int numberOfOutputs = 0;
		int[] inputColumns  = null;
		int[] outputColumns = null;
		
		DataSet dataSet = new DataSet();
		
		Scanner sc = new Scanner(System.in);
		
		int experiment = 0;
		
		while(true) {
			boolean flagNeural = true;
			
			experiment++;
			System.out.println("*** EXPERIMENT #"+experiment+" ***");
			
			System.out.println("Number of epochs:");
			int typedEpochs = sc.nextInt();
			
			System.out.println("Number of neurons in hidden layer:");
			int typedNumHdnLayer = sc.nextInt();
			
			System.out.println("Learning rate:");
			double typedLearningRate = sc.nextDouble();
			
			System.out.println("Which dataset would you like to use?");
			System.out.println("1) Breast cancer");
			System.out.println("2) Diabetes");
			System.out.println("99) Quit");
			int option = sc.nextInt();
			
			switch (option) {
				case 1:
					CHOSEN_OPTION   = "Breast cancer";
					numberOfInputs  = 9;
					numberOfOutputs = 2;
					
					// load data
					dataSet = new DataSet("data", "breast_cancer_fulldata.txt");
					inputColumns  = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
					outputColumns = new int[] { 9, 10 };
					
					break;
				case 2:
					CHOSEN_OPTION   = "Diabetes";
					numberOfInputs  = 8;
					numberOfOutputs = 2;
					
					// load data
					dataSet = new DataSet("data", "diabetes_fulldata.txt");
					inputColumns  = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
					outputColumns = new int[] { 8, 9 };
					
					break;
				case 99:
					System.out.println("Successful exit!");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid option...");
					flagNeural = false;
			}
			//sc.close();
			
			if(flagNeural) {
			
				int[] numberOfHiddenNeurons = { typedNumHdnLayer };
			
				//HyperTan hl0Fnc = new HyperTan(1.0);
				Sigmoid hl0Fnc = new Sigmoid(1.0);
				Linear outputAcFnc = new Linear(1.0);
			
				IActivationFunction[] hiddenAcFnc = { hl0Fnc };
			
				System.out.println("Creating Neural Network...");
				NeuralNet nn = new NeuralNet(numberOfInputs, numberOfOutputs, numberOfHiddenNeurons, hiddenAcFnc, outputAcFnc,
						new UniformInitialization(-1.0, 1.0));
				System.out.println("Neural Network created!");
				//nn.print();
				
				// load data
				double[][] _neuralDataSet = dataSet.getData();
			
				// normalize data
				DataNormalization dn = new DataNormalization(-1.0, 1.0);
				double[][] dataNormalized = new double[_neuralDataSet.length][_neuralDataSet[0].length];
				dataNormalized = dn.normalize(_neuralDataSet);
				
				double[][] dataNormalizedToTrain = Arrays.copyOfRange(dataNormalized, 0, (int) Math.ceil(dataNormalized.length*(0.8)));
				double[][] dataNormalizedToTest  = Arrays.copyOfRange(dataNormalized, (int) Math.ceil(dataNormalized.length*(0.8))+1, dataNormalized.length);
				
				// normalized data to train ANN:
				NeuralDataSet neuralDataSetToTrain = new NeuralDataSet(dataNormalizedToTrain, inputColumns, outputColumns);
				
				// normalized data to test ANN:
				NeuralDataSet neuralDataSetToTest = new NeuralDataSet(dataNormalizedToTest, inputColumns, outputColumns);
			
				//System.out.println("Dataset to train created");
				//neuralDataSetToTrain.printInput();
				//neuralDataSetToTrain.printTargetOutput();
			
				System.out.println("Getting the first output of the neural network");
			
				// create ANN and define parameters to TRAIN: 
				Backpropagation backprop = new Backpropagation(nn, neuralDataSetToTrain, LearningAlgorithm.LearningMode.BATCH);
				backprop.setLearningRate( typedLearningRate );
				backprop.setMaxEpochs( typedEpochs );
				backprop.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
				backprop.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
				backprop.setMinOverallError(0.001);
				backprop.setMomentumRate(0.7);
				backprop.setTestingDataSet(neuralDataSetToTest);
				backprop.printTraining = true;
				backprop.showPlotError=true;
				
				// train ANN:
				try {
					backprop.forward();
					//neuralDataSetToTrain.printNeuralOutput();
			
					backprop.train();
					System.out.println("End of training");
					if (backprop.getMinOverallError() >= backprop.getOverallGeneralError()) {
						System.out.println("Training successful!");
					} else {
						System.out.println("Training was unsuccessful");
					}
					System.out.println("Overall Error:" + String.valueOf(backprop.getOverallGeneralError()));
					System.out.println("Min Overall Error:" + String.valueOf(backprop.getMinOverallError()));
					System.out.println("Epochs of training:" + String.valueOf(backprop.getEpoch()));
			
					//System.out.println("Target Outputs (TRAIN):");
					//neuralDataSetToTrain.printTargetOutput();
			
					//System.out.println("Neural Output after training:");
					//backprop.forward();
					//neuralDataSetToTrain.printNeuralOutput();
				} catch (NeuralException ne) {
					ne.printStackTrace();
				}
			
				//System.out.println("Dataset to test created");
				//neuralDataSetToTest.printInput();
				//neuralDataSetToTest.printTargetOutput();
				
				try {
					
					backprop.test();// forward();
					
					//neuralDataSetToTest.printNeuralOutput();
					
					//System.out.println("Target Outputs (TEST):");
					//neuralDataSetToTest.printTargetOutput();
					
					ArrayList<ArrayList<Double>> listOutputValues = neuralDataSetToTest.getArrayNeuralOutputData();
					
					// denormalize test data 
					double[][] dataNormalizedOutputTest = extractMatrixByArrayList(outputColumns, dataNormalizedToTest, listOutputValues);
					
					double[] dataDenormalizedOutputTest1 = dn.denormalize( ArrayOperations.getColumn(dataNormalizedOutputTest, 0), outputColumns[0] );
					double[] dataDenormalizedOutputTest2 = dn.denormalize( ArrayOperations.getColumn(dataNormalizedOutputTest, 1), outputColumns[1] );
					
					// adapt data (test):
					double[][] dataOutputTestAdapted = adaptData(outputColumns, dataNormalizedToTest, dataDenormalizedOutputTest1,
							dataDenormalizedOutputTest2);
					
					// target output:
					ArrayList<ArrayList<Double>> listTargetValues = neuralDataSetToTest.getArrayTargetOutputData();
					
					double[][] dataTargetOutputTest = extractMatrixByArrayList(outputColumns, dataNormalizedToTest, listTargetValues);
					
					double[] dataDenormalizedTargetOutputTest1 = dn.denormalize( ArrayOperations.getColumn(dataTargetOutputTest, 0), outputColumns[0] );
					double[] dataDenormalizedTargetOutputTest2 = dn.denormalize( ArrayOperations.getColumn(dataTargetOutputTest, 1), outputColumns[1] );
					
					// adapt data (target):
					double[][] dataOutputTargetTestAdapted = adaptData(outputColumns, dataNormalizedToTest,
							dataDenormalizedTargetOutputTest1, dataDenormalizedTargetOutputTest2);
			
					// calculate confusion matrix:
					double[][] confusionMatrix = neuralDataSetToTest.outputData.calculateConfusionMatrix(dataOutputTestAdapted, dataOutputTargetTestAdapted);
					
					System.out.println("\n\n### Results for "+CHOSEN_OPTION+" ###");
					
					System.out.println("\n### Confusion Matrix ###");
					System.out.println( Arrays.deepToString(confusionMatrix).replaceAll("],", "]\n") );
					
					// calculate another performance measures
					neuralDataSetToTest.outputData.calculatePerformanceMeasures(confusionMatrix);
					
					System.out.println("\n\n\n");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		
			} //end if flagNeural
			
		} //end while loop 

	}

	private static double[][] extractMatrixByArrayList(int[] outputColumns, double[][] data, ArrayList<ArrayList<Double>> list) {
		double[][] matrix = new double[data.length][outputColumns.length];
		int i = 0, j = 0;
		for (ArrayList<Double> arrayList : list) {
			for (Double value : arrayList) {
				matrix[i][j] = value;
				j++;
			}
			i++;
			j=0;
		}
		return matrix;
	}

	private static double[][] adaptData(int[] outputColumns, double[][] data,
			double[] dataOutput1, double[] dataOutput2) {
		double[][] matrix = new double[data.length][outputColumns.length];
		for (int k = 0; k < dataOutput1.length; k++) {
			double v1 = dataOutput1[k];
			double v2 = dataOutput2[k];
			if(v1 <= 0.50) {
				matrix[k][0] = 0.0;
			}else {
				matrix[k][0] = 1.0;
			}
			if(v2 <= 0.50) {
				matrix[k][1] = 0.0;
			}else {
				matrix[k][1] = 1.0;
			}
		}
		return matrix;
	}

}
