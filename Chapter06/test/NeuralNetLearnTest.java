package edu.packt.neuralnet.test;

import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
*
* NeuralNetLearnTest
* This class solely performs test of Neural Net learning process 
* 
* @authors Alan de Souza, Fabio Soares 
* @version 0.1
* 
*/
public class NeuralNetLearnTest {
    
    public static void main(String[] args){
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=2;
        int numberOfOutputs=1;
        int[] numberOfHiddenNeurons={4,3,2};
        IActivationFunction[] hiddenAcFnc={new Sigmoid(10.0)
                ,new Sigmoid(1.05)
                ,new Sigmoid(2)};
        
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet(numberOfInputs, numberOfOutputs
                , numberOfHiddenNeurons, hiddenAcFnc, new Linear(1.0)
                ,new UniformInitialization(-1.0,1.0));
        System.out.println("Neural Network created!");
        nn.print();
        
        double[] dataInputRecord={-100.3, -1.15};
        nn.setInputs(dataInputRecord);
        nn.calc();
        double[] dataOutputRecord=nn.getOutputs();
        System.out.println("Output:"+String.valueOf(dataOutputRecord[0]));
        
        
    }
}
