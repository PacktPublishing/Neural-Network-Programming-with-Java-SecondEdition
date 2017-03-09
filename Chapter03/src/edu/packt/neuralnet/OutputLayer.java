package edu.packt.neuralnet;

import edu.packt.neuralnet.math.IActivationFunction;

/**
 *
 * OutputLayer
 * This class represents an output layer of a neural network, inheriting from 
 * NeuralLayer, which contains all basic definitions of a Neural Layer
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class OutputLayer extends NeuralLayer {
    
    /**
     * OutputLayer constructor
     * @param numberofneurons Number of Neurons (and also the outputs) of this 
     * layer
     * @param iaf Activation Function of this layer
     * @param numberofinputs Number of Inputs of this layer
     * @param _neuralNet 
     */
    public OutputLayer(NeuralNet _neuralNet,int numberofneurons
            ,IActivationFunction iaf,int numberofinputs){
        super(_neuralNet,numberofneurons,iaf);
        numberOfInputs=numberofinputs;
        nextLayer=null;
        init(_neuralNet.getWeightInitialization());
    }
    
    public OutputLayer(NeuralNet _neuralNet,int numberofneurons
            ,IActivationFunction iaf,NeuralLayer _previousLayer){
        super(_neuralNet,numberofneurons,iaf);
        setPreviousLayer(_previousLayer);
        numberOfInputs=_previousLayer.getNumberOfNeuronsInLayer();
        init(_neuralNet.getWeightInitialization());
    }
    
    /**
     * setNextLayer
     * This method prevents any attempt to link this layer to a next one, 
     * provided that this should be always the last
     * @param layer Dummy layer 
     */
    @Override
    public void setNextLayer(NeuralLayer layer){
        nextLayer=null;
    }
    
    /**
     * setPreviousLayer
     * This method links this layer to the previous one
     * @param layer Previous Layer
     */
    @Override
    public void setPreviousLayer(NeuralLayer layer){
        previousLayer=layer;
        if(layer.nextLayer!=this)
            layer.setNextLayer(this);
    }
    
}
