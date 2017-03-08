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
public class UniformInitialization extends WeightInitialization {
    
    private double min;
    private double max;
    
    public UniformInitialization(double _min,double _max){
        this.min=_min;
        this.max=_max;
    }
    
    @Override
    public double Generate(){
        return RandomNumberGenerator.GenerateBetween(min, max);
    }
    
}
