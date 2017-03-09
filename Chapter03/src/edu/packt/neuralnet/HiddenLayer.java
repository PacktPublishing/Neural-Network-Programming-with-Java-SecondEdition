
package edu.packt.neuralnet;

import edu.packt.neuralnet.math.IActivationFunction;

/**
 *
 * HiddenLayer
 * This class extends from NeuralLayer and represents a hidden layer in a Neural 
 * Network
 * 
 * @authors Alan de Souza, Fábio Soares 
 * @version 0.1
 * 
 */
public class HiddenLayer extends NeuralLayer {
    
    /**
     * HiddenLayer constructor
     * 
     * @param numberofneurons Number of neurons in this hidden layer
     * @param iaf Activation Function for all neurons in this layer
     * @param numberofinputs Number of inputs in this layer
     * @param _neuralNet
     * @see HiddenLayer
     */
    public HiddenLayer(NeuralNet _neuralNet, int numberofneurons
            ,IActivationFunction iaf,int numberofinputs){
        super(_neuralNet,numberofneurons,iaf);
        numberOfInputs=numberofinputs;
        this.init(_neuralNet.getWeightInitialization());
    }
    
    /**
     * setPreviousLayer
     * This method links this layer to a previous layer in the Neural Network
     * 
     * @param previous Previous Neural Layer
     * @see HiddenLayer
     */
    @Override
    public void setPreviousLayer(NeuralLayer previous){
        this.previousLayer=previous;
        if(previous.nextLayer!=this)
            previous.setNextLayer(this);
    }
    
    /**
     * setNextLayer
     * This method links this layer to a next layer in the Neural Network
     * 
     * @param next Next Neural Layer
     * @see HiddenLayer
     */
    @Override
    public void setNextLayer(NeuralLayer next){
        nextLayer=next;
        if(next.previousLayer!=this)
            next.setPreviousLayer(this);
    }
    
}
