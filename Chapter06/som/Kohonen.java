package edu.packt.neuralnet.som;
import edu.packt.neuralnet.HiddenLayer;
import edu.packt.neuralnet.InputLayer;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.OutputLayer;
import edu.packt.neuralnet.init.WeightInitialization;
import edu.packt.neuralnet.math.Linear;
import java.util.ArrayList;

/**
 * This class implements the Kohonen algorithm.
 * 
 * @author Alan de Souza, Fabio Soares
 * @version 0.1
 *
 */
public class Kohonen extends NeuralNet {
    
    public enum MapDimension {ZERO,ONE_DIMENSION,TWO_DIMENSION};
    
    public enum Distance {EUCLIDIAN};
    
    public Kohonen.Distance distanceMeasure = Kohonen.Distance.EUCLIDIAN;
    
    public Kohonen(int numberofinputs, int numberofoutputs, WeightInitialization _weightInitialization, int dim){
        //super(numberofinputs,numberofoutputs,new Linear(1.0));
        weightInitialization=_weightInitialization;
        activeBias=false;
        numberOfHiddenLayers=0;
        neuronsInHiddenLayers = new int[numberOfHiddenLayers+1];
        indexesWeightPerLayer = new int[numberOfHiddenLayers+2];  
        indexesWeightPerLayer[numberOfHiddenLayers+1]=
                indexesWeightPerLayer[numberOfHiddenLayers] 
                    + neuronsInHiddenLayers[numberOfHiddenLayers]
                        *(numberOfInputs+1);
        numberOfInputs=numberofinputs;
        numberOfOutputs=numberofoutputs;
        input=new ArrayList<>(numberofinputs);
        inputLayer=new InputLayer(this,numberofinputs);
        outputLayer=new CompetitiveLayer(this,numberofoutputs, numberofinputs,dim);
        inputLayer.setNextLayer(outputLayer);
        setNeuralNetMode(NeuralNetMode.RUN);       
        deactivateBias();
    }
    
    public Kohonen(int numberofinputs, int noutx, int nouty, WeightInitialization _weightInitialization){
        super();
        //super(numberofinputs,noutx*nouty,new Linear(1.0));
        weightInitialization=_weightInitialization;
        activeBias=false;
        numberOfHiddenLayers=0;
        neuronsInHiddenLayers = new int[numberOfHiddenLayers+1];
        indexesWeightPerLayer = new int[numberOfHiddenLayers+2];  
        indexesWeightPerLayer[numberOfHiddenLayers+1]=
                indexesWeightPerLayer[numberOfHiddenLayers] 
                    + neuronsInHiddenLayers[numberOfHiddenLayers]
                        *(numberOfInputs+1);
        numberOfInputs=numberofinputs;
        numberOfOutputs=noutx*nouty;
        input=new ArrayList<>(numberofinputs);
        inputLayer=new InputLayer(this,numberofinputs);
        outputLayer=new CompetitiveLayer2D(this,noutx,nouty, numberofinputs);
        inputLayer.setNextLayer(outputLayer);
        setNeuralNetMode(NeuralNetMode.RUN); 
        deactivateBias();
    }
    
    public double[] getNeuronWeights(int n){
        return ((CompetitiveLayer)(outputLayer)).getNeuronWeights(n);
    }
    
    public double[] getNeuronWeights(int x,int y){
        return ((CompetitiveLayer2D)(outputLayer)).getNeuronWeights(x,y);
    }
    
    public int getIndexWinnerNeuron(){
        return ((CompetitiveLayer)(outputLayer)).getIndexWinnerNeuron();
    }
    
}
