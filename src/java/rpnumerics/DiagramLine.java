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

    }

    public DiagramLine() {

        coords_ = new ArrayList<List<RealVector>>();

        partsNumber_ = coords_.size();
        typesID_ = new HashMap<Integer, Integer>();

        for (List<RealVector> list : coords_) {

            size_ += list.size();

        }

    }

    public List<RealSegment> getSegments() {

        List<RealVector> singleRealVectorList = new ArrayList<RealVector>();

        for (List<RealVector> realVectorList : getCoords()) {
            singleRealVectorList.addAll(realVectorList);
        }

        CoordsArray[] toTransformArray = new CoordsArray[singleRealVectorList.size()];
        
        
        for (int i = 0; i < toTransformArray.length; i++) {
            toTransformArray[i]= new CoordsArray(singleRealVectorList.get(i));
            
        }
        
        
        

        return MultidAdapter.converseCoordsArrayToRealSegments(toTransformArray);

    }

    public void addPart(List<RealVector> part) {
        coords_.add(part);
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
