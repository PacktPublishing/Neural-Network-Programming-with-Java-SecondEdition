/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.init;

import edu.packt.neuralnet.math.RandomNumberGenerator;

/**
 *
 * @author fab
 */
public class GaussianInitialization extends WeightInitialization {
    
    private double mean;
    
    private double stdev;
    
    public GaussianInitialization(double _mean,double _stdev){
        this.mean=_mean;
        this.stdev=_stdev;
    }
    
    @Override 
    public double Generate(){
        return RandomNumberGenerator.GenerateGaussian(mean, stdev);
    }
    
}
