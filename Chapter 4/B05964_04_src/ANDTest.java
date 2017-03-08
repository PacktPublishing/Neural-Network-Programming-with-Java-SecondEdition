/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet;

import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.learn.DeltaRule;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;

/**
 *
 * @author fab
 */
public class ANDTest {
    public static void main(String[] args){
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=2;
        int numberOfOutputs=1;
        
        Linear outputAcFnc = new Linear(1.0);
        NeuralNet perceptron = new NeuralNet(numberOfInputs,numberOfOutputs,outputAcFnc);
        
        double[][] _neuralDataSet = {
            {0.0 , 0.0 , 0.0 }
        ,   {0.0 , 1.0 , 0.0 }
        ,   {1.0 , 0.0 , 0.0 }
        ,   {1.0 , 1.0 , 1.0 }
        };
        
        int[] inputColumns = {0,1};
        int[] outputColumns = {2};
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet,inputColumns,outputColumns);
        
        DeltaRule deltaRule=new DeltaRule(perceptron,neuralDataSet
                ,LearningAlgorithm.LearningMode.ONLINE);
        
        deltaRule.printTraining=true;
        deltaRule.setLearningRate(0.01);
        deltaRule.setMaxEpochs(4000);
        deltaRule.setGeneralErrorMeasurement(DeltaRule.ErrorMeasurement.SimpleError);
        deltaRule.setOverallErrorMeasurement(DeltaRule.ErrorMeasurement.MSE);
        deltaRule.setMinOverallError(0.01);
        
        try{
            deltaRule.forward();
            neuralDataSet.printNeuralOutput();
            
            double w0 = perceptron.getOutputLayer().getWeight(0, 0);
            double w1 = perceptron.getOutputLayer().getWeight(1, 0);
            Double bias = perceptron.getOutputLayer().getWeight(2, 0);
            
            System.out.println("Initial weight0:"+String.valueOf(w0));
            System.out.println("Initial weight1:"+String.valueOf(w1));
            System.out.println("Initial bias:"+String.valueOf(bias));
        
            System.out.println("Beginning training");
            
            deltaRule.train();
            
            System.out.println("End of training");
            if(deltaRule.getMinOverallError()>=deltaRule.getOverallGeneralError()){
                System.out.println("Training succesful!");
            }
            else{
                System.out.println("Training was unsuccesful");
            }
            System.out.println("Overall Error:"
                        +String.valueOf(deltaRule.getOverallGeneralError()));
            System.out.println("Min Overall Error:"
                        +String.valueOf(deltaRule.getMinOverallError()));
            System.out.println("Epochs of training:"
                        +String.valueOf(deltaRule.getEpoch()));
            
            System.out.println("Target Outputs:");
            neuralDataSet.printTargetOutput();
            
            System.out.println("Neural Output after training:");
            deltaRule.forward();
            neuralDataSet.printNeuralOutput();
            
            w0 = perceptron.getOutputLayer().getWeight(0, 0);
            w1 = perceptron.getOutputLayer().getWeight(1, 0);
            bias = perceptron.getOutputLayer().getWeight(2, 0);
            
            System.out.println("Weight0 found:"+String.valueOf(w0));
            System.out.println("Weight1 found:"+String.valueOf(w1));
            System.out.println("Bias found:"+String.valueOf(bias));
            
        }
        catch(NeuralException ne){
            
        }
        
    }
}
