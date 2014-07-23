/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rpn.component.MultidAdapter;
import wave.multid.CoordsArray;
import wave.util.RealSegment;
import wave.util.RealVector;

public class DiagramLine {
    //
    // Members
    //

    private final List<List<RealVector>> coords_;
    
    private List<RealSegment> segments_;

    private int size_;

    private int partsNumber_;

    private HashMap<Integer, Integer> typesID_;
    //
    // Constructor
    //

    public DiagramLine(List<List<RealVector>> coords) {

        coords_ = coords;
        partsNumber_ = coords.size();
        typesID_ = new HashMap<Integer, Integer>(partsNumber_);

        for (List<RealVector> list : coords) {

            size_ += list.size();

        }
        
        segments_=createSegmentsList();

    }

    public DiagramLine() {

        coords_ = new ArrayList<List<RealVector>>();

        partsNumber_ = coords_.size();
        typesID_ = new HashMap<Integer, Integer>();

        for (List<RealVector> list : coords_) {

            size_ += list.size();

        }
        
        segments_=createSegmentsList();

    }
    
    
    
    
    public RealVector getMin() {
       

        double minX = 0;
        double minY = 0;
        
            List<List<RealVector>> coords = getCoords();

            for (List<RealVector> list : coords) {

                for (RealVector realVector : list) {

                    if (realVector.getElement(0) < minX) {
                        minX = realVector.getElement(0);
                    }

                    if (realVector.getElement(1) < minY) {
                        minY = realVector.getElement(1);
                    }

                }
            
        }

        RealVector limits = new RealVector(2);
        limits.setElement(0, minX);
        limits.setElement(1, minY);
        return limits;

    }

    

    public RealVector getMax() {
       

        double maxX = 0;
        double maxY = 0;
      
            List<List<RealVector>> coords = getCoords();

            for (List<RealVector> list : coords) {

                for (RealVector realVector : list) {

                    if (realVector.getElement(0) > maxX) {
                        maxX = realVector.getElement(0);
                    }

                    if (realVector.getElement(1) > maxY) {
                        maxY = realVector.getElement(1);
                    }

                }
        }

        RealVector limits = new RealVector(2);
        limits.setElement(0, maxX);
        limits.setElement(1, maxY);
        return limits;

    }
    
    
    public List<RealSegment> getSegments() {

       return segments_;
    }
    
    
    
    private List<RealSegment> createSegmentsList() {

        List<RealVector> singleRealVectorList = new ArrayList<RealVector>();

        for (List<RealVector> realVectorList : coords_) {
            singleRealVectorList.addAll(realVectorList);
        }

        CoordsArray[] toTransformArray = new CoordsArray[singleRealVectorList.size()];
        
        
        for (int i = 0; i < toTransformArray.length; i++) {
            toTransformArray[i]= new CoordsArray(singleRealVectorList.get(i));
            
        }
        
        
        

        return MultidAdapter.converseCoordsArrayToRealSegments(toTransformArray);

    }

    
    
    public void addCoord(int partIndex, int coordIndex,RealVector coord){
        coords_.get(partIndex).add(coordIndex,coord);
        segments_=createSegmentsList();
    }
    
    public void addPart(List<RealVector> part) {
        coords_.add(part);
        segments_= createSegmentsList();
    }


    public List<List<RealVector>> getCoords() {
        return coords_;
    }

    public int getSize() {
        return size_;
    }

    public void setType(int partIndex, int partType) {
        typesID_.put(new Integer(partIndex), new Integer(partType));
    }

    public int getType(int partIndex) {

        return typesID_.get(partIndex);
    }

    public String toXML() {

        return toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
