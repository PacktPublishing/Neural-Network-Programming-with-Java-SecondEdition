
package edu.packt.neuralnet.math;

/**
 * This class generates an identity matrix
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 */
public class IdentityMatrix extends Matrix{
    
	/**
     * IdentityMatrix constructor
     * @param identity matrix order value
     */
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
