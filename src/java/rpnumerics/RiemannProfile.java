/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealVector;


import wave.multid.view.ViewingAttr;
import java.awt.Color;
import rpn.component.MultidAdapter;
import wave.util.RealSegment;

public class RiemannProfile extends RPnCurve implements RpSolution {
    //
    // Members
    //


    private OrbitPoint[] points_;


    //
    // Constructor
    //
   
    public RiemannProfile(OrbitPoint[] points) {
        super(MultidAdapter.converseOrbitPointsToCoordsArray(points), new ViewingAttr(Color.white));
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



    public OrbitPoint [] getPoints(){
        return points_;
    }
   

    @Override
    public List<RealSegment> segments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }




    


    
}
