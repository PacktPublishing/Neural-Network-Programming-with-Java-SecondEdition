
package edu.packt.neuralnet.math;

/**
 * 
 * This class has several methods to manipulate matrixes mathematically
 * It is used by ELM and Levenberg-Marquardt learning algorithms
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class Matrix {
    final double[][] content;
    final int numberOfRows;
    final int numberOfColumns;
    

    private Double determinant;
    
    public Matrix(int nRows,int nColumns){
        numberOfRows=nRows;
        numberOfColumns=nColumns;
        
        content = new double[numberOfRows][numberOfColumns];
    }
    
    public Matrix(double[][] matrix){
        numberOfRows = matrix.length;
        numberOfColumns = matrix[0].length;
        content = matrix;
        if(numberOfRows==numberOfColumns)
            this.determinant=determinant();
    }
    
    public Matrix(double[] vector){
        numberOfRows = 1;
        numberOfColumns = vector.length;
        content = new double[numberOfRows][numberOfColumns];
        content[0]=vector;
    }
    
    public Matrix(double[] vector,boolean column){
        if(column==true){
            Matrix result=new Matrix(vector).transpose();
            content=result.content;
            numberOfRows=vector.length;
            numberOfColumns=1;
        }
        else{
            Matrix result=new Matrix(vector);
            content=result.content;
            numberOfRows=1;
            numberOfColumns=vector.length;
        }
    }
    
    
    public Matrix(Matrix a){
        numberOfRows=a.getNumberOfRows();
        numberOfColumns=a.getNumberOfColumns();
        content = new double[numberOfRows][numberOfColumns];
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++){
                setValue(i,j,a.getValue(i,j));
            }
        }
        if(numberOfRows==numberOfColumns)
            this.determinant=a.determinant;
    }
    
    public Matrix add(Matrix a){
        int nRows = a.getNumberOfRows();
        int nColumns = a.getNumberOfColumns();
        
        if(numberOfRows!=a.getNumberOfRows())
            throw new IllegalArgumentException("Number of rows of both matrices must match");
        
        if(numberOfColumns!=a.getNumberOfColumns())
            throw new IllegalArgumentException("Number of colmns of both matrices must match");
        
        Matrix result = new Matrix(nRows,nColumns);
        
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                result.setValue(i, j, getValue(i,j)+a.getValue(i, j));
            }
        }
        
        return result;
    }
    
    public static Matrix add(Matrix a,Matrix b){
        int nRows = a.getNumberOfRows();
        int nColumns = a.getNumberOfColumns();
        
        if(a.numberOfRows!=b.getNumberOfRows())
            throw new IllegalArgumentException("Number of rows of both matrices must match");
        
        if(a.numberOfColumns!=b.getNumberOfColumns())
            throw new IllegalArgumentException("Number of colmns of both matrices must match");
        
        Matrix result = new Matrix(nRows,nColumns);
        
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                result.setValue(i, j, a.getValue(i,j)+b.getValue(i, j));
            }
        }
        
        return result;
    }
    
    public Matrix subtract(Matrix a){
        int nRows = a.getNumberOfRows();
        int nColumns = a.getNumberOfColumns();
        
        if(numberOfRows!=a.getNumberOfRows())
            throw new IllegalArgumentException("Number of rows of both matrices must match");
        
        if(numberOfColumns!=a.getNumberOfColumns())
            throw new IllegalArgumentException("Number of colmns of both matrices must match");
        
        Matrix result = new Matrix(nRows,nColumns);
        
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                result.setValue(i, j, getValue(i,j)-a.getValue(i, j));
            }
        }
        
        return result;
    }  
    
    public static Matrix subtract(Matrix a,Matrix b){
        int nRows = a.getNumberOfRows();
        int nColumns = a.getNumberOfColumns();
        
        if(a.numberOfRows!=b.getNumberOfRows())
            throw new IllegalArgumentException("Number of rows of both matrices must match");
        
        if(a.numberOfColumns!=b.getNumberOfColumns())
            throw new IllegalArgumentException("Number of colmns of both matrices must match");
        
        Matrix result = new Matrix(nRows,nColumns);
        
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nColumns;j++){
                result.setValue(i, j, a.getValue(i,j)-b.getValue(i, j));
            }
        }
        return result;
    }    
    
    public Matrix transpose(){
        Matrix result = new Matrix(numberOfColumns,numberOfRows);
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++){
                result.setValue(j, i, getValue(i,j));
            }
        }
        return result;
    }
    
    public static Matrix transpose(Matrix a){
        Matrix result = new Matrix(a.getNumberOfColumns(),a.getNumberOfRows());
        for(int i=0;i<a.getNumberOfRows();i++){
            for(int j=0;j<a.getNumberOfColumns();j++){
                result.setValue(j, i, a.getValue(i,j));
            }
        }
        return result;
    }
    
    public Matrix multiply(Matrix a){
        Matrix result = new Matrix(getNumberOfRows(),a.getNumberOfColumns());
        if(getNumberOfColumns()!=a.getNumberOfRows())
            throw new IllegalArgumentException("Number of Columns of first Matrix must match the number of Rows of second Matrix");

        for(int i=0;i<getNumberOfRows();i++){
            for(int j=0;j<a.getNumberOfColumns();j++){
                double value = 0;
                for(int k=0;k<a.getNumberOfRows();k++){
                    value+=getValue(i,k)*a.getValue(k,j);
                }
                result.setValue(i, j, value);
            }
        }
        return result;
    }
    
    public Matrix multiply(double a){
        Matrix result = new Matrix(getNumberOfRows(),getNumberOfColumns());
        
        for(int i=0;i<getNumberOfRows();i++){
            for(int j=0;j<getNumberOfColumns();j++){
                result.setValue(i, j, getValue(i,j)*a);
            }
        }
        
        return result;
    }
    
    public static Matrix multiply(Matrix a,Matrix b){
        Matrix result = new Matrix(a.getNumberOfRows(),b.getNumberOfColumns());
        if(a.getNumberOfColumns()!=b.getNumberOfRows())
            throw new IllegalArgumentException("Number of Columns of first Matrix must match the number of Rows of second Matrix");

        for(int i=0;i<a.getNumberOfRows();i++){
            for(int j=0;j<b.getNumberOfColumns();j++){
                double value = 0;
                for(int k=0;k<b.getNumberOfRows();k++){
                    value+=a.getValue(i,k)*b.getValue(k,j);
                }
                result.setValue(i, j, value);
            }
        }
        return result;
    }
    
    public static Matrix multiply(Matrix a, double b){
        Matrix result = new Matrix(a.getNumberOfRows(),a.getNumberOfColumns());
        
        for(int i=0;i<a.getNumberOfRows();i++){
            for(int j=0;j<a.getNumberOfColumns();j++){
                result.setValue(i, j, a.getValue(i,j)*b);
            }
        }
        
        return result;
    }    
    
    public Matrix[] LUdecomposition(){
        Matrix[] result = new Matrix[3];
        Matrix LU = new Matrix(this);
        Matrix L = new Matrix(LU.getNumberOfRows(),LU.getNumberOfColumns());
        Matrix SignP = new Matrix(new IdentityMatrix(LU.getNumberOfColumns()));
        int permutations=0;
        L.setZeros();
        L.setValue(0,0,1.0);
        for(int i=1;i<LU.getNumberOfRows();i++){
            L.setValue(i,i,1.0);
            for(int j=0;j<i;j++){
                double multiplier = -LU.getValue(i, j)/LU.getValue(j, j);
                LU.sumRowByRow(i, j, multiplier);
                if(j==i-1){
                    double value= LU.getValue(i, i);
                    if(value==0){
                        int rowNextNonZero=LU.rowNextNonZeroValue(i, i+1, numberOfRows-1);
                        LU.permutateRow(i, rowNextNonZero);
                        permutations++;
                    }
                }
                L.setValue(i, j, -multiplier);
            }
        }
        Matrix U = new Matrix(LU);
        if(permutations%2==1)
            SignP.setValue(0, 0, -1);
        result[0]=L;
        result[1]=U;
        result[2]=SignP;
        return result;
    }
    
    public void multiplyRow(int row, double multiplier){
        if(row>getNumberOfRows())
            throw new IllegalArgumentException("Row index must be lower than the number of rows");
        sumRowByRow(row,row,multiplier);
    }
    
    public void sumRowByRow(int row,int rowSum, double multiplier){
        if(row>getNumberOfRows())
            throw new IllegalArgumentException("Row index must be lower than the number of rows");
        if(rowSum>getNumberOfRows())
            throw new IllegalArgumentException("Row index must be lower than the number of rows");
        for(int j=0;j<getNumberOfColumns();j++){
            setValue(row,j,getValue(row,j)+getValue(rowSum,j)*multiplier);
        }
    }
    
    public int rowNextNonZeroValue(int atColumn,int fromRow,int untilRow){
        for(int i=fromRow;i<=untilRow;i++){
            if(getValue(i, atColumn)!=0)
                return i;
        }
        return -1;
    }
    
    public void permutateRow(int rowI,int rowJ){
        double[] auxRow=getRow(rowI);
        double[] permRow=getRow(rowJ);
        setRow(rowI, permRow);
        setRow(rowJ, auxRow);
    }
    
    public double determinant(){
        if(determinant!=null)
            return determinant;
        
        double result = 0;
        if(getNumberOfRows()!=getNumberOfColumns())
            throw new IllegalArgumentException("Only square matrices can have determinant");

        if(getNumberOfColumns()==1){
            return content[0][0];
        }
        else if(getNumberOfColumns()==2){
            return (content[0][0]*content[1][1])-(content[1][0]*content[0][1]);
        }
        else{
            Matrix[] LU = LUdecomposition();
            return LU[1].multiply(LU[2]).multiplyDiagonal();
        }
//        else{
//            for(int k=0;k<getNumberOfColumns();k++){
//                Matrix minorMatrix = subMatrix(0,k);
//                result+= ((k%2==0)? getValue(0,k): -getValue(0,k)) * minorMatrix.determinant();
//            }
//            setDeterminant(result);
//            return result;
//        }
    }
    
    private void setDeterminant(double det){
        determinant = det;
    }
    private double getDeterminant(){
        if(determinant!=null)
            return determinant;
        else
            return determinant();
    }
    
    public static double determinant(Matrix a){
        if(a.determinant!=null)
            return a.getDeterminant();
        
        if(a.getNumberOfRows()!=a.getNumberOfColumns())
            throw new IllegalArgumentException("Only square matrices can have determinant");

        if(a.getNumberOfColumns()==1){
            return a.getValue(0, 0);
        }
        else if(a.getNumberOfColumns()==2){
            return (a.getValue(0, 0)*a.getValue(1, 1))-(a.getValue(1, 0)*a.getValue(0, 1));
        }
        else{
            Matrix[] LU = a.LUdecomposition();
            return LU[1].multiply(LU[2]).multiplyDiagonal();
        }        
//        for(int k=0;k<a.getNumberOfColumns();k++){
//            Matrix minorMatrix = a.subMatrix(0, k);
//            result+= ((k%2==0)? a.getValue(0,k): -a.getValue(0,k)) * minorMatrix.determinant();
//        }
//        a.setDeterminant(result);
        
    }    
    
    public double multiplyDiagonal(){
        double result=1;
        for(int i=0;i<getNumberOfColumns();i++){
            result*=getValue(i,i);
        }
        return result;
    }
    
    public Matrix subMatrix(int row,int column){
        if(row>getNumberOfRows())
            throw new IllegalArgumentException("Row index out of matrix`s limits");
        if(column>getNumberOfColumns())
            throw new IllegalArgumentException("Column index out of matrix`s limits");
        
        Matrix result = new Matrix(getNumberOfRows()-1,getNumberOfColumns()-1);
        for(int i=0;i<getNumberOfRows();i++){
            if(i==row) continue;
            for(int j=0;j<getNumberOfRows();j++){
                if(j==column) continue;
                result.setValue((i<row?i:i-1), (j<column?j:j-1), getValue(i,j));
            }
        }
        return result;
    }
    
    public static Matrix subMatrix(Matrix a,int row,int column){
        if(row>a.getNumberOfRows())
            throw new IllegalArgumentException("Row index out of matrix`s limits");
        if(column>a.getNumberOfColumns())
            throw new IllegalArgumentException("Column index out of matrix`s limits");
        
        Matrix result = new Matrix(a.getNumberOfRows()-1,a.getNumberOfColumns()-1);
        for(int i=0;i<a.getNumberOfRows();i++){
            if(i==row) continue;
            for(int j=0;j<a.getNumberOfRows();j++){
                if(j==column) continue;
                result.setValue((i<row?i:i-1), (j<column?j:j-1), a.getValue(i,j));
            }
        }
        return result;
    }
    
    public Matrix subMatrix(int rowi,int rowe,int columni,int columne){
        if(rowi>getNumberOfRows()||rowe>getNumberOfRows())
            throw new IllegalArgumentException("Row index out of matrix`s limits");
        if(columni>getNumberOfColumns()||columne>getNumberOfColumns())
            throw new IllegalArgumentException("Column index out of matrix`s limits");
        if(rowe<rowi){
            int aux=rowi;
            rowi=rowe;
            rowe=aux;
        }
        if(columne<columni){
            int aux=columni;
            columni=columne;
            columne=aux;
        }
        Matrix result = new Matrix(rowe-rowi+1,columne-columni+1);
        for(int i=rowi;i<=rowe;i++){
            for(int j=columni;j<=columne;j++){
                result.setValue(i-rowi,j-columni,this.getValue(i,j));
            }
        }
        return result;
    }
    
    public static Matrix subMatrix(Matrix a,int rowi,int rowe,int columni,int columne){
        if(rowi>a.getNumberOfRows()||rowe>a.getNumberOfRows())
            throw new IllegalArgumentException("Row index out of matrix`s limits");
        if(columni>a.getNumberOfColumns()||columne>a.getNumberOfColumns())
            throw new IllegalArgumentException("Column index out of matrix`s limits");
        if(rowe<rowi){
            int aux=rowi;
            rowi=rowe;
            rowe=aux;
        }
        if(columne<columni){
            int aux=columni;
            columni=columne;
            columne=aux;
        }
        Matrix result = new Matrix(rowe-rowi+1,columne-columni+1);
        for(int i=rowi;i<=rowe;i++){
            for(int j=columni;j<=columne;j++){
                result.setValue(i-rowi,j-columni,a.getValue(i,j));
            }
        }
        return result;
    }
    
    public Matrix coFactors(){
        Matrix result = new Matrix(getNumberOfRows(),getNumberOfColumns());
        for(int i=0;i<getNumberOfRows();i++){
            for(int j=0;j<getNumberOfColumns();j++){
                result.setValue(i, j, Math.pow(-1.0, i+j)*subMatrix(i,j).determinant());
            }
        }
        return result;
    }
    
    public static Matrix coFactors(Matrix a){
        Matrix result = new Matrix(a.getNumberOfRows(),a.getNumberOfColumns());
        for(int i=0;i<a.getNumberOfRows();i++){
            for(int j=0;j<a.getNumberOfColumns();j++){
                result.setValue(i, j, a.subMatrix(i,j).determinant());
            }
        }
        return result;
    }    
    
    public Matrix inverse(){
//        Matrix CoF = coFactors();
//        Matrix CoFT = CoF.transpose();
//        double det = (1/determinant());
//        Matrix result = CoFT.multiply(det);
        
        Matrix[] LU = this.LUdecomposition();
        Matrix L=LU[0];
        Matrix U=LU[1];
        
        Matrix Z=new Matrix(this.numberOfRows,this.numberOfColumns);
        Matrix result = new Matrix(this.numberOfRows,this.numberOfColumns);
        
        int N=this.numberOfRows;

        for(int p=0;p<N;p++){ // columns
            for(int m=0;m<N;m++){ //rows
                double value=0.0;
                if(m==p){
                    value=1.0;
                }
                for(int i=p;i<m;i++){
                    value-=L.getValue(m, i)*Z.getValue(i, p);
                }
                Z.setValue(m, p, value);
            }
        }
        for(int p=0;p<N;p++){
            for(int m=N-1;m>=0;m--){
                double value=Z.getValue(m, p);
                for(int i=m+1;i<N;i++){
                    value-=U.getValue(m, i)*result.getValue(i, p);
                }
                value/=U.getValue(m, m);
                result.setValue(m,p,value);
            }
        }
        
        return result;
    }
    
    public static Matrix inverse(Matrix a){
        if(a.getDeterminant()==0)
            throw new IllegalArgumentException("This matrix is not inversible");
        Matrix result = a.coFactors().transpose().multiply((1/a.determinant()));
        return result;
    }
    
    public double getValue(int i,int j){
        if(i>=numberOfRows)
            throw new IllegalArgumentException("Number of Row outside the matrix`s limits");
        if(j>=numberOfColumns)
            throw new IllegalArgumentException("Number of Column outside the matrix`s limits");
        
        return content[i][j];
    }
    
    public double[] getRow(int row){
        double[] result=new double[numberOfColumns];
        for(int j=0;j<numberOfColumns;j++){
            result[j]=content[row][j];
        }
        return result;
    }
    
    public double[] getColumn(int column){
        double[] result=new double[numberOfRows];
        for(int i=0;i<numberOfRows;i++){
            result[i]=content[i][column];
        }
        return result;
    }
    
    public void setRow(int row,double[] values){
        for(int j=0;j<numberOfColumns;j++){
            content[row][j]=values[j];
        }
    }
    
    public void setColumn(int column,double[] values){
        for(int i=0;i<numberOfRows;i++){
            content[i][column]=values[i];
        }
    }
    
    public void setValue(int i,int j,double value){
        if(i>=numberOfRows)
            throw new IllegalArgumentException("Number of Row outside the matrix`s limits");
        if(j>=numberOfColumns)
            throw new IllegalArgumentException("Number of Column outside the matrix`s limits");
                
        content[i][j]=value;
        determinant = null;
    }
    
    public void setZeros(){
        for(int i=0;i<getNumberOfRows();i++){
            for(int j=0;j<getNumberOfColumns();j++){
                setValue(i,j,0.0);
            }
        }
    }
    
    public void setOnes(){
        for(int i=0;i<getNumberOfRows();i++){
            for(int j=0;j<getNumberOfColumns();j++){
                setValue(i,j,1.0);
            }
        }
    }
    
    public int getNumberOfRows(){
        return numberOfRows;
    }
    
    public int getNumberOfColumns(){
        return numberOfColumns;
    }
        
}
