/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.examples.weather;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.ChartFrame;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.chart.Chart;
import edu.packt.neuralnet.data.DataNormalization;
import edu.packt.neuralnet.data.LoadCsv;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.data.TimeSeries;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.ArrayOperations;
import edu.packt.neuralnet.math.HyperTan;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.Sigmoid;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fab
 */
public class WeatherExampleDelays {
    
    public static void main(String[] args){
        //makeDelays();
        //dropNaNs();
        TimeSeries dataPOA = new TimeSeries(LoadCsv.getDataSet("data", "portoalegre2010daily_delays_clean.txt", true, ";"));
        dataPOA.setIndexColumn(0);
        int[] plotcolumns = {2,3,6};
        Paint[] color = {Color.BLUE,Color.BLACK,Color.RED};
        ChartFrame chfr = dataPOA.getTimePlot("TimePlot", plotcolumns, color, 41100.0, 41200.0);
        chfr.setVisible(true);

        int[] inputColumns = {9,10,11,12,13,14,15,16};
        int[] outputColumns = {2,3,6};
        
        NeuralDataSet[] nntt = NeuralDataSet.randomSeparateTrainTest(dataPOA, inputColumns, outputColumns, 0.7);
        
        DataNormalization.setNormalization(nntt, -1.0, 1.0);

        NeuralDataSet trainingData = nntt[0];
        NeuralDataSet testingData = nntt[1];
        
        //setup neural net parameters:
        int numberOfInputs  = 8;
        int numberOfOutputs = 3;
        int[] numberOfHiddenNeurons = { 20,10 };
        
        Linear outputAcFnc = new Linear();
        HyperTan hl0Fnc = new HyperTan(1.0);
        Sigmoid hl1Fnc = new Sigmoid(1.0);
        
        IActivationFunction[] hiddenAcFnc = { hl0Fnc, hl1Fnc };
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet( numberOfInputs, numberOfOutputs, numberOfHiddenNeurons, 
                hiddenAcFnc, outputAcFnc, new UniformInitialization(-1.0, 1.0) );
        System.out.println("Neural Network created!");
        //nn.print();
      
        //setup backpropagation learning algorithm parameters
        Backpropagation backprop = new Backpropagation(nn,trainingData,LearningAlgorithm.LearningMode.BATCH);
        backprop.setLearningRate(0.4);
        backprop.setMaxEpochs(100);
        backprop.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        backprop.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        backprop.setMinOverallError(0.01);
        backprop.printTraining = true;
        backprop.setMomentumRate( 0.2 );
        backprop.setTestingDataSet(testingData);
        
        
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
            
            //plot list of errors by epoch 
            Chart c = new Chart();
            c.plot(backprop.getListOfErrorsByEpoch(), "MSE", "Epoch", "Error");
            
            int[] allColumns = {9,10,11,12,13,14,15,16,2,3,6};
            double[][] selectedDataSet = ArrayOperations.getMultipleColumns(dataPOA.getData(), allColumns);
            int[] allDsInput = {0,1,2,3,4,5,6,7};
            int[] allDsOutput = {8,9,10};
            NeuralDataSet allDataSet = new NeuralDataSet(selectedDataSet,allDsInput,allDsOutput);
            allDataSet.setInputNorm(trainingData.inputNorm);
            allDataSet.setOutputNorm(trainingData.outputNorm);
            allDataSet.neuralNet=nn;
            allDataSet.simulate();
            
            
        }
        catch(NeuralException ne){
            
        }
        
//        LevenbergMarquardt lma = new LevenbergMarquardt(nn,trainingData,LearningAlgorithm.LearningMode.BATCH);
//        lma.setDamping(0.001);
//        lma.setMaxEpochs(100);
//        lma.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
//        lma.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
//        lma.setMinOverallError(0.01);
//        lma.printTraining=true;
//        
//        try{
//            lma.forward();
//            trainingData.printNeuralOutput();
//            
//            lma.train();
//            System.out.println("End of training");
//            if(lma.getMinOverallError()>=lma.getOverallGeneralError()){
//                System.out.println("Training successful!");
//            }
//            else{
//                System.out.println("Training was unsuccessful");
//            }
//            System.out.println("Overall Error:"
//                        +String.valueOf(lma.getOverallGeneralError()));
//            System.out.println("Min Overall Error:"
//                        +String.valueOf(lma.getMinOverallError()));
//            System.out.println("Epochs of training:"
//                        +String.valueOf(lma.getEpoch()));
//            
//            //System.out.println("Target Outputs:");
//            //trainingData.printTargetOutput();
//            
//            //System.out.println("Neural Output after training:");
//            //lma.forward();
//            //trainingData.printNeuralOutput();
//            
//
//        }
//        catch(NeuralException ne){
//            
//        }
        
        
        
    }
    
    public static void makeDelays(){
        TimeSeries weatherData = new TimeSeries(LoadCsv.getDataSet("data","belem1980diario.txt", true, ";"));
        weatherData.setIndexColumn(0);
        weatherData.shift(1, -2);
        weatherData.save("data","belem1980daily_delays.txt",";");
        
        TimeSeries dataPOA = new TimeSeries(LoadCsv.getDataSet("data","portoalegre1980diario.txt", true, ";"));
        dataPOA.setIndexColumn(0);
        dataPOA.shift(1,-1);
        dataPOA.shift(2,-1);
        dataPOA.shift(3,-1);
        dataPOA.shift(4,-1);
        dataPOA.shift(1,-2);
        dataPOA.shift(2,-2);
        dataPOA.shift(3,-2);
        dataPOA.shift(4,-2);
        dataPOA.save("data", "portoalegre1980daily_delays.txt", ";");        
    }
    
    public static void dropNaNs(){
        TimeSeries dataPOA = new TimeSeries(LoadCsv.getDataSet("data", "portoalegre1980daily_delays.txt", true, ";"));
        dataPOA.dropNaN();
        dataPOA.save("data","portoalegre1980daily_delays_clean.txt",";");
    }
}
