/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.som;

import edu.packt.neuralnet.NeuralNet;
import java.util.ArrayList;

/**
 *
 * @author fab
 */
public class CompetitiveLayer2D extends CompetitiveLayer {
    
    protected int sizeMapX;
    protected int sizeMapY;
    
    protected int[] winner2DIndex;
    
    public CompetitiveLayer2D(NeuralNet _neuralNet,int numberOfNeuronsX,int numberOfNeuronsY,int numberOfInputs){
        super(_neuralNet,numberOfNeuronsX*numberOfNeuronsY,numberOfInputs);
        this.dimension=Kohonen.MapDimension.TWO_DIMENSION;
        this.winnerIndex=new int[1];
        this.winner2DIndex=new int[2];
        this.coordNeuron=new int[numberOfNeuronsX*numberOfNeuronsY][2];
        this.sizeMapX=numberOfNeuronsX;
        this.sizeMapY=numberOfNeuronsY;
        for(int i=0;i<numberOfNeuronsY;i++){
            for(int j=0;j<numberOfNeuronsX;j++){
                coordNeuron[i*numberOfNeuronsX+j][0]=i;
                coordNeuron[i*numberOfNeuronsX+j][1]=j;
            }
        }
    }
    
    @Override
    public double neighborhood(int u, int v, int s,int t){
        double result;
        double exponent=-(neuronDistance(u,v)/neighborhoodRadius(s,t));
        result=Math.exp(exponent);

        return result;
    }
    
    @Override
    public double neuronDistance(int u,int v){
        double distance=Math.pow(coordNeuron[u][0]-coordNeuron[v][0],2);
        distance+=Math.pow(coordNeuron[u][1]-coordNeuron[v][1],2);
        return Math.sqrt(distance);
    }
    
    public double[] getNeuronWeights(int x,int y){
        double[] nweights = neuron.get(x*sizeMapX+y).getWeights();
        double[] result = new double[nweights.length-1];
        for(int i=0;i<result.length;i++){
            result[i]=nweights[i];
        }
        return result;
    }
    
    public double[][] getNeuronWeightsColumnGrid(int y){
        double[][] result = new double[sizeMapY][numberOfInputs];
        for(int i=0;i<sizeMapY;i++){
            result[i]=getNeuronWeights(i,y);
        }
        return result;
    }

    public double[][] getNeuronWeightsRowGrid(int x){
        double[][] result = new double[sizeMapX][numberOfInputs];
        for(int i=0;i<sizeMapX;i++){
            result[i]=getNeuronWeights(x,i);
        }
        return result;
    }

    
    public ArrayList<double[][]> getGridWeights(){
        ArrayList<double[][]> result = new ArrayList<>();
        for(int i=0;i<sizeMapY;i++){
            result.add(getNeuronWeightsRowGrid(i));
        }
        for(int j=0;j<sizeMapX;j++){
            result.add(getNeuronWeightsColumnGrid(j));
        }
        return result;
    }
   
}
