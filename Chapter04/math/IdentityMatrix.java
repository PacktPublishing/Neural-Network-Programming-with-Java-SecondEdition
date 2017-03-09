/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.math;

/**
 *
 * @author fab
 */
public class IdentityMatrix extends Matrix{
    
    public IdentityMatrix(int order){
        super(order,order);
        for(int i=0;i<order;i++)
            for(int j=0;j<order;j++)
                setValue(i,j,(i==j)?1:0);
    }
    
    // to prevent editions on this matrix
    public void setValue(int row, int column){
        
    }
    
}
