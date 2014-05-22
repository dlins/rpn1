/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.component.MultidAdapter;

public class RiemannProfile extends RPnCurve  {
    //
    // Members
    //


    private OrbitPoint[] points_;


    //
    // Constructor
    //
   
    public RiemannProfile(OrbitPoint[] points) {
        super(MultidAdapter.converseCoordsArrayToRealSegments(MultidAdapter.converseOrbitPointsToCoordsArray(points)));
        points_ = points;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n points = ");
        for (int i = 0; i < points_.length; i++) {


            buf.append("[" + i + "] = " + points_[i]+" lambda: "+ points_[i].getSpeed());
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
 

   
}
