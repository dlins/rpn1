/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;


import wave.multid.view.ViewingAttr;
import java.awt.Color;
import rpn.component.MultidAdapter;
import wave.util.RealSegment;

public class RiemannProfile extends RPnCurve  {
    //
    // Members
    //


    private OrbitPoint[] points_;
//    private List<RealSegment> segments_;


    //
    // Constructor
    //
   
    public RiemannProfile(OrbitPoint[] points) {
        super(MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseOrbitPointsToCoordsArray(points)));
//        segments_= MultidAdapter.converseRPnCurveToRealSegments(this);
        points_ = points;


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


    public String toXML() {

        return toString();
    }

    public OrbitPoint [] getPoints(){
        return points_;
    }
   

//    @Override
//    public List<RealSegment> segments() {
//        
//        return segments_;
//
//    }
    
}
