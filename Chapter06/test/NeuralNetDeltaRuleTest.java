
package edu.packt.neuralnet.test;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.learn.DeltaRule;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.HyperTan;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;

/**
*
* NeuralNetDeltaRuleTest
* This class solely performs test of Neural Net using Delta Rule 
* 
* @authors Alan de Souza, Fabio Soares 
* @version 0.1
* 
*/
public class NeuralNetDeltaRuleTest {
    
    public static void main(String[] args){
        
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=1;
        int numberOfOutputs=1;
        
        Linear outputAcFnc = new Linear(1.0);
        HyperTan htAcFnc = new HyperTan(0.85);
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet(numberOfInputs,numberOfOutputs,
                //outputAcFnc);
                htAcFnc);
        System.out.println("Neural Network created!");
        nn.print();
        
        double[][] _neuralDataSet = {
            {1.2 , fncTest(1.2)}
        ,   {0.3 , fncTest(0.3)}
        ,   {-0.5 , fncTest(-0.5)}
        ,   {-2.3 , fncTest(-2.3)}
        ,   {1.7 , fncTest(1.7)}
        ,   {-0.1 , fncTest(-0.1)}
        ,   {-2.7 , fncTest(-2.7)}
        };
        
        int[] inputColumns = {0};
        int[] outputColumns = {1};
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet,inputColumns,outputColumns);
        
        System.out.println("Dataset created");
        neuralDataSet.printInput();
        neuralDataSet.printTargetOutput();
        
        System.out.println("Getting the first output of the neural network");
        
        DeltaRule deltaRule=new DeltaRule(nn,neuralDataSet
                ,LearningAlgorithm.LearningMode.ONLINE);
        
        deltaRule.printTraining=true;
        deltaRule.setLearningRate(0.3);
        deltaRule.setMaxEpochs(1000);
        deltaRule.setGeneralErrorMeasurement(DeltaRule.ErrorMeasurement.SimpleError);
        deltaRule.setOverallErrorMeasurement(DeltaRule.ErrorMeasurement.MSE);
        deltaRule.setMinOverallError(0.00001);
        
        try{
            deltaRule.forward();
            neuralDataSet.printNeuralOutput();
            
            Double weight = nn.getOutputLayer().getWeight(0, 0);
            Double bias = nn.getOutputLayer().getWeight(1, 0);
            
            System.out.println("Initial weight:"+String.valueOf(weight));
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
            
            weight = nn.getOutputLayer().getWeight(0, 0);
            bias = nn.getOutputLayer().getWeight(1, 0);
            
            System.out.println("Weight found:"+String.valueOf(weight));
            System.out.println("Bias found:"+String.valueOf(bias));
            
            double[][] _testDataSet ={
                {-1.7 , fncTest(-1.7) }
              , {-1.0 , fncTest(-1.0) }
              , {0.0 , fncTest(0.0) }
              , {0.8 , fncTest(0.8) }
              , {2.0 , fncTest(2.0) }
            };
            
            NeuralDataSet testDataSet = new NeuralDataSet(_testDataSet, inputColumns, outputColumns);
            
            deltaRule.setTestingDataSet(testDataSet);
            deltaRule.test();
            testDataSet.printNeuralOutput();
        }
        catch(NeuralException ne){
            
        }
        
        
    }
    
    public static double fncTest(double x){
        return 0.11*x;
    }
    
}
