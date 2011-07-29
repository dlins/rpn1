/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import java.util.ArrayList;
import wave.util.RealVector;
import wave.util.RealSegment;
import wave.multid.CoordsArray;



public class ShockCurve extends Orbit implements RpSolution {

    public ShockCurve(OrbitPoint[] points, int flag) {
        super(points, flag);

    }

    private static CoordsArray[] coordsArrayFromRealSegments(List segments) {

        ArrayList tempCoords = new ArrayList(segments.size());

        for (int i = 0; i < segments.size(); i++) {

            RealSegment segment = (RealSegment) segments.get(i);
            tempCoords.add(new CoordsArray(segment.p1()));
            tempCoords.add(new CoordsArray(segment.p2()));

        }
        CoordsArray[] coords = new CoordsArray[tempCoords.size()];
        for (int i = 0; i < tempCoords.size(); i++) {
            coords[i] = (CoordsArray) tempCoords.get(i);
        }
        tempCoords = null;
        return coords;

    }

    

   
//    public int findClosestSegment(RealVector targetPoint, double alpha) {
//        return 0;
//    }

    public RPnCurve cut() {
        return null;
    }

    public RPnCurve concat(RPnCurve curve) {
        return null;
    }
}
