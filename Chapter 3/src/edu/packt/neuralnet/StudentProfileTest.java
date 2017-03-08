/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet;

import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.ELM;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.learn.LevenbergMarquardt;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
 *
 * @author fab
 */
public class StudentProfileTest {
    public static void main(String[] args){
        RandomNumberGenerator.seed=0;
        
        Double[][] _neuralDataSet = {
            {1.0,   0.73,   1.0,    -1.0}
        ,   {1.0,   0.81,   1.0,    -1.0}
        ,   {1.0,   0.86,   1.0,    -1.0}
        ,   {0.0,   0.65,   1.0,    -1.0}
        ,   {0.0,   0.45,   1.0,    -1.0}
        ,   {1.0,   0.70,   -1.0,    1.0}
        ,   {0.0,   0.51,   -1.0,    1.0}
        ,   {1.0,   0.89,   -1.0,    1.0}
        ,   {1.0,   0.79,   -1.0,    1.0}
        ,   {0.0,   0.54,   -1.0,    1.0}

        };
        
        int[] inputColumns = {0,1};
        int[] outputColumns = {2,3};
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet,inputColumns,outputColumns);
        
        int numberOfInputs = 2;
        int numberOfOutputs = 2;
        
        int[] numberOfHiddenNeurons={5};
        
        Linear outputAcFnc = new Linear(1.0);
        Sigmoid hdAcFnc = new Sigmoid(1.0);
        IActivationFunction[] hiddenAcFnc={hdAcFnc  };
        
        NeuralNet nnlm = new NeuralNet(numberOfInputs,numberOfOutputs
                ,numberOfHiddenNeurons,hiddenAcFnc,outputAcFnc);
        
        NeuralNet nnelm = new NeuralNet(numberOfInputs,numberOfOutputs
                ,numberOfHiddenNeurons,hiddenAcFnc,outputAcFnc);
        
        
        LevenbergMarquardt lma = new LevenbergMarquardt(nnlm,neuralDataSet,LearningAlgorithm.LearningMode.BATCH);
        lma.setDamping(0.001);
        lma.setMaxEpochs(100);
        lma.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        lma.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        lma.setMinOverallError(0.0001);
        lma.printTraining=true;
        
        ELM elm = new ELM(nnelm,neuralDataSet);
        elm.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        elm.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        elm.setMinOverallError(0.0001);
        elm.printTraining=true;
        
        
        
        try{
            lma.forward();
            neuralDataSet.printNeuralOutput();
            
            lma.train();
            System.out.println("End of training");
            if(lma.getMinOverallError()>=lma.getOverallGeneralError()){
                System.out.println("Training successful!");
            }
            else{
                System.out.println("Training was unsuccessful");
            }
            System.out.println("Overall Error:"
                        +String.valueOf(lma.getOverallGeneralError()));
            System.out.println("Min Overall Error:"
                        +String.valueOf(lma.getMinOverallError()));
            System.out.println("Epochs of training:"
                        +String.valueOf(lma.getEpoch()));
            
            System.out.println("Target Outputs:");
            neuralDataSet.printTargetOutput();
            
            System.out.println("Neural Output after training:");
            lma.forward();
            neuralDataSet.printNeuralOutput();
            
            elm.forward();
            neuralDataSet.printNeuralOutput();
            
            elm.train();
            System.out.println("End of training");
            if(elm.getMinOverallError()>=elm.getOverallGeneralError()){
                System.out.println("Training successful!");
            }
            else{
                System.out.println("Training was unsuccessful");
            }
            System.out.println("Overall Error:"
                        +String.valueOf(elm.getOverallGeneralError()));
            System.out.println("Min Overall Error:"
                        +String.valueOf(elm.getMinOverallError()));
            
            System.out.println("Target Outputs:");
            neuralDataSet.printTargetOutput();
            
            System.out.println("Neural Output after training:");
            elm.forward();
            neuralDataSet.printNeuralOutput();
            

        }
        catch(NeuralException ne){
            
        }
        
    }
}
