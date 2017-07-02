/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.packt.neuralnet.data;

import java.util.ArrayList;
import java.util.Dictionary;

/**
 *
 * @author fab
 */
public class CategoricalDataSet extends DataSet {
    
   
    
    public ArrayList<ArrayList<String>> categoricalData;
    
    public ArrayList<Dictionary<String,Double>> ordinalCategory;
    
    public ArrayList<String> allColumns;
    
    public enum ColumnType { REAL, ORDINAL, CATEGORICAL}
    
    public ArrayList<ColumnType> columnTypes;
    
    public CategoricalDataSet(String[][] _data){
        super();
        numberOfRecords=_data.length;
        numberOfColumns=_data[0].length;
        boolean[] isCategorical=new boolean[numberOfColumns];
        columns = new ArrayList<>();
        for(int i=0;i<numberOfColumns;i++){
            allColumns.add("Column"+String.valueOf(i));
            isCategorical[i]=false;
        }
        categoricalData = new ArrayList<>();
        for(int i=0;i<numberOfRecords;i++){
            categoricalData.add(new ArrayList<String>());
            for(int j=0;j<numberOfColumns;j++){
                if(!isCategorical[i]){
                    //double value = 
                }
                categoricalData.get(i).add(_data[i][j]);
            }
        }
    }
    
    public int[] checkCategoricalColumns(){
        return new int[0];
    }
    
}


/*v_estoque mme on mdme.id_mov_estoque=mme.id_mov_estoque
inner join me_produtos mp on mdme.id_produto=mp.id_produto
inner join zs_registros_detalhes zg on mp.rdt_grupo=zg.id_registro_detalhe
inner join zs_registros_detalhes zs on mp.rdt_sub_grupo=zs.id_registro_detalhe
where mme.ic_operacao_estoque=1*/