/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.List;
import wave.util.RealVector;

public class DiagramLine   {
    //
    // Members
    //

    private final List<List<RealVector>> coords_;

    private int size_;
    
    private int partsNumber_;
    
    private List<Integer> typesID_;
    //
    // Constructor
    //
   
    public DiagramLine(List<List<RealVector>> coords) {

        coords_= coords;
        partsNumber_=coords.size();
        typesID_=new ArrayList<Integer>(partsNumber_);
        
        for (List<RealVector> list : coords) {
            
            size_+=list.size();
            
        }
    
    }
    
    
    
    public DiagramLine() {

        coords_= new ArrayList<List<RealVector>>();
        
        partsNumber_=coords_.size();
        typesID_=new ArrayList<Integer>(partsNumber_);
        
        for (List<RealVector> list : coords_) {
            
            size_+=list.size();
            
        }
    
    }
    
    
    
    
    public void addPart(List<RealVector> part){
        coords_.add(part);
    }

    
    
    
    public List<List<RealVector>> getCoords() {
        return coords_;
    }

    public int getSize() {
        return size_;
    }

   
    public void setType(int partIndex, int partType){
        typesID_.set(partIndex, new Integer(partType));
    }
    
    
    public int getType (int partIndex){
        
        return typesID_.get(partIndex);
    }



    public String toXML() {

        return toString();
    }

    

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 

   
}
