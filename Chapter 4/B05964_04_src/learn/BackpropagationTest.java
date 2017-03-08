/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.learn;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
 *
 * @author fab
 */
public class BackpropagationTest {
    public static void main(String[] args){
        //NeuralNet nn = new NeuralNet
        RandomNumberGenerator.seed=850;
        
        int numberOfInputs=3;
        int numberOfOutputs=4;
        int[] numberOfHiddenNeurons={4,3,5};
        
        
        Linear outputAcFnc = new Linear(1.0);
        Sigmoid hl0Fnc = new Sigmoid(1.0);
        Sigmoid hl1Fnc = new Sigmoid(1.0);
        Sigmoid hl2Fnc = new Sigmoid(1.0);
        IActivationFunction[] hiddenAcFnc={hl0Fnc
                ,hl1Fnc
                ,hl2Fnc};
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet(numberOfInputs, numberOfOutputs
                , numberOfHiddenNeurons, hiddenAcFnc, outputAcFnc
                ,new UniformInitialization(-1.0,1.0));
        System.out.println("Neural Network created!");
        nn.print();
        
        double[][] _neuralDataSet = {
            {-1.0,-1.0,-1.0,-1.0,1.0,-3.0,1.0}
        ,   {-1.0,-1.0,1.0,1.0,-1.0,-1.0,-1.0}
        ,   {-1.0,1.0,-1.0,1.0,-1.0,-1.0,-1.0}
        ,   {-1.0,1.0,1.0,-1.0,-1.0,1.0,-3.0}
        ,   {1.0,-1.0,-1.0,1.0,-1.0,-1.0,3.0}                
        ,   {1.0,-1.0,1.0,-1.0,-1.0,1.0,1.0}
        ,   {1.0,1.0,-1.0,-1.0,1.0,1.0,1.0}
        ,   {1.0,1.0,1.0,1.0,-1.0,3.0,-1.0}
        };
        
        int[] inputColumns = {0,1,2};
        int[] outputColumns = {3,4,5,6};
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet,inputColumns,outputColumns);
        
        System.out.println("Dataset created");
        neuralDataSet.printInput();
        neuralDataSet.printTargetOutput();
        
        System.out.println("Getting the first output of the neural network");
        
        Backpropagation backprop = new Backpropagation(nn,neuralDataSet,LearningAlgorithm.LearningMode.BATCH);
        backprop.setLearningRate(0.2);
        backprop.setMaxEpochs(20000);
        backprop.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        backprop.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        backprop.setMinOverallError(0.0001);
        backprop.printTraining=true;
        backprop.setMomentumRate(0.7);
        
        try{
            backprop.forward();
            neuralDataSet.printNeuralOutput();
            
            backprop.train();
            System.out.println("End of training");
            if(backprop.getMinOverallError()>=backprop.getOverallGeneralError()){
                System.out.println("Training successful!");
            }
            else{
                System.out.println("Training was unsuccessful");
            }
            System.out.println("Overall Error:"
                        +String.valueOf(backprop.getOverallGeneralError()));
            System.out.println("Min Overall Error:"
                        +String.valueOf(backprop.getMinOverallError()));
            System.out.println("Epochs of training:"
                        +String.valueOf(backprop.getEpoch()));
            
            System.out.println("Target Outputs:");
            neuralDataSet.printTargetOutput();
            
            System.out.println("Neural Output after training:");
            backprop.forward();
            neuralDataSet.printNeuralOutput();
        }
        catch(NeuralException ne){
            
        }

    }
}
