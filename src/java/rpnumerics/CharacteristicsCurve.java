/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;


import wave.util.RealSegment;

public class CharacteristicsCurve extends RpCurve {
    //
    // Members
    //


    private OrbitPoint[] points_;
    private List<RealSegment> segments_;
    private List<List<PhasePoint[]>> charPoints_;

    //
    // Constructor
    //
   
    public CharacteristicsCurve(List<List<PhasePoint[]>> points) {
        charPoints_=points;

    }
    
    
    public List<PhasePoint[]>getFamilyPoints(int familyIndex){
        
        return charPoints_.get(familyIndex);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n points = ");
        for (int i = 0; i < points_.length; i++) {


            buf.append("[" + i + "] = " + points_[i]+" lambda: "+ points_[i].getLambda());
            buf.append("\n");
        }
        return buf.toString();
    }



    public OrbitPoint [] getPoints(){
        return points_;
    }
   

    @Override
    public List<RealSegment> segments() {
        
        return segments_;

    }
}
