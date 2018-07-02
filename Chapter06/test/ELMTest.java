
package edu.packt.neuralnet.test;

import edu.packt.neuralnet.NeuralException;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.data.NeuralDataSet;
import edu.packt.neuralnet.init.UniformInitialization;
import edu.packt.neuralnet.learn.Backpropagation;
import edu.packt.neuralnet.learn.ELM;
import edu.packt.neuralnet.math.IActivationFunction;
import edu.packt.neuralnet.math.Linear;
import edu.packt.neuralnet.math.RandomNumberGenerator;
import edu.packt.neuralnet.math.Sigmoid;

/**
*
* ELMTest
* This class solely performs ELM learning algorithm test 
* 
* @authors Alan de Souza, Fabio Soares 
* @version 0.1
* 
*/
public class ELMTest {
    public static void main(String[] args){
        //NeuralNet nn = new NeuralNet
        RandomNumberGenerator.seed=0;
        
        int numberOfInputs=3;
        int numberOfOutputs=4;
        int[] numberOfHiddenNeurons={4};
        
        
        Linear outputAcFnc = new Linear(1.0);
        Sigmoid hl0Fnc = new Sigmoid(1.0);
        IActivationFunction[] hiddenAcFnc={hl0Fnc
        };
        System.out.println("Creating Neural Network...");
        NeuralNet nn = new NeuralNet(numberOfInputs, numberOfOutputs
                , numberOfHiddenNeurons, hiddenAcFnc, outputAcFnc
                ,new UniformInitialization(-1.0,1.0));
        System.out.println("Neural Network created!");
        //nn.getOutputLayer().deactivateBias();
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
        
        ELM elm = new ELM(nn,neuralDataSet);
        elm.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
        elm.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
        elm.setMinOverallError(0.0001);
        elm.printTraining=true;
        
        
        try{
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
