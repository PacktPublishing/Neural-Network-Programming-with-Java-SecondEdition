package edu.packt.neuralnet.test;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.learn.Hebbian;
import edu.packt.neuralnet.learn.LearningAlgorithm;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
*
* NeuralNetHebbianTest
* This class solely performs test of Neural Net using Hebbian learning algorithm 
* 
* @authors Alan de Souza, Fábio Soares 
* @version 0.1
* 
*/
public class NeuralNetHebbianTest {
    public static void main(String[] args){
        
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=2;
        int numberOfOutputs=1;
        
        Sigmoid outputAcFnc = new Sigmoid(1.0);
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet(numberOfInputs,numberOfOutputs,
                outputAcFnc);
        nn.deactivateBias();
        System.out.println("Neural Network created!");
        nn.print();
        
        double[][] _neuralDataSet = new double[10][2];
        
        for (double[] _neuralDataSet1 : _neuralDataSet) {
            for (int j = 0; j < _neuralDataSet1.length; j++) {
                _neuralDataSet1[j] = RandomNumberGenerator.GenerateNext();
            }
        }
        
        NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet,1);
        
        System.out.println("Dataset created");
        neuralDataSet.printInput();
        
        System.out.println("Getting the first output of the neural network");
        
        Hebbian hebbian=new Hebbian(nn,neuralDataSet
                ,LearningAlgorithm.LearningMode.ONLINE);
        
        hebbian.printTraining=true;
        hebbian.setLearningRate(0.3);
        hebbian.setMaxEpochs(1000);
        
        try{
            hebbian.forward();
            neuralDataSet.printNeuralOutput();
        
            System.out.println("Beginning training");
            
            hebbian.train();
            
            System.out.println("End of training");
            System.out.println("Epochs of training:"
                        +String.valueOf(hebbian.getEpoch()));
            
            System.out.println("Neural Output after training:");
            hebbian.forward();
            neuralDataSet.printNeuralOutput();
            
        }
        catch(NeuralException ne){
            
        }
        
        
    }    
    
}
