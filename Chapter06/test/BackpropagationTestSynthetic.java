package edu.packt.neuralnet.test;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.DataSet;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
*
* BackpropagationTest
* This class solely performs Backpropagation learning algorithm test 
* 
* @authors Alan de Souza, Fabio Soares 
* @version 0.1
* 
*/
public class BackpropagationTestSynthetic {
    public static void main(String[] args){
        //NeuralNet nn = new NeuralNet
        RandomNumberGenerator.seed=850;
        
        int numberOfInputs=1;
        int numberOfOutputs=1;
        int[] numberOfHiddenNeurons={5};
        
        
        Linear outputAcFnc = new Linear(1.0);
        Sigmoid hl0Fnc = new Sigmoid(1.0);
        IActivationFunction[] hiddenAcFnc={hl0Fnc};
        
        System.out.println("Creating Neural Network...");
        
        NeuralNet nn = new NeuralNet(numberOfInputs, numberOfOutputs
                , numberOfHiddenNeurons, hiddenAcFnc, outputAcFnc
                ,new UniformInitialization(-1.0,1.0));
        
        System.out.println("Neural Network created!");
        nn.print();
        
		DataSet dataSet = new DataSet("data", "xpow2.csv");
		double[][] _neuralDataSet = dataSet.getData();
        
        int[] inputColumns = {1};
        int[] outputColumns = {2};
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet,inputColumns,outputColumns);
        
        System.out.println("Dataset created");
        neuralDataSet.printInput();
        neuralDataSet.printTargetOutput();
        
        System.out.println("Getting the first output of the neural network");
        
        Backpropagation backprop = new Backpropagation(nn,neuralDataSet,LearningAlgorithm.LearningMode.ONLINE);
        backprop.setLearningRate(0.1);
        backprop.setMaxEpochs(1000);
        backprop.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        backprop.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        backprop.setMinOverallError(0.002);
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
