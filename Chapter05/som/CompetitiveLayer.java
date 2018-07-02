/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.som;
import edu.packt.neuralnet.NeuralNet;
import edu.packt.neuralnet.Neuron;
import edu.packt.neuralnet.OutputLayer;
import edu.packt.neuralnet.math.ArrayOperations;
import edu.packt.neuralnet.math.Linear;

/**
 *
 * @author fab
 */
public class CompetitiveLayer extends OutputLayer {
    
    public Kohonen.MapDimension dimension;
    
    public Kohonen.Distance distanceCalculation = Kohonen.Distance.EUCLIDIAN;
    
    protected int[][] coordNeuron;
    
    public double initialRadius=1.0;
    
    public Neuron winnerNeuron;
    
    public int[] winnerIndex;
    
    public CompetitiveLayer(NeuralNet _neuralNet,int numberOfNeurons,int numberOfInputs,int dimension){
        super(_neuralNet,numberOfNeurons,new Linear(1.0),numberOfInputs);
        switch(dimension){
            case 1:
                this.dimension=Kohonen.MapDimension.ONE_DIMENSION;
                break;
            case 0:
            default:                
                this.dimension=Kohonen.MapDimension.ZERO;
                break;
        }
        coordNeuron=new int[numberOfNeurons][1];
        winnerIndex=new int[1];
        for(int i=0;i<numberOfNeurons;i++){
            coordNeuron[i][0]=i;
        }          
        
    }
    
    protected CompetitiveLayer(NeuralNet _neuralNet,int numberOfNeurons,int numberOfInputs){
        super(_neuralNet,numberOfNeurons,new Linear(1.0),numberOfInputs);
    }
    
    public double neighborhood(int u, int v, int s,int t){
        double result;
        switch(dimension){
            case ZERO: 
                if(u==v)
                    result=1.0;
                else
                    result=0.0;
                break;
            case ONE_DIMENSION:
            default:
                double exponent=-(neuronDistance(u,v)/neighborhoodRadius(s,t));
                result=Math.exp(exponent);
        }
        return result;
    }
    
    public double neuronDistance(int u,int v){
        return Math.abs(coordNeuron[u][0]-coordNeuron[v][0]);
    }
    
    public double neighborhoodRadius(int s,int t){
        return this.initialRadius*Math.exp(-((double)s/(double)t));
    }
    
    @Override
    public void calc(){
        if(input!=null && neuron!=null){
            double[] result = new double[numberOfNeuronsInLayer];
            for(int i=0;i<numberOfNeuronsInLayer;i++){
                neuron.get(i).setInputs(this.input);
                neuron.get(i).calc();
                result[i]=getWeightDistance(i);
                try{
                    output.set(i,0.0);
                }
                catch(IndexOutOfBoundsException iobe){
                    output.add(0.0);
                }
            }
            winnerIndex[0]=ArrayOperations.indexmin(result);
            winnerNeuron=neuron.get(winnerIndex[0]);
            output.set(winnerIndex[0], 1.0);
        }
    }
    
    public double[] getNeuronWeights(int n){
        double[] nweights = neuron.get(n).getWeights();
        double[] result = new double[nweights.length-1];
        for(int i=0;i<result.length;i++){
            result[i]=nweights[i];
        }
        return result;
    }
    
    public int getIndexWinnerNeuron(){
        return winnerIndex[0];
    }
    
    public int[] get2DIndexWinnerNeuron(){
        return winnerIndex;
    }
    
    public double getWeightDistance(int neuron){
        double[] inputs = this.getInputs();
        double[] weights = this.getNeuronWeights(neuron);
        int n=this.numberOfInputs;
        double result=0.0;
        switch(distanceCalculation){
            case EUCLIDIAN:
            default:
                for(int i=0;i<n;i++){
                    result+=Math.pow(inputs[i]-weights[i],2);
                }
                result=Math.sqrt(result);
        }
        return result;
    }
    
    public double[][] getWeights(){
        int n=this.numberOfNeuronsInLayer;
        int p=this.numberOfInputs;
        double[][] result = new double[n][p];
        for(int i=0;i<n;i++){
            result[i]=getNeuronWeights(i);
        }
        return result;
    }
}
