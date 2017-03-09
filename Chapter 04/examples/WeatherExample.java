package edu.packt.neuralnet.examples;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.ChartFrame;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.DataNormalization;
import edu.packt.neuralnet.data.DataNormalization.NormalizationTypes;
import edu.packt.neuralnet.data.LoadCsv;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.ArrayOperations;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

public class WeatherExample {

    public static void main(String[] args) {
		
        //load weather data:
        LoadCsv csv = new LoadCsv();
        double[][] rawWeatherData = csv.getData("data", "inmet_13_14_input_new.csv");
		
        //normalize data:
        DataNormalization norm = new DataNormalization();
        norm.TYPE = NormalizationTypes.MIN_MAX;
        double[][] normalizedWeatherData = norm.normalize(rawWeatherData);
		
        //set seed:
        RandomNumberGenerator.seed = 7;
		
        //setup neural net parameters:
        int numberOfInputs  = 4;
        int numberOfOutputs = 1;
        int[] numberOfHiddenNeurons = { 2 };
        
        Linear outputAcFnc = new Linear();
        Sigmoid hl0Fnc = new Sigmoid(1.0);
        Sigmoid hl1Fnc = new Sigmoid(1.0);
        
        IActivationFunction[] hiddenAcFnc = { hl0Fnc };
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet( numberOfInputs, numberOfOutputs, numberOfHiddenNeurons, 
                hiddenAcFnc, outputAcFnc, new UniformInitialization(-1.0, 1.0) );
        System.out.println("Neural Network created!");
        //nn.print();
        
        int[] inputColumns  = { 0, 1, 2, 3 };
        int[] outputColumns = { 4 };
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(normalizedWeatherData, inputColumns, outputColumns);
        
        System.out.println("Dataset created");
        //neuralDataSet.printInput();
        //neuralDataSet.printTargetOutput();
        
        System.out.println("Getting the first output of the neural network");
        
        //setup backpropagation learning algorithm parameters
        Backpropagation backprop = new Backpropagation(nn,neuralDataSet,LearningAlgorithm.LearningMode.ONLINE);
        backprop.setLearningRate(0.1);
        backprop.setMaxEpochs(1000);
        backprop.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        backprop.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        backprop.setMinOverallError(0.001);
        backprop.printTraining = true;
        backprop.setMomentumRate( 0.5 );
        
        try{
            backprop.forward();
            //neuralDataSet.printNeuralOutput();
            
            backprop.train();
            System.out.println("End of training");
            if(backprop.getMinOverallError()>=backprop.getOverallGeneralError()){
                System.out.println("Training successful!");
            }
            else{
                System.out.println("Training was unsuccessful");
            }
            
            //print some results
            System.out.println("Overall Error:"      + String.valueOf(backprop.getOverallGeneralError()));
            System.out.println("Min Overall Error:"  + String.valueOf(backprop.getMinOverallError()));
            System.out.println("Epochs of training:" + String.valueOf(backprop.getEpoch()));
            
            //System.out.println("Target Outputs:");
            //neuralDataSet.printTargetOutput();
            
            System.out.println("Neural Output after training:");
            backprop.forward();
            //neuralDataSet.printNeuralOutput();
            
            double[] neuralOutputNorm = ArrayOperations.getColumn(neuralDataSet.getRealVsEstimated(0), 0);
            double[] targetOutputNorm = ArrayOperations.getColumn(neuralDataSet.getRealVsEstimated(0), 1);
            
            double[] rawTargetOutput = ArrayOperations.getColumn(rawWeatherData, outputColumns[0]); 
            
            double[] neuralOutputDenorm = norm.denormalize(rawTargetOutput, neuralOutputNorm);
            double[] targetOutputDenorm = norm.denormalize(rawTargetOutput, targetOutputNorm);
            
            //plot list of errors by epoch 
            Chart c = new Chart();
            c.plot(backprop.getListOfErrorsByEpoch(), "MSE", "Epoch", "Error");
            
            double[][] matrixRealVSEstimatedDenorm = {targetOutputDenorm, neuralOutputDenorm};
            double[][] matrixRealVSEstimatedDenormT = ArrayOperations.transposeMatrix(matrixRealVSEstimatedDenorm);
            
            //scatterplot real x estimated
            String[] seriesNamesScatter = {"Samples"};
            Paint[]  seriesColorScatter = {Color.BLUE};
            Chart scatter = new Chart("Forecasting Weather Scatterplot", matrixRealVSEstimatedDenormT, seriesNamesScatter, 0, seriesColorScatter, Chart.SeriesType.DOTS);
            ChartFrame frame = new ChartFrame( "Neural Net Chart", scatter.scatterPlot("Real", "Estimated") );
            frame.pack();
            frame.setVisible(true);
            
        }
        catch(NeuralException ne){
            
        }
		
    }
	
}
